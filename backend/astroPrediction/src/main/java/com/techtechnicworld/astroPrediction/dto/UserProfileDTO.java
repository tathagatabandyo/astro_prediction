package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.techtechnicworld.enums.Gender;

public record UserProfileDTO(
        Long id,
        String fullName,
        String email,
        String profileImage,
        String phone,
        LocalDate dateOfBirth,
        LocalTime birthTime,
        String birthPlace,
        Gender gender,
        String preferredLanguage,
        String timezone,
        List<String> roles,
        BigDecimal walletBalance,
        LocalDateTime createdAt) {

}
