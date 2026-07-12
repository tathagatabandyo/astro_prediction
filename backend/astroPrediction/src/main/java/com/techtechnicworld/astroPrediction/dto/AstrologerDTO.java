package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.util.List;

import com.techtechnicworld.astroPrediction.entity.AstrologerProfileEntity;

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
public class AstrologerDTO {
        private Long id;

        private String fullName;

        private String profileImage;

        private String bio;

        private Integer experienceYears;

        private List<String> languages;

        private List<String> expertise;

        private BigDecimal pricingPerMinute;

        private Boolean isOnline;

        private Boolean isVerified;

        private BigDecimal averageRating;

        private Integer totalReviews;

        private Integer totalConsultations;

        private String introVideoUrl;

        public static AstrologerDTO from(AstrologerProfileEntity astrologerProfileEntity) {
                return AstrologerDTO.builder()
                                .id(astrologerProfileEntity.getId())
                                .fullName(astrologerProfileEntity.getUser().getFullName())
                                // .profileImage(astrologerProfileEntity.getUser().getProfileImage())
                                .profileImage(null)
                                .bio(astrologerProfileEntity.getBio())
                                .experienceYears(astrologerProfileEntity.getExperienceYears())
                                .languages(astrologerProfileEntity.getLanguages())
                                .expertise(astrologerProfileEntity.getExpertise())
                                .pricingPerMinute(astrologerProfileEntity.getPricingPerMinute())
                                .isOnline(astrologerProfileEntity.getIsOnline())
                                .isVerified(astrologerProfileEntity.getIsVerified())
                                .averageRating(astrologerProfileEntity.getAverageRating())
                                .totalReviews(astrologerProfileEntity.getTotalReviews())
                                .totalConsultations(astrologerProfileEntity.getTotalConsultations())
                                .introVideoUrl(astrologerProfileEntity.getIntroVideoUrl())
                                .build();
        }

        public static List<AstrologerDTO> from(List<AstrologerProfileEntity> entities) {
                return entities.stream()
                                .map(AstrologerDTO::from)
                                .toList();
        }

}
