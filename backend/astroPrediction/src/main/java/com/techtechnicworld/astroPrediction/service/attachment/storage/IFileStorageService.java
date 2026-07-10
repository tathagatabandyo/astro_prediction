package com.techtechnicworld.astroPrediction.service.attachment.storage;

import java.io.InputStream;

import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;
import com.techtechnicworld.enums.StorageProvider;

public interface IFileStorageService {
    StorageProvider getProvider();

    /**
     * Upload file.
     *
     * @param file   Uploaded file
     * @param folder Folder name
     * @return Uploaded file information
     */
    FileStorageResultDto upload(MultipartFile file, String folder) throws Exception;

    /**
     * Download file as stream.
     */
    InputStream download(
            String objectName) throws Exception;

    /**
     * Delete file.
     */
    void delete(
            String objectName) throws Exception;

    /**
     * Generate public or presigned URL.
     */
    String generateUrl(
            String objectName) throws Exception;

    /**
     * Check whether file exists.
     */
    boolean exists(
            String objectName) throws Exception;
}
