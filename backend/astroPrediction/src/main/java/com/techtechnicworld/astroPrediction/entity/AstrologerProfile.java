package com.techtechnicworld.astroPrediction.entity;

import java.math.BigDecimal;

import com.techtechnicworld.enums.VerificationStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "astrologer_profiles")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AstrologerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(name = "experience_years")
    private Integer experienceYears;

    @Column(columnDefinition = "JSON")
    private String languages;

    @Column(columnDefinition = "JSON")
    private String expertise;

    @Column(name = "pricing_per_minute", precision = 10, scale = 2)
    @Builder.Default
    private BigDecimal pricingPerMinute = BigDecimal.ZERO;

    @Column(name = "is_online", nullable = false)
    @Builder.Default
    private Boolean isOnline = false;

    @Column(name = "is_verified", nullable = false)
    @Builder.Default
    private Boolean isVerified = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "verification_status", length = 20)
    @Builder.Default
    private VerificationStatus verificationStatus = VerificationStatus.PENDING;

    @Column(columnDefinition = "JSON")
    private String certifications;

    @Column(name = "kyc_documents", columnDefinition = "JSON")
    private String kycDocuments;

    @Column(name = "intro_video_url", length = 500)
    private String introVideoUrl;

    @Column(name = "average_rating", precision = 2, scale = 1)
    @Builder.Default
    private BigDecimal averageRating = BigDecimal.ZERO;

    @Column(name = "total_reviews")
    @Builder.Default
    private Integer totalReviews = 0;

    @Column(name = "total_consultations")
    @Builder.Default
    private Integer totalConsultations = 0;

    @Column(name = "earnings_total", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal earningsTotal = BigDecimal.ZERO;
}
