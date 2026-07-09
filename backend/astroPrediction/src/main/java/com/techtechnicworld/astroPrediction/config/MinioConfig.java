package com.techtechnicworld.astroPrediction.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.techtechnicworld.astroPrediction.service.attachment.storage.StorageProperties;

import io.minio.MinioClient;
import lombok.RequiredArgsConstructor;

@Configuration
@ConditionalOnProperty(prefix = "attachment.storage.minio", name = "url")
@RequiredArgsConstructor
public class MinioConfig {

    private final StorageProperties storageProperties;

    @Bean
    public MinioClient minioClient() {
        return MinioClient.builder()
                .endpoint(storageProperties.getMinio().getUrl())
                .credentials(storageProperties.getMinio().getAccessKey(), storageProperties.getMinio().getSecretKey())
                .build();
    }
}
