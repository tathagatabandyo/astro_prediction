package com.techtechnicworld.astroPrediction.service.attachment.storage;

import org.springframework.boot.context.properties.ConfigurationProperties;

import com.techtechnicworld.enums.StorageProvider;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ConfigurationProperties(prefix = "attachment.storage")
public class StorageProperties {
    /**
     * Storage Provider
     * LOCAL, MINIO, S3
     */
    private StorageProvider provider = StorageProvider.LOCAL;

    /**
     * Local storage directory
     */
    private String localPath = "uploads";

    /**
     * Local base URL
     */
    private String localBaseUrl = "/files";

    /**
     * Bucket name
     */
    private String bucketName;

    /**
     * Presigned URL expiry (seconds)
     */
    private Integer presignedUrlExpiry = 604800;

    /**
     * MinIO Configuration
     */
    private Minio minio = new Minio();

    /**
     * S3 Configuration
     */
    private S3 s3 = new S3();

    @Getter
    @Setter
    public static class Minio {

        /**
         * MinIO Server URL
         */
        private String url;

        /**
         * MinIO Access Key
         */
        private String accessKey;

        /**
         * MinIO Secret Key
         */
        private String secretKey;
    }

    @Getter
    @Setter
    public static class S3 {

        /**
         * S3 Bucket name
         */
        private String bucketName;

        /**
         * AWS Region
         */
        private String region;

        /**
         * AWS Access Key
         */
        private String accessKey;

        /**
         * AWS Secret Key
         */
        private String secretKey;

        /**
         * Optional S3-compatible endpoint (e.g. for MinIO in S3 mode).
         * Leave empty to use the default AWS endpoint.
         */
        private String endpoint;
    }
}
