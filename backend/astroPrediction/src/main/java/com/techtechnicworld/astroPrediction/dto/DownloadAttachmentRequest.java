package com.techtechnicworld.astroPrediction.dto;

import jakarta.validation.constraints.AssertTrue;

public record DownloadAttachmentRequest(
        Long id,
        String attachmentName,
        String contentId) {
    @AssertTrue(message = "At least one of id, attachmentName, or contentId must be provided.")
    public boolean isValidRequest() {
        return id != null
                || hasText(attachmentName)
                || hasText(contentId);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
