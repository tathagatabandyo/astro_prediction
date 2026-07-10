package com.techtechnicworld.astroPrediction.service.auth;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AuthResponse;
import com.techtechnicworld.astroPrediction.dto.LoginRequest;
import com.techtechnicworld.astroPrediction.dto.PasswordUpdateRequest;
import com.techtechnicworld.astroPrediction.dto.RegisterRequest;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface IAuthService {
    ApiResponse<AuthResponse> login(LoginRequest request, HttpServletRequest servletRequest, HttpServletResponse response);
    ApiResponse<Void> register(RegisterRequest request);
    ApiResponse<AuthResponse> refreshToken(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<Void> logout(HttpServletRequest request, HttpServletResponse response);
    ApiResponse<Void> verifyEmail(String token);
    ApiResponse<Void> resendVerification(String email);
    ApiResponse<Void> forgotPassword(String email);
    ApiResponse<Void> resetPassword(PasswordUpdateRequest request);
}
