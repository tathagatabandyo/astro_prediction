package com.techtechnicworld.astroPrediction.dto;

public record AuthResponse(
    String accessToken,
    Long expiresIn,
    String sessionId,
    String tokenType
) {}
