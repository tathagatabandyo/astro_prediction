package com.techtechnicworld.astroPrediction.security;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
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

        /*
         * EMAIL VERIFICATION TOKEN
         */

        public String generateVerificationToken(User user) {

                Map<String, Object> claims = new HashMap<>();

                claims.put("userId", user.getId());
                claims.put("email", user.getEmail());
                claims.put("type", "EMAIL_VERIFICATION");

                return buildToken(
                                claims,
                                user.getId().toString(),
                                verificationExpiration,
                                verificationSecret);
        }

        /*
         * ACCESS TOKEN
         */
        public String generateAccessToken(User user) {

                Map<String, Object> claims = new HashMap<>();

                claims.put("userId", user.getId());
                claims.put("email", user.getEmail());

                return buildToken(
                                claims,
                                user.getId().toString(),
                                accessExpiration,
                                accessSecret);
        }

        /*
         * REFRESH TOKEN
         */
        public String generateRefreshToken(User user) {

                Map<String, Object> claims = new HashMap<>();

                claims.put("userId", user.getId());
                claims.put("type", "REFRESH");

                return buildToken(
                                claims,
                                user.getId().toString(),
                                refreshExpiration,
                                refreshSecret);
        }

        /*
         * COMMON TOKEN BUILDER
         */
        private String buildToken(
                        Map<String, Object> claims,
                        String subject,
                        long expirationMillis,
                        String secret) {

                Instant now = Instant.now();

                Instant expiry = now.plusMillis(expirationMillis);

                return Jwts.builder()
                                .claims(claims)
                                .subject(subject)
                                .issuedAt(Date.from(now))
                                .expiration(Date.from(expiry))
                                .signWith(getSigningKey(secret))
                                .compact();
        }

        public boolean validateToken(String token, String secret) {
                try {
                        Jwts.parser()
                                        .verifyWith(getSigningKey(secret))
                                        .build()
                                        .parseSignedClaims(token);

                        return true;

                } catch (Exception ex) {
                        return false;
                }
        }

        /*
         * ACCESS TOKEN VALIDATION
         */

        public boolean validateAccessToken(
                        String token,
                        UserDetails userDetails) {

                String email = extractEmail(token, accessSecret);

                return email.equals(
                                userDetails.getUsername())
                                && !isTokenExpired(token, accessSecret);
        }

        /*
         * REFRESH TOKEN VALIDATION
         */

        public boolean validateRefreshToken(
                        String token) {

                Claims claims = getClaims(token, refreshSecret);

                return "REFRESH".equals(
                                claims.get("type"))
                                && !isTokenExpired(
                                                token,
                                                refreshSecret);
        }

        /*
         * EMAIL VERIFICATION VALIDATION
         */

        public boolean validateVerificationToken(String token) {

                Claims claims = getClaims(
                                token,
                                verificationSecret);

                return "EMAIL_VERIFICATION".equals(claims.get("type")) &&
                                !isTokenExpired(
                                                token,
                                                verificationSecret);
        }

        private Claims getClaims(String token, String secret) {
                return Jwts.parser()
                                .verifyWith(getSigningKey(secret))
                                .build()
                                .parseSignedClaims(token)
                                .getPayload();
        }

        public <T> T extractClaim(String token, Function<Claims, T> resolver, String secret) {
                return resolver.apply(getClaims(token, secret));
        }

        /*
         * EXTRACT EMAIL
         */

        public String extractEmail(String token, String secret) {
                return getClaims(token, secret).get("email", String.class);
        }

        public String extractUserId(String token, String secret) {
                return getClaims(token, secret).getSubject();
        }

        /*
         * TOKEN EXPIRATION
         */

        public boolean isTokenExpired(
                        String token,
                        String secret) {

                return extractClaim(
                                token,
                                Claims::getExpiration,
                                secret)
                                .before(new Date());
        }

        /*
         * SIGNING KEY
         */

        private SecretKey getSigningKey(
                        String secret) {

                return Keys.hmacShaKeyFor(
                                secret.getBytes(
                                                StandardCharsets.UTF_8));
        }
}
