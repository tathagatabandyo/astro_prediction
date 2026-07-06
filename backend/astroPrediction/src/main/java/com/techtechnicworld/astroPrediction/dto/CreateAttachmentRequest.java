package com.techtechnicworld.astroPrediction.dto;

import com.techtechnicworld.enums.AttachmentAccessType;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreateAttachmentRequest(
        @Size(max = 255, message = "Batch number must not exceed 255 characters.") @Pattern(regexp = "^[A-Za-z0-9_-]*$", message = "Batch number can contain only letters, numbers, hyphens (-), and underscores (_).") String batchNumber,

        AttachmentAccessType accessType,

        Boolean embedded) {

}
