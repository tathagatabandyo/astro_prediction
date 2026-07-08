package com.techtechnicworld.astroPrediction.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.techtechnicworld.astroPrediction.entity.AttachmentEntity;

import jakarta.transaction.Transactional;

public interface AttachmentRepository extends JpaRepository<AttachmentEntity, Long> {

    List<AttachmentEntity> findByBatchNumber(String batchNumber);

    Optional<AttachmentEntity> findByIdAndDeletedAtIsNull(Long id);

    Optional<AttachmentEntity> findByStoredFilenameAndDeletedAtIsNull(String storedFilename);

    List<AttachmentEntity> findAllByBatchNumberAndDeletedAtIsNull(String batchNumber);

    List<AttachmentEntity> findAllByContentIdAndDeletedAtIsNull(String contentId);

    @Modifying
    @Transactional
    @Query("""
                UPDATE AttachmentEntity a
                   SET a.deletedAt = :deletedAt,
                       a.updatedAt = :deletedAt
                 WHERE a.batchNumber = :batchNumber
                   AND a.deletedAt IS NULL
            """)
    int softDeleteByBatchNumber(
            @Param("batchNumber") String batchNumber,
            @Param("deletedAt") LocalDateTime deletedAt);
}
