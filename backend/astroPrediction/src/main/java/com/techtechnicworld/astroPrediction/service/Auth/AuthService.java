package com.techtechnicworld.astroPrediction.service.Auth;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AuthResponse;
import com.techtechnicworld.astroPrediction.dto.LoginRequest;
import com.techtechnicworld.astroPrediction.dto.PasswordUpdateRequest;
import com.techtechnicworld.astroPrediction.dto.RegisterRequest;
import com.techtechnicworld.astroPrediction.entity.Role;
import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.astroPrediction.entity.UserRole;
import com.techtechnicworld.astroPrediction.entity.Wallet;
import com.techtechnicworld.astroPrediction.exception.BadRequestException;
import com.techtechnicworld.astroPrediction.exception.ResourceNotFoundException;
import com.techtechnicworld.astroPrediction.repository.RoleRepository;
import com.techtechnicworld.astroPrediction.repository.UserRepository;
import com.techtechnicworld.astroPrediction.repository.UserRoleRepository;
import com.techtechnicworld.astroPrediction.repository.WalletRepository;
import com.techtechnicworld.astroPrediction.security.JwtUtil;
import com.techtechnicworld.enums.RoleName;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService implements IAuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserRoleRepository userRoleRepository;
    private final WalletRepository walletRepository;
    private final JwtUtil jwtUtil;

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
        // emailService.sendVerificationEmail(userEntity.getEmail(),
        // userEntity.getFullName(), token);

        return ApiResponse.success("Registration successful. Please check your email to verify your account.", null);
    }

    @Override
    public ApiResponse<AuthResponse> login(LoginRequest request, HttpServletRequest servletRequest) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'login'");
    }

    @Override
    public ApiResponse<AuthResponse> refreshToken(HttpServletRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'refreshToken'");
    }

    @Override
    public ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'logout'");
    }

    @Override
    public ApiResponse<Void> verifyEmail(String token) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'verifyEmail'");
    }

    @Override
    public ApiResponse<Void> resendVerification(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resendVerification'");
    }

    @Override
    public ApiResponse<Void> forgotPassword(String email) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'forgotPassword'");
    }

    @Override
    public ApiResponse<Void> resetPassword(PasswordUpdateRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'resetPassword'");
    }
}
