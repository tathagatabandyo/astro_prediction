package com.techtechnicworld.astroPrediction.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.techtechnicworld.astroPrediction.repository.RevokedTokenRepository;
import com.techtechnicworld.enums.TokenType;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;
    private final RevokedTokenRepository revokedTokenRepository;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = JwtUtil.getTokenFromRequest(request);

        if (token != null && SecurityContextHolder.getContext().getAuthentication() == null
                && jwtUtil.validateAccessToken(token)) {
            String tokenId = jwtUtil.extractTokenId(token, TokenType.ACCESS);
            String email = jwtUtil.extractEmail(token, TokenType.ACCESS);
            if (!revokedTokenRepository.existsByTokenId(tokenId) && email != null) {
                // String userId = jwtUtil.extractUserId(token, TokenType.ACCESS);
                UserDetails userDetails = userDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        filterChain.doFilter(request, response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/auth/")
                || path.equals("/api/payments/stripe/webhook")
                || path.startsWith("/api/horoscope/public/")
                || path.startsWith("/api/files/public/")
                || path.startsWith("/api/ws/");
    }
}
