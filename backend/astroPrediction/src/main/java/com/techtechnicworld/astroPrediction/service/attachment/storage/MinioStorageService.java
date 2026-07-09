package com.techtechnicworld.astroPrediction.service.attachment.storage;

import java.io.InputStream;
import java.util.concurrent.TimeUnit;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;
import com.techtechnicworld.enums.StorageProvider;

import io.minio.BucketExistsArgs;
import io.minio.GetObjectArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.StatObjectArgs;
import io.minio.Http;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@ConditionalOnProperty(prefix = "attachment.storage.minio", name = "url")
@RequiredArgsConstructor
public class MinioStorageService implements IFileStorageService {

    private final MinioClient minioClient;

    private final StorageProperties storageProperties;

    private String bucketName;

    @Override
    public StorageProvider getProvider() {
        return StorageProvider.MINIO;
    }

    @PostConstruct
    public void initialize() {
        bucketName = storageProperties.getBucketName();
    }

    @Override
    public FileStorageResultDto upload(MultipartFile file, String folder) throws Exception {
        ensureBucketExists();

        String objectName = folder + "/" + java.util.UUID.randomUUID() + "_"
                + org.springframework.util.StringUtils.cleanPath(file.getOriginalFilename());

        minioClient.putObject(
                PutObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .stream(file.getInputStream(), file.getSize(), -1L)
                        .contentType(file.getContentType())
                        .build());

        String url = storageProperties.getMinio().getUrl()
                + "/"
                + bucketName
                + "/"
                + objectName;

        return FileStorageResultDto.builder()
                .objectName(objectName)
                .storedFilename(objectName.substring(objectName.lastIndexOf('/') + 1))
                .url(url)
                .mimeType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        return minioClient.getObject(
                GetObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    @Override
    public void delete(String objectName) throws Exception {
        minioClient.removeObject(
                RemoveObjectArgs.builder()
                        .bucket(bucketName)
                        .object(objectName)
                        .build());
    }

    @Override
    public String generateUrl(String objectName) throws Exception {
        return minioClient.getPresignedObjectUrl(
                GetPresignedObjectUrlArgs.builder()
                        .method(Http.Method.GET)
                        .bucket(bucketName)
                        .object(objectName)
                        .expiry(storageProperties.getPresignedUrlExpiry(), TimeUnit.SECONDS)
                        .build());
    }

    @Override
    public boolean exists(String objectName) throws Exception {
        try {
            minioClient.statObject(
                    StatObjectArgs.builder()
                            .bucket(bucketName)
                            .object(objectName)
                            .build());
            return true;
        } catch (io.minio.errors.ErrorResponseException e) {
            return false;
        }
    }

    private void ensureBucketExists() throws Exception {
        boolean exists = minioClient.bucketExists(
                BucketExistsArgs.builder()
                        .bucket(bucketName)
                        .build());

        if (!exists) {
            minioClient.makeBucket(
                    MakeBucketArgs.builder()
                            .bucket(bucketName)
                            .build());
        }
    }
}
