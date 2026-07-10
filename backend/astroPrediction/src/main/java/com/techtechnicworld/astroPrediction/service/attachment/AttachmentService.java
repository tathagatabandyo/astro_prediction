package com.techtechnicworld.astroPrediction.service.attachment;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.AttachmentResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;
import com.techtechnicworld.astroPrediction.entity.AttachmentEntity;
import com.techtechnicworld.enums.AttachmentAccessType;
import com.techtechnicworld.enums.AttachmentCategory;
import com.techtechnicworld.enums.AttachmentType;
import com.techtechnicworld.astroPrediction.exception.ApplicationException;
import com.techtechnicworld.astroPrediction.exception.ResourceNotFoundException;
import com.techtechnicworld.astroPrediction.repository.AttachmentRepository;
import com.techtechnicworld.astroPrediction.service.attachment.storage.IFileStorageService;
import com.techtechnicworld.astroPrediction.service.attachment.storage.StorageProperties;
import com.techtechnicworld.enums.StorageProvider;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AttachmentService implements IAttachmentService {

    private final AttachmentRepository attachmentRepository;
    private final List<IFileStorageService> fileStorageServices;
    private final StorageProperties storageProperties;

    @Value("${domain:http://localhost:8085}")
    private String domain;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Override
    @Transactional
    public ApiResponse<List<AttachmentResponse>> createAttachments(CreateAttachmentRequest createAttachmentRequest,
            List<MultipartFile> files) {

        if (files == null || files.isEmpty()) {
            throw new ApplicationException(HttpStatus.BAD_REQUEST, "NO_FILES",
                    "At least one file must be provided.");
        }

        String batchNumber = Optional.ofNullable(createAttachmentRequest.batchNumber())
                .filter(b -> !b.isBlank())
                .orElse(UUID.randomUUID().toString());

        boolean embedded = Optional.ofNullable(createAttachmentRequest.embedded()).orElse(false);

        AttachmentCategory category = Optional.ofNullable(createAttachmentRequest.category())
                .orElse(AttachmentCategory.ATTACHMENT);

        String contentId = Optional.ofNullable(createAttachmentRequest.contentId())
                .filter(c -> !c.isBlank())
                .orElse(null);

        List<AttachmentEntity> attachmentEntities = new ArrayList<>();
        StorageProvider provider = storageProperties.getProvider();
        IFileStorageService fileStorageService = storageServiceFor(provider);

        try {
            for (MultipartFile file : files) {
                FileStorageResultDto storage = fileStorageService.upload(file, "attachments");

                AttachmentEntity attachmentEntity = AttachmentEntity.builder()
                        .originalFilename(file.getOriginalFilename())
                        .storedFilename(storage.getStoredFilename())
                        .path(storage.getObjectName())
                        .url(storage.getUrl())
                        .mimeType(storage.getMimeType())
                        .fileSize(storage.getSize())
                        .attachmentType(getAttachmentType(file))
                        .contentId(contentId)
                        .category(category)
                        .batchNumber(batchNumber)
                        .accessType(createAttachmentRequest.accessType())
                        .embedded(embedded)
                        .storageProvider(provider)
                        .build();

                attachmentEntities.add(attachmentEntity);
            }
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_CREATE_ATTACHMENT",
                    e.getMessage());
        }

        List<AttachmentEntity> saved = attachmentRepository.saveAll(attachmentEntities);

        for (AttachmentEntity attachmentEntity : saved) {
            attachmentEntity.setUrl(getAttachmentUrl(attachmentEntity));
        }

        saved = attachmentRepository.saveAll(saved);

        return ApiResponse.success("Attachments created successfully.", AttachmentResponse.from(saved));
    }

    private String getAttachmentUrl(AttachmentEntity attachment) {
        String basePath = AttachmentAccessType.PUBLIC.equals(attachment.getAccessType())
                ? "/public/attachment/download/"
                : "/attachment/download/";

        String base = domain.endsWith("/") ? domain.substring(0, domain.length() - 1) : domain;
        String ctx = (contextPath == null || contextPath.isBlank()) ? "" : contextPath;
        ctx = ctx.startsWith("/") ? ctx : "/" + ctx;

        return base + ctx + basePath + attachment.getId();
    }

    @Override
    public Resource downloadAttachment(AttachmentRequest downloadAttachmentRequest) {
        AttachmentEntity attachment = resolveAttachment(downloadAttachmentRequest);
        IFileStorageService fileStorageService = storageServiceFor(attachment);

        try {
            InputStream stream = fileStorageService.download(attachment.getPath());
            return new InputStreamResource(stream);
        } catch (Exception e) {
            throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_DOWNLOAD_ATTACHMENT",
                    e.getMessage());
        }
    }

    @Override
    public ApiResponse<?> getAttachment(AttachmentRequest getAttachmentRequest) {
        if (getAttachmentRequest.ids() != null && !getAttachmentRequest.ids().isEmpty()) {
            return ApiResponse.success(AttachmentResponse.from(getAllByIds(getAttachmentRequest.ids())));
        }

        return ApiResponse.success(getAttachmentMetadata(getAttachmentRequest));
    }

    @Override
    public AttachmentResponse getAttachmentMetadata(AttachmentRequest getAttachmentRequest) {
        return AttachmentResponse.from(resolveAttachment(getAttachmentRequest));
    }

    @Override
    @Transactional
    public ApiResponse<?> deleteAttachment(AttachmentRequest deleteAttachmentRequest) {
        List<AttachmentEntity> attachments;
        if (deleteAttachmentRequest.ids() != null && !deleteAttachmentRequest.ids().isEmpty()) {
            attachments = getAllByIds(deleteAttachmentRequest.ids());
        } else {
            attachments = List.of(resolveAttachment(deleteAttachmentRequest));
        }

        for (AttachmentEntity attachment : attachments) {
            try {
                IFileStorageService fileStorageService = storageServiceFor(attachment);
                fileStorageService.delete(attachment.getPath());
            } catch (Exception e) {
                throw new ApplicationException(HttpStatus.INTERNAL_SERVER_ERROR, "FAILED_DELETE_ATTACHMENT",
                        e.getMessage());
            }

            attachment.setDeletedAt(java.time.LocalDateTime.now(java.time.ZoneOffset.UTC));
        }

        attachmentRepository.saveAll(attachments);

        return ApiResponse.success("Attachments deleted successfully.", attachments);
    }

    private List<AttachmentEntity> getAllByIds(List<Long> ids) {
        List<AttachmentEntity> attachments = attachmentRepository.findAllById(ids)
                .stream()
                .filter(a -> a.getDeletedAt() == null)
                .toList();

        if (attachments.isEmpty()) {
            throw new ResourceNotFoundException("No attachments found for the provided ids.");
        }

        return attachments;
    }

    @Override
    public ApiResponse<List<AttachmentResponse>> getAttachments(Integer start, Integer limit) {
        int page = Optional.ofNullable(start).filter(s -> s > 0).orElse(0);
        int size = Optional.ofNullable(limit).filter(l -> l > 0).orElse(20);

        List<AttachmentEntity> attachments = attachmentRepository
                .findAll(PageRequest.of(page, size))
                .getContent();

        return ApiResponse.success(AttachmentResponse.from(attachments));
    }

    private AttachmentType getAttachmentType(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null) {
            return AttachmentType.OTHER;
        }

        String type = contentType.toLowerCase();
        if (type.startsWith("image/")) {
            return AttachmentType.IMAGE;
        }
        if (type.startsWith("audio/")) {
            return AttachmentType.AUDIO;
        }
        if (type.startsWith("video/")) {
            return AttachmentType.VIDEO;
        }
        if (type.startsWith("text/")) {
            return AttachmentType.TEXT;
        }
        if ("application/pdf".equals(type)) {
            return AttachmentType.PDF;
        }
        if (type.contains("zip") || type.contains("tar") || type.contains("compressed")
                || type.contains("archive") || type.contains("x-7z") || type.contains("x-rar")) {
            return AttachmentType.ARCHIVE;
        }
        if (type.contains("spreadsheet") || type.contains("excel") || type.contains("csv")) {
            return AttachmentType.SPREADSHEET;
        }
        if (type.contains("presentation") || type.contains("powerpoint")) {
            return AttachmentType.PRESENTATION;
        }
        if (type.contains("word") || type.contains("document") || type.contains("msword")
                || type.contains("officedocument")) {
            return AttachmentType.DOCUMENT;
        }
        return AttachmentType.OTHER;
    }

    private IFileStorageService storageServiceFor(AttachmentEntity attachment) {
        StorageProvider provider = Optional.ofNullable(attachment.getStorageProvider())
                .orElse(storageProperties.getProvider());

        return storageServiceFor(provider);
    }

    private IFileStorageService storageServiceFor(StorageProvider provider) {
        return fileStorageServices.stream()
                .filter(service -> service.getProvider() == provider)
                .findFirst()
                .orElseThrow(() -> new ApplicationException(
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "STORAGE_PROVIDER_NOT_AVAILABLE",
                        "Storage provider " + provider + " is not enabled in this application."));
    }

    private AttachmentEntity resolveAttachment(AttachmentRequest request) {
        if (request.id() != null) {
            return attachmentRepository.findByIdAndDeletedAtIsNull(request.id())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Attachment not found with id: " + request.id()));
        }

        if (request.ids() != null && !request.ids().isEmpty()) {
            Long id = request.ids().get(0);
            return attachmentRepository.findByIdAndDeletedAtIsNull(id)
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Attachment not found with id: " + id));
        }

        if (request.contentId() != null && !request.contentId().isBlank()) {
            List<AttachmentEntity> attachments = attachmentRepository
                    .findAllByContentIdAndDeletedAtIsNull(request.contentId());
            if (attachments.isEmpty()) {
                throw new ResourceNotFoundException(
                        "Attachment not found with content id: " + request.contentId());
            }
            return attachments.get(0);
        }

        if (request.attachmentName() != null && !request.attachmentName().isBlank()) {
            return attachmentRepository.findByStoredFilenameAndDeletedAtIsNull(request.attachmentName())
                    .orElseThrow(() -> new ResourceNotFoundException(
                            "Attachment not found with attachment name: " + request.attachmentName()));
        }

        throw new ResourceNotFoundException("No valid attachment identifier provided.");
    }
}
