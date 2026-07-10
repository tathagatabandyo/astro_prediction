package com.techtechnicworld.astroPrediction.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.techtechnicworld.astroPrediction.entity.AttachmentEntity;
import com.techtechnicworld.enums.AttachmentAccessType;
import com.techtechnicworld.enums.AttachmentCategory;
import com.techtechnicworld.enums.AttachmentType;
import com.techtechnicworld.enums.StorageProvider;

public record AttachmentResponse(
        Long id,

        String originalFilename,

        String storedFilename,

        String path,

        String url,

        String mimeType,

        AttachmentType attachmentType,

        String contentId,

        String batchNumber,

        Long fileSize,

        AttachmentAccessType accessType,

        AttachmentCategory category,

        Boolean embedded,

        StorageProvider storageProvider,

        LocalDateTime createdAt) {

    public static AttachmentResponse from(AttachmentEntity attachment) {
        return new AttachmentResponse(
                attachment.getId(),
                attachment.getOriginalFilename(),
                attachment.getStoredFilename(),
                attachment.getPath(),
                attachment.getUrl(),
                attachment.getMimeType(),
                attachment.getAttachmentType(),
                attachment.getContentId(),
                attachment.getBatchNumber(),
                attachment.getFileSize(),
                attachment.getAccessType(),
                attachment.getCategory(),
                attachment.getEmbedded(),
                attachment.getStorageProvider(),
                attachment.getCreatedAt());
    }

    public static List<AttachmentResponse> from(List<AttachmentEntity> attachments) {
        return attachments.stream()
                .map(AttachmentResponse::from)
                .toList();
    }
}
