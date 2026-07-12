package com.techtechnicworld.astroPrediction.repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.techtechnicworld.astroPrediction.entity.AstrologerProfileEntity;
import com.techtechnicworld.enums.VerificationStatus;

public interface AstrologerProfileRepository extends JpaRepository<AstrologerProfileEntity, Long> {
    Optional<AstrologerProfileEntity> findByUserIdAndDeletedAtIsNull(Long userId);

    List<AstrologerProfileEntity> findByIsOnlineTrueAndVerificationStatusAndDeletedAtIsNull(VerificationStatus status);

    List<AstrologerProfileEntity> findByVerificationStatusAndDeletedAtIsNull(VerificationStatus status);

    List<AstrologerProfileEntity> findTop8ByVerificationStatusAndDeletedAtIsNullOrderByAverageRatingDesc(
            VerificationStatus status);

    @Query("SELECT a FROM AstrologerProfileEntity a WHERE a.deletedAt IS NULL AND a.verificationStatus = 'APPROVED' AND "
            +
            "(:search IS NULL OR a.user.fullName LIKE %:search%) AND " +
            "(:languages IS NULL OR a.languages LIKE %:languages%) AND " +
            "(:expertise IS NULL OR a.expertise LIKE %:expertise%) AND " +
            "(:minPrice IS NULL OR a.pricingPerMinute >= :minPrice) AND " +
            "(:maxPrice IS NULL OR a.pricingPerMinute <= :maxPrice) AND " +
            "(:minRating IS NULL OR a.averageRating >= :minRating)")
    Page<AstrologerProfileEntity> searchAstrologers(String search, String languages, String expertise,
            BigDecimal minPrice, BigDecimal maxPrice, Integer minRating, Pageable pageable);
}
