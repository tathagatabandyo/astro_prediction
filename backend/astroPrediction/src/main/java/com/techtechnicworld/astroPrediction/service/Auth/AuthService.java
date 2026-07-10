package com.techtechnicworld.astroPrediction.service.auth;

import com.techtechnicworld.astroPrediction.repository.RevokedTokenRepository;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AuthResponse;
import com.techtechnicworld.astroPrediction.dto.LoginRequest;
import com.techtechnicworld.astroPrediction.dto.PasswordUpdateRequest;
import com.techtechnicworld.astroPrediction.dto.RegisterRequest;
import com.techtechnicworld.astroPrediction.entity.LoginAudit;
import com.techtechnicworld.astroPrediction.entity.RevokedToken;
import com.techtechnicworld.astroPrediction.entity.Role;
import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.astroPrediction.entity.UserRole;
import com.techtechnicworld.astroPrediction.entity.UserSession;
import com.techtechnicworld.astroPrediction.entity.Wallet;
import com.techtechnicworld.astroPrediction.exception.BadRequestException;
import com.techtechnicworld.astroPrediction.exception.ResourceNotFoundException;
import com.techtechnicworld.astroPrediction.exception.UnauthorizedException;
import com.techtechnicworld.astroPrediction.repository.LoginAuditRepository;
import com.techtechnicworld.astroPrediction.repository.RoleRepository;
import com.techtechnicworld.astroPrediction.repository.UserRepository;
import com.techtechnicworld.astroPrediction.repository.UserRoleRepository;
import com.techtechnicworld.astroPrediction.repository.UserSessionRepository;
import com.techtechnicworld.astroPrediction.repository.WalletRepository;
import com.techtechnicworld.astroPrediction.security.JwtUtil;
import com.techtechnicworld.astroPrediction.service.email.EmailService;
import com.techtechnicworld.astroPrediction.util.DeviceInfoUtil;
import com.techtechnicworld.enums.AuditEventType;
import com.techtechnicworld.enums.DeviceType;
import com.techtechnicworld.enums.RoleName;
import com.techtechnicworld.enums.TokenType;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final RevokedTokenRepository revokedTokenRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final WalletRepository walletRepository;
    private final JwtUtil jwtUtil;
    private final EmailService emailService;
    private final UserSessionRepository userSessionRepository;
    private final DeviceInfoUtil deviceInfoUtil;
    private final LoginAuditRepository loginAuditRepository;

    @Override
    @Transactional
    public ApiResponse<Void> register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException("Email already registered");
        }

        if (!request.password().equals(request.confirmPassword())) {
            throw new BadRequestException("Passwords do not match");
        }

        User userEntity = User.builder()
                .fullName(request.fullName())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .isActive(true)
                .emailVerified(false)
                .build();
        userEntity = userRepository.save(userEntity);

        Role role = roleRepository.findByName(RoleName.ROLE_USER)
                .orElseThrow(() -> new ResourceNotFoundException("Default role not found"));

        UserRole userRole = UserRole.builder().user(userEntity).role(role).build();
        userRoleRepository.save(userRole);

        Wallet wallet = Wallet.builder()
                .user(userEntity)
                .build();
        walletRepository.save(wallet);

        String token = jwtUtil.generateVerificationToken(userEntity);
        emailService.sendVerificationEmail(userEntity.getEmail(),
                userEntity.getFullName(), token);

        return ApiResponse.success("Registration successful. Please check your email to verify your account.", null);
    }

    @Override
    @Transactional
    public ApiResponse<Void> verifyEmail(String token) {
        if (!jwtUtil.validateVerificationToken(token)) {
            throw new BadRequestException("Invalid or expired verification token");
        }

        String email = jwtUtil.extractEmailFromVerificationToken(token);

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (Boolean.TRUE.equals(userEntity.getEmailVerified())) {
            throw new BadRequestException("Email is already verified");
        }

        userEntity.setEmailVerified(true);
        userEntity.setIsActive(true);
        userEntity.setEmailVerifiedAt(LocalDateTime.now());
        userRepository.save(userEntity);

        return ApiResponse.success("Email verified successfully", null);
    }

    private static final String REFRESH_TOKEN_COOKIE = "refresh_token";

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        User userEntity = userRepository.findByEmail(request.email())
                .orElseThrow(() -> {
                    this.logLoginAudit(null, request.email(), AuditEventType.LOGIN_FAILURE, httpServletRequest);
                    return new BadRequestException("User not found");
                });

        if (!Boolean.TRUE.equals(userEntity.getIsActive())) {
            throw new UnauthorizedException("Account is deactivated");
        }

        if (!Boolean.TRUE.equals(userEntity.getEmailVerified())) {
            throw new UnauthorizedException("Email is not verified");
        }

        if (!passwordEncoder.matches(request.password(), userEntity.getPassword())) {
            this.logLoginAudit(userEntity, request.email(), AuditEventType.LOGIN_FAILURE, httpServletRequest);
            throw new UnauthorizedException("Invalid credentials");
        }

        String sessionId = UUID.randomUUID().toString();
        String accessToken = jwtUtil.generateAccessToken(userEntity, sessionId);
        String refreshToken = jwtUtil.generateRefreshToken(userEntity, sessionId);

        DeviceType deviceType = request.deviceType() != null ? request.deviceType() : DeviceType.WEB_BROWSER;

        this.createUserSession(userEntity, sessionId, deviceType, request.deviceId(), httpServletRequest);
        this.logLoginAudit(userEntity, request.email(), AuditEventType.LOGIN_SUCCESS, httpServletRequest);
        setRefreshCookie(httpServletResponse, refreshToken);

        return ApiResponse.success("Login successful", new AuthResponse(
                accessToken,
                jwtUtil.getAccessExpiration(),
                sessionId,
                "Bearer"));
    }

    private void createUserSession(User user, String sessionId, DeviceType deviceType, String deviceId,
            HttpServletRequest httpServletRequest) {
        String userAgent = httpServletRequest.getHeader("User-Agent");

        UserSession userSessionEntity = UserSession.builder()
                .user(user)
                .sessionId(sessionId)
                .deviceId(deviceId)
                .deviceType(deviceType)
                .browser(deviceInfoUtil.extractBrowser(userAgent))
                .operatingSystem(deviceInfoUtil.extractOS(userAgent))
                .userAgent(userAgent)
                .ipAddress(deviceInfoUtil.getClientIp(httpServletRequest))
                .loginMethod("PASSWORD")
                .lastActivityAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusDays(30))
                .build();
        userSessionRepository.save(userSessionEntity);
    }

    private void logLoginAudit(User loginUser, String loginEmail, AuditEventType eventType,
            HttpServletRequest httpServletRequest) {
        String userAgent = httpServletRequest.getHeader("User-Agent");
        LoginAudit audit = LoginAudit.builder()
                .user(loginUser)
                .eventType(eventType)
                .deviceType(deviceInfoUtil.extractBrowser(userAgent))
                .browser(deviceInfoUtil.extractBrowser(userAgent))
                .os(deviceInfoUtil.extractOS(userAgent))
                .ipAddress(deviceInfoUtil.getClientIp(httpServletRequest))
                .details(eventType == AuditEventType.LOGIN_FAILURE ? "Failed login for: " + loginEmail : null)
                .build();
        loginAuditRepository.save(audit);
    }

    @Override
    @Transactional
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response) {
        String refreshToken = readRefreshCookie(request);
        if (refreshToken == null || !jwtUtil.validateRefreshToken(refreshToken)) {
            throw new UnauthorizedException("Invalid or expired refresh token");
        }

        String sessionId = jwtUtil.extractSessionIdFromRefreshToken(refreshToken);

        UserSession session = userSessionRepository.findBySessionId(sessionId)
                .orElseThrow(() -> new UnauthorizedException("Session not found"));

        if (Boolean.TRUE.equals(session.getRevoked())
                || session.getExpiresAt().isBefore(LocalDateTime.now(ZoneOffset.UTC))) {
            throw new UnauthorizedException("Session expired or revoked");
        }

        User user = session.getUser();
        String newAccessToken = jwtUtil.generateAccessToken(user, sessionId);

        session.setLastActivityAt(LocalDateTime.now(ZoneOffset.UTC));
        userSessionRepository.save(session);

        return ApiResponse.success("Token refreshed", new AuthResponse(
                newAccessToken,
                jwtUtil.getAccessExpiration(),
                sessionId,
                "Bearer"));
    }

    private void setRefreshCookie(HttpServletResponse response, String refreshToken) {
        ResponseCookie cookie = ResponseCookie.from(REFRESH_TOKEN_COOKIE, refreshToken)
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .path("/api/auth")
                .maxAge(jwtUtil.getRefreshExpiration() / 1000)
                .build();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    private String readRefreshCookie(HttpServletRequest request) {
        if (request.getCookies() == null)
            return null;
        for (Cookie cookie : request.getCookies()) {
            if (REFRESH_TOKEN_COOKIE.equals(cookie.getName()))
                return cookie.getValue();
        }
        return null;
    }

    @Override
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        String token = JwtUtil.getTokenFromRequest(request);
        if (token != null) {
            String tokenId = jwtUtil.extractTokenId(token, TokenType.ACCESS);
            String sessionId = jwtUtil.extractSessionId(token, TokenType.ACCESS);
            RevokedToken revokedToken = RevokedToken.builder()
                    .tokenId(tokenId)
                    .sessionId(sessionId)
                    .expiresAt(LocalDateTime.now(ZoneOffset.UTC))
                    .build();
            revokedTokenRepository.save(revokedToken);
        }

        Cookie cookie = new Cookie(REFRESH_TOKEN_COOKIE, null);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/api/auth");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ApiResponse.success("Logged out successfully", null);
    }

    @Override
    public ApiResponse<Void> resendVerification(String email) {
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (userEntity.getEmailVerified().booleanValue()) {
            throw new BadRequestException("Email already verified");
        }

        String token = jwtUtil.generateVerificationToken(userEntity);
        emailService.sendVerificationEmail(userEntity.getEmail(), userEntity.getFullName(), token);

        return ApiResponse.success("Verification email sent", null);
    }

    @Override
    public ApiResponse<Void> forgotPassword(String email) {
        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        String token = jwtUtil.generateForgotPasswordToken(userEntity);
        emailService.sendPasswordResetEmail(userEntity.getEmail(), userEntity.getFullName(), token);

        return ApiResponse.success("If an account exists with this email, a password reset link will be sent.", null);
    }

    @Override
    public ApiResponse<Void> resetPassword(PasswordUpdateRequest request) {
        if (!jwtUtil.validateToken(request.token(), TokenType.FORGOT_PASSWORD)) {
            throw new BadRequestException("Invalid or expired token");
        }

        String email = jwtUtil.extractEmail(request.token(), TokenType.FORGOT_PASSWORD);

        User userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userEntity.setPassword(passwordEncoder.encode(request.newPassword()));
        userRepository.save(userEntity);

        return ApiResponse.success("Password updated successfully", null);
    }
}
