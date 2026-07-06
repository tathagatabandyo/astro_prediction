package com.techtechnicworld.astroPrediction.entity;

import com.techtechnicworld.enums.AttachmentAccessType;
import com.techtechnicworld.enums.AttachmentType;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(
    name = "attachments",
    indexes = {
        // Single-column indexes
        @Index(name = "idx_attachment_content_id", columnList = "content_id"),
        @Index(name = "idx_attachment_batch_number", columnList = "batch_number"),
        @Index(name = "idx_attachment_type", columnList = "attachment_type"),
        @Index(name = "idx_attachment_access_type", columnList = "access_type"),
        @Index(name = "idx_attachment_embedded", columnList = "embedded"),

        // Composite indexes
        @Index(
            name = "idx_attachment_content_type",
            columnList = "content_id, attachment_type"
        ),
        @Index(
            name = "idx_attachment_batch_type",
            columnList = "batch_number, attachment_type"
        ),
        @Index(
            name = "idx_attachment_content_embedded",
            columnList = "content_id, embedded"
        ),
        @Index(
            name = "idx_attachment_content_access",
            columnList = "content_id, access_type"
        )
    }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentEntity extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "original_filename", nullable = false, length = 255)
    private String originalFilename;

    @Column(name = "stored_filename", nullable = false, length = 255)
    private String storedFilename;

    @Column(name = "path", nullable = false, columnDefinition = "TEXT")
    private String path;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "mime_type", nullable = false, length = 255)
    private String mimeType;

    @Enumerated(EnumType.STRING)
    @Column(name = "attachment_type", nullable = false, length = 255)
    private AttachmentType attachmentType;

    @Column(name = "content_id", nullable = false, length = 255)
    private String contentId;

    @Column(name = "batch_number", nullable = false, length = 255)
    private String batchNumber;

    @Column(name = "file_size", nullable = false)
    private Long fileSize;

    @Enumerated(EnumType.STRING)
    @Column(name = "access_type", nullable = false, length = 20)
    private AttachmentAccessType accessType;

    @Column(name = "embedded", nullable = false)
    @Builder.Default
    private Boolean embedded = false;
}
