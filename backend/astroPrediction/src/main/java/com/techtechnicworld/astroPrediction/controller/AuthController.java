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
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest loginRequest, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
       return ResponseEntity.ok(authService.login(loginRequest, httpServletRequest, httpServletResponse));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<ApiResponse<AuthResponse>> refreshToken(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.refreshToken(httpServletRequest, httpServletResponse));
    }

    @PostMapping("/register")
    public ResponseEntity<ApiResponse<Void>> register(@Valid @RequestBody RegisterRequest registerRequest) {
        return ResponseEntity.ok(authService.register(registerRequest));
    }
    
    @PostMapping("/logout")
    public ResponseEntity<ApiResponse<Void>> logout(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return ResponseEntity.ok(authService.logout(httpServletRequest, httpServletResponse));
    }

    @PostMapping("/verify-email")
    public ResponseEntity<ApiResponse<Void>> postMethodName(@RequestParam String token) {
        return ResponseEntity.ok(authService.verifyEmail(token));
    }
    
}
