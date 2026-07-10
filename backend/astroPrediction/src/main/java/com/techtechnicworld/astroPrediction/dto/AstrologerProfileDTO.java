package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.techtechnicworld.enums.VerificationStatus;

public record AstrologerProfileDTO(
        Long id,
        Long userId,
        String fullName,
        String email,
        String profileImage,
        String bio,
        Integer experienceYears,
        List<String> languages,
        List<String> expertise,
        BigDecimal pricingPerMinute,
        Boolean isOnline,
        VerificationStatus verificationStatus,
        List<String> certifications,
        List<String> kycDocuments,
        String introVideoUrl,
        BigDecimal averageRating,
        Integer totalReviews,
        Integer totalConsultations,
        BigDecimal earningsTotal,
        LocalDateTime createdAt) {

}
