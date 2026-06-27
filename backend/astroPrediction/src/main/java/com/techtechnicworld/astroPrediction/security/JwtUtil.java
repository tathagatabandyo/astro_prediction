package com.techtechnicworld.astroPrediction.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import java.util.function.Function;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.enums.TokenType;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtUtil {

        private static final String CLAIM_USER_ID = "userId";
        private static final String CLAIM_EMAIL = "email";
        private static final String CLAIM_TYPE = "type";
        private static final String CLAIM_SESSION_ID = "sessionId";

        @Value("${jwt.access.secret}")
        private String accessSecret;

        @Value("${jwt.access.expiration}")
        private long accessExpiration;

        @Value("${jwt.refresh.secret}")
        private String refreshSecret;

        @Value("${jwt.refresh.expiration}")
        private long refreshExpiration;

        @Value("${jwt.verification.secret}")
        private String verificationSecret;

        @Value("${jwt.verification.expiration}")
        private long verificationExpiration;

        @Value("${jwt.forgot.password.secret}")
        private String forgotPasswordSecret;

        @Value("${jwt.forgot.password.expiration}")
        private long forgotPasswordExpiration;

        // --- GENERATE ---

        public String generateAccessToken(User user, String sessionId) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(),
                                                CLAIM_TYPE, TokenType.ACCESS.name(), CLAIM_SESSION_ID, sessionId),
                                user.getId().toString(),
                                accessExpiration,
                                accessSecret);
        }

        public String generateRefreshToken(User user, String sessionId) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(),
                                                CLAIM_TYPE, TokenType.REFRESH.name(), CLAIM_SESSION_ID, sessionId),
                                user.getId().toString(),
                                refreshExpiration,
                                refreshSecret);
        }

        public String generateVerificationToken(User user) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(),
                                                CLAIM_TYPE, TokenType.EMAIL_VERIFICATION.name()),
                                user.getId().toString(),
                                verificationExpiration,
                                verificationSecret);
        }

        public String generateForgotPasswordToken(User user) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(),
                                                CLAIM_TYPE, TokenType.FORGOT_PASSWORD.name()),
                                user.getId().toString(),
                                verificationExpiration,
                                verificationSecret);
        }

        public boolean validateAccessToken(String token, UserDetails userDetails) {
                try {
                        Claims claims = getClaims(token, TokenType.ACCESS);
                        return TokenType.ACCESS.name().equals(claims.get(CLAIM_TYPE))
                                        && userDetails.getUsername().equals(claims.get(CLAIM_EMAIL, String.class));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateAccessToken(String token) {
                try {
                        Claims claims = getClaims(token, TokenType.ACCESS);
                        return TokenType.ACCESS.name().equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateRefreshToken(String token) {
                try {
                        Claims claims = getClaims(token, TokenType.REFRESH);
                        return TokenType.REFRESH.name().equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateVerificationToken(String token) {
                try {
                        Claims claims = getClaims(token, TokenType.EMAIL_VERIFICATION);
                        return TokenType.EMAIL_VERIFICATION.name().equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateToken(String token, TokenType tokenType) {
                try {
                        Claims claims = getClaims(token, tokenType);

                        // Explicit expired check (in addition to JWT signature/format validation).
                        Date expiration = claims.getExpiration();
                        if (expiration == null || !expiration.after(new Date())) {
                                return false;
                        }

                        return tokenType.name().equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        // --- EXPIRATION ---

        public long getAccessExpiration() {
                return accessExpiration;
        }

        public long getRefreshExpiration() {
                return refreshExpiration;
        }

        // --- EXTRACT (typed convenience) ---

        public String extractEmailFromAccessToken(String token) {
                return extractEmail(token, TokenType.ACCESS);
        }

        public String extractEmailFromVerificationToken(String token) {
                return extractEmail(token, TokenType.EMAIL_VERIFICATION);
        }

        public String extractSessionIdFromRefreshToken(String token) {
                return extractClaim(token, c -> c.get(CLAIM_SESSION_ID, String.class), refreshSecret);
        }

        public String extractSessionId(String token, TokenType tokenType) {
                return extractClaim(token, c -> c.get(CLAIM_SESSION_ID, String.class), getSecretByTokenType(tokenType));
        }

        public String extractTokenId(String token, TokenType tokenType) {
                return getClaims(token, tokenType).getId();
        }

        // --- EXTRACT ---

        public String extractEmail(String token, String secret) {
                return extractClaim(token, c -> c.get(CLAIM_EMAIL, String.class), secret);
        }

        public String extractEmail(String token, TokenType tokenType) {
                return extractEmail(token, getSecretByTokenType(tokenType));
        }

        public String extractUserId(String token, TokenType tokenType) {
                return extractClaim(token, Claims::getSubject, getSecretByTokenType(tokenType));
        }

        public boolean isTokenExpired(String token, String secret) {
                return extractClaim(token, Claims::getExpiration, secret).before(new Date());
        }

        public <T> T extractClaim(String token, Function<Claims, T> resolver, String secret) {
                return resolver.apply(getClaims(token, secret));
        }

        // --- PRIVATE ---

        private String buildToken(Map<String, Object> claims, String subject, long expirationMillis, String secret) {
                Instant now = Instant.now();
                return Jwts.builder()
                                .claims(claims)
                                .subject(subject)
                                .id(UUID.randomUUID().toString())
                                .issuedAt(Date.from(now))
                                .expiration(Date.from(now.plusMillis(expirationMillis)))
                                .signWith(getSigningKey(secret))
                                .compact();
        }

        private Claims getClaims(String token, String secret) {
                return Jwts.parser()
                                .verifyWith(getSigningKey(secret))
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
        }

        private Claims getClaims(String token, TokenType tokenType) {
                return getClaims(token, getSecretByTokenType(tokenType));
        }

        private String getSecretByTokenType(TokenType tokenType) {
                return switch (tokenType) {
                        case ACCESS -> accessSecret;
                        case REFRESH -> refreshSecret;
                        case EMAIL_VERIFICATION -> verificationSecret;
                        case FORGOT_PASSWORD -> forgotPasswordSecret;
                        default -> throw new IllegalArgumentException("Unexpected value: " + tokenType);
                };
        }

        private SecretKey getSigningKey(String secret) {
                return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }

        public static String getTokenFromRequest(HttpServletRequest httpServletRequest) {
                String authHeader = httpServletRequest.getHeader("Authorization");

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                        return authHeader.substring(7);
                }
                return null;
        }
}
