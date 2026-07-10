package com.techtechnicworld.astroPrediction.controller;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.AttachmentResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.service.attachment.IAttachmentService;
import com.techtechnicworld.enums.AttachmentAccessType;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class AttachmentController {

    private final IAttachmentService attachmentService;

    @PostMapping(value = "/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createAttachments(
            @RequestPart("request") CreateAttachmentRequest request,
            @RequestPart("files") List<MultipartFile> files) {

        ApiResponse<?> response = attachmentService.createAttachments(request, files);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PostMapping(value = "/public/attachment", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiResponse<?>> createPublicAttachments(@RequestPart("files") List<MultipartFile> files) {

        ApiResponse<?> response = attachmentService.createAttachments(
                new CreateAttachmentRequest(null, null, null, AttachmentAccessType.PUBLIC, null), files);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/public/attachment/download/{identifier}")
    public ResponseEntity<Resource> downloadPublicAttachment(@PathVariable String identifier) {
        return buildDownloadResponse(identifier);
    }

    @GetMapping("/attachment/download/{identifier}")
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String identifier) {
        return buildDownloadResponse(identifier);
    }

    @GetMapping("/attachment/{id}")
    public ResponseEntity<ApiResponse<?>> getAttachment(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.getAttachment(new AttachmentRequest(id, null, null, null)));
    }

    @DeleteMapping("/attachment/{id}")
    public ResponseEntity<ApiResponse<?>> deleteAttachment(@PathVariable Long id) {
        return ResponseEntity.ok(attachmentService.deleteAttachment(new AttachmentRequest(id, null, null, null)));
    }

    @GetMapping("/attachment")
    public ResponseEntity<ApiResponse<?>> getAttachments(
            @RequestParam(required = false) Integer start,
            @RequestParam(required = false) Integer limit) {

        return ResponseEntity.ok(attachmentService.getAttachments(start, limit));
    }

    private ResponseEntity<Resource> buildDownloadResponse(String identifier) {
        AttachmentRequest request = resolveRequest(identifier);

        AttachmentResponse metadata = attachmentService.getAttachmentMetadata(request);

        Resource resource = attachmentService.downloadAttachment(request);

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(metadata.mimeType()))
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(metadata.fileSize()))
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "inline; filename=\"" + metadata.originalFilename() + "\"")
                .body(resource);
    }

    private AttachmentRequest resolveRequest(String identifier) {
        try {
            Long id = Long.parseLong(identifier);
            return new AttachmentRequest(id, null, null, null);
        } catch (NumberFormatException e) {
            return new AttachmentRequest(null, identifier, null, null);
        }
    }
}
