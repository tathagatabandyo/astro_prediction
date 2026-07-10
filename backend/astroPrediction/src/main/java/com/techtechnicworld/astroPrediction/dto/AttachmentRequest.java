package com.techtechnicworld.astroPrediction.dto;

import java.util.List;

import jakarta.validation.constraints.AssertTrue;

public record AttachmentRequest(
        Long id,
        String attachmentName,
        String contentId,
        List<Long> ids) {

    @AssertTrue(message = "At least one of id, attachmentName, contentId, or ids must be provided.")
    public boolean isValidRequest() {
        return id != null
                || hasText(attachmentName)
                || hasText(contentId)
                || hasElements(ids);
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }

    private static boolean hasElements(List<Long> list) {
        return list != null
                && !list.isEmpty()
                && list.stream().anyMatch(java.util.Objects::nonNull);
    }
}
