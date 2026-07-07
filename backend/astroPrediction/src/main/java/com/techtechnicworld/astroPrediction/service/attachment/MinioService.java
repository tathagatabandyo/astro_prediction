package com.techtechnicworld.astroPrediction.service.attachment;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import io.minio.BucketExistsArgs;
import io.minio.GetPresignedObjectUrlArgs;
import io.minio.MakeBucketArgs;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.RemoveObjectArgs;
import io.minio.Http.Method;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MinioService {
    private final MinioClient minioClient;

    @Value("${minio.minio.bucket-name}")
    private String bucketName;

    @Value("${minio.presigned-url-expiry-seconds:604800}")
    private int presignedUrlExpirySeconds;

    @PostConstruct
    public void init() throws Exception {
        boolean exists = minioClient.bucketExists(BucketExistsArgs.builder().bucket(bucketName).build());
        if (!exists) {
            minioClient.makeBucket(MakeBucketArgs.builder().bucket(bucketName).build());
        }
    }

    // Returns the objectName (not a URL) so callers can store it permanently
    public String uploadFile(MultipartFile file, String folder) throws Exception {
        String objectName = folder + "/" + UUID.randomUUID() + "_" + file.getOriginalFilename();
        minioClient.putObject(PutObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .stream(file.getInputStream(), file.getSize(), -1L)
                .contentType(file.getContentType())
                .build());
        return objectName;
    }

    // // Generates a fresh presigned URL from a stored objectName
    // public String getPresignedUrl(String objectName) throws Exception {
    // return minioClient.getPresignedObjectUrl(
    // GetPresignedObjectUrlArgs.builder()
    // .method(Method.GET)
    // .bucket(bucketName)
    // .object(objectName)
    // .expiry(presignedUrlExpirySeconds, TimeUnit.SECONDS)
    // .build());
    // }

    public void deleteFile(String objectName) throws Exception {
        minioClient.removeObject(RemoveObjectArgs.builder()
                .bucket(bucketName)
                .object(objectName)
                .build());
    }
}
