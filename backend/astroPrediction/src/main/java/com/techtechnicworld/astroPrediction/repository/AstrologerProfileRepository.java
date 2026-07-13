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

    @Query(value = """
            SELECT a.*
            FROM astrologer_profiles a
            JOIN users u ON u.id = a.user_id
            WHERE a.deleted_at IS NULL
              AND a.verification_status = 'APPROVED'
              AND (:search IS NULL OR u.full_name ILIKE CONCAT('%', :search, '%'))
              AND (:languages IS NULL OR CAST(a.languages AS text) ILIKE CONCAT('%', :languages, '%'))
              AND (:expertise IS NULL OR CAST(a.expertise AS text) ILIKE CONCAT('%', :expertise, '%'))
              AND (:minPrice IS NULL OR a.pricing_per_minute >= :minPrice)
              AND (:maxPrice IS NULL OR a.pricing_per_minute <= :maxPrice)
              AND (:minRating IS NULL OR a.average_rating >= :minRating)
            """, countQuery = """
            SELECT COUNT(*)
            FROM astrologer_profiles a
            JOIN users u ON u.id = a.user_id
            WHERE a.deleted_at IS NULL
              AND a.verification_status = 'APPROVED'
              AND (:search IS NULL OR u.full_name ILIKE CONCAT('%', :search, '%'))
              AND (:languages IS NULL OR CAST(a.languages AS text) ILIKE CONCAT('%', :languages, '%'))
              AND (:expertise IS NULL OR CAST(a.expertise AS text) ILIKE CONCAT('%', :expertise, '%'))
              AND (:minPrice IS NULL OR a.pricing_per_minute >= :minPrice)
              AND (:maxPrice IS NULL OR a.pricing_per_minute <= :maxPrice)
              AND (:minRating IS NULL OR a.average_rating >= :minRating)
            """, nativeQuery = true)
    Page<AstrologerProfileEntity> searchAstrologers(String search, String languages, String expertise,
            BigDecimal minPrice, BigDecimal maxPrice, Integer minRating, Pageable pageable);
}
