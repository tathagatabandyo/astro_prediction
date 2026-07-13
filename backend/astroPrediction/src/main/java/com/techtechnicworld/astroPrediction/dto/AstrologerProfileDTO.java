package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.techtechnicworld.astroPrediction.entity.AstrologerProfileEntity;
import com.techtechnicworld.enums.VerificationStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AstrologerProfileDTO {

        private Long id;

        private Long userId;

        private String fullName;

        private String email;

        private String profileImage;

        private String bio;

        private Integer experienceYears;

        private List<String> languages;

        private List<String> expertise;

        private BigDecimal pricingPerMinute;

        private Boolean isOnline;

        private VerificationStatus verificationStatus;

        private List<String> certifications;

        private List<String> kycDocuments;

        private String introVideoUrl;

        private BigDecimal averageRating;

        private Integer totalReviews;

        private Integer totalConsultations;

        private BigDecimal earningsTotal;

        private LocalDateTime createdAt;

        public static AstrologerProfileDTO from(AstrologerProfileEntity astrologerProfileEntity) {
                return AstrologerProfileDTO.builder()
                                .id(astrologerProfileEntity.getId())
                                .userId(astrologerProfileEntity.getUser().getId())
                                .fullName(astrologerProfileEntity.getUser().getFullName())
                                .fullName(astrologerProfileEntity.getUser().getEmail())
                                // .profileImage(astrologerProfileEntity.getUser().getProfileImage())
                                .profileImage(null)
                                .bio(astrologerProfileEntity.getBio())
                                .experienceYears(astrologerProfileEntity.getExperienceYears())
                                .languages(astrologerProfileEntity.getLanguages())
                                .expertise(astrologerProfileEntity.getExpertise())
                                .pricingPerMinute(astrologerProfileEntity.getPricingPerMinute())
                                .isOnline(astrologerProfileEntity.getIsOnline())
                                .verificationStatus(astrologerProfileEntity.getVerificationStatus())
                                .certifications(astrologerProfileEntity.getCertifications())
                                .kycDocuments(astrologerProfileEntity.getKycDocuments())
                                .averageRating(astrologerProfileEntity.getAverageRating())
                                .totalReviews(astrologerProfileEntity.getTotalReviews())
                                .totalConsultations(astrologerProfileEntity.getTotalConsultations())
                                .introVideoUrl(astrologerProfileEntity.getIntroVideoUrl())
                                .earningsTotal(astrologerProfileEntity.getEarningsTotal())
                                .createdAt(astrologerProfileEntity.getCreatedAt())
                                .build();
        }

        public static List<AstrologerProfileDTO> from(List<AstrologerProfileEntity> entities) {
                return entities.stream()
                                .map(AstrologerProfileDTO::from)
                                .toList();
        }
}