package com.techtechnicworld.astroPrediction.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AuthResponse;
import com.techtechnicworld.astroPrediction.dto.LoginRequest;
import com.techtechnicworld.astroPrediction.dto.PasswordUpdateRequest;
import com.techtechnicworld.astroPrediction.dto.RegisterRequest;
import com.techtechnicworld.astroPrediction.service.Auth.AuthService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.login(loginRequest, httpServletRequest, httpServletResponse));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.refreshToken(httpServletRequest, httpServletResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.logout(httpServletRequest, httpServletResponse));
    }

    @GetMapping("/verify-email")
    public ResponseEntity<Void> verifyEmail(@RequestParam String token) {
        String redirectUrl;
        try {
            authService.verifyEmail(token);
            redirectUrl = frontendUrl + "/auth/email-verified?success=true";
        } catch (Exception e) {
            redirectUrl = frontendUrl + "/auth/email-verified?success=false&message="
                    + e.getMessage().replace(" ", "+");
        }
        return ResponseEntity.status(HttpStatus.FOUND)
                .header(HttpHeaders.LOCATION, redirectUrl)
                .build();
    }

    @PostMapping("/resend-verification")
    public ResponseEntity<ApiResponse<?>> resendVerification(
            @RequestParam @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email) {

        return ResponseEntity.ok(authService.resendVerification(email));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse<?>> forgotPassword(
            @RequestParam @NotBlank(message = "Email is required") @Email(message = "Invalid email address") String email) {

        return ResponseEntity.ok(authService.forgotPassword(email));
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse<?>> resetPassword(@Valid @RequestBody PasswordUpdateRequest passwordUpdateRequest) {
        return ResponseEntity.ok(authService.resetPassword(passwordUpdateRequest));
    }
    
}
