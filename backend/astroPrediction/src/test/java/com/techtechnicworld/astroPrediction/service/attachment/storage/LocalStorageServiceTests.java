package com.techtechnicworld.astroPrediction.service.attachment.storage;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.mock.web.MockMultipartFile;

import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;

class LocalStorageServiceTests {

    @TempDir
    Path uploadDirectory;

    @Test
    void uploadCreatesDatedDirectoriesBeforeWritingFile() throws Exception {
        StorageProperties properties = new StorageProperties();
        properties.setLocalPath(uploadDirectory.toString());

        LocalStorageService storageService = new LocalStorageService(properties);
        storageService.initialize();

        byte[] content = "image-content".getBytes(StandardCharsets.UTF_8);
        MockMultipartFile file = new MockMultipartFile(
                "file",
                "photo.jpeg",
                "image/jpeg",
                content);

        FileStorageResultDto result = storageService.upload(file, "attachments");
        Path storedFile = uploadDirectory.resolve(result.getObjectName());

        assertTrue(Files.isRegularFile(storedFile));
        assertArrayEquals(content, Files.readAllBytes(storedFile));
    }
}
