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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

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

        // --- GENERATE ---

        public String generateAccessToken(User user, String sessionId) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(), CLAIM_TYPE, "ACCESS", CLAIM_SESSION_ID, sessionId),
                                user.getId().toString(),
                                accessExpiration,
                                accessSecret);
        }

        public String generateRefreshToken(User user, String sessionId) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(), CLAIM_TYPE, "REFRESH", CLAIM_SESSION_ID, sessionId),
                                user.getId().toString(),
                                refreshExpiration,
                                refreshSecret);
        }

        public String generateVerificationToken(User user) {
                return buildToken(
                                Map.of(CLAIM_USER_ID, user.getId(), CLAIM_EMAIL, user.getEmail(), CLAIM_TYPE,
                                                "EMAIL_VERIFICATION"),
                                user.getId().toString(),
                                verificationExpiration,
                                verificationSecret);
        }

        // --- VALIDATE ---

        public boolean validateToken(String token, String secret) {
                try {
                        getClaims(token, secret);
                        return true;
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateAccessToken(String token, UserDetails userDetails) {
                try {
                        Claims claims = getClaims(token, accessSecret);
                        return "ACCESS".equals(claims.get(CLAIM_TYPE))
                                        && userDetails.getUsername().equals(claims.get(CLAIM_EMAIL, String.class));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateRefreshToken(String token) {
                try {
                        Claims claims = getClaims(token, refreshSecret);
                        return "REFRESH".equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        public boolean validateVerificationToken(String token) {
                try {
                        Claims claims = getClaims(token, verificationSecret);
                        return "EMAIL_VERIFICATION".equals(claims.get(CLAIM_TYPE));
                } catch (Exception _) {
                        return false;
                }
        }

        public long getAccessExpiration() {
                return accessExpiration;
        }

        // --- EXTRACT (typed convenience) ---

        public String extractEmailFromVerificationToken(String token) {
                return extractEmail(token, verificationSecret);
        }

        // --- EXTRACT ---

        public String extractEmail(String token, String secret) {
                return extractClaim(token, c -> c.get(CLAIM_EMAIL, String.class), secret);
        }

        public String extractUserId(String token, String secret) {
                return extractClaim(token, Claims::getSubject, secret);
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

        private SecretKey getSigningKey(String secret) {
                return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        }
}
