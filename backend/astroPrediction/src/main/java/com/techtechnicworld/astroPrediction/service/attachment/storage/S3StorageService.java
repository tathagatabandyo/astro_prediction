package com.techtechnicworld.astroPrediction.service.attachment.storage;

import java.io.InputStream;
import java.util.UUID;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;
import com.techtechnicworld.enums.StorageProvider;

import lombok.RequiredArgsConstructor;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.HeadObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.model.S3Exception;

@Service
@ConditionalOnProperty(prefix = "attachment.storage.s3", name = "region")
@RequiredArgsConstructor
public class S3StorageService implements IFileStorageService {

    private final S3Client s3Client;

    private final StorageProperties storageProperties;

    @Override
    public StorageProvider getProvider() {
        return StorageProvider.S3;
    }

    private String bucketName() {
        return storageProperties.getS3().getBucketName();
    }

    @Override
    public FileStorageResultDto upload(MultipartFile file, String folder) throws Exception {

        String objectName = folder + "/" + UUID.randomUUID() + "_"
                + org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(bucketName())
                .key(objectName)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(request,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return FileStorageResultDto.builder()
                .objectName(objectName)
                .storedFilename(objectName.substring(objectName.lastIndexOf('/') + 1))
                .url(generateUrl(objectName))
                .mimeType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        return s3Client.getObject(
                GetObjectRequest.builder()
                        .bucket(bucketName())
                        .key(objectName)
                        .build());
    }

    @Override
    public void delete(String objectName) throws Exception {
        s3Client.deleteObject(
                DeleteObjectRequest.builder()
                        .bucket(bucketName())
                        .key(objectName)
                        .build());
    }

    @Override
    public String generateUrl(String objectName) throws Exception {
        String endpoint = storageProperties.getS3().getEndpoint();
        String base;
        if (endpoint != null && !endpoint.isBlank()) {
            base = endpoint.replaceAll("/$", "") + "/" + bucketName();
        } else {
            base = "https://s3." + storageProperties.getS3().getRegion()
                    + ".amazonaws.com/" + bucketName();
        }
        return base + "/" + objectName;
    }

    @Override
    public boolean exists(String objectName) throws Exception {
        try {
            s3Client.headObject(
                    HeadObjectRequest.builder()
                            .bucket(bucketName())
                            .key(objectName)
                            .build());
            return true;
        } catch (S3Exception e) {
            return false;
        }
    }
}
