package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.util.List;

public record AstrologerDTO(
        Long id,
        String fullName,
        String profileImage,
        String bio,
        Integer experienceYears,
        List<String> languages,
        List<String> expertise,
        BigDecimal pricingPerMinute,
        Boolean isOnline,
        Boolean isVerified,
        BigDecimal averageRating,
        Integer totalReviews,
        Integer totalConsultations,
        String introVideoUrl) {

}
