package com.techtechnicworld.astroPrediction.service.attachment.storage;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.UUID;

import org.springframework.util.StringUtils;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.FileStorageResultDto;
import com.techtechnicworld.enums.StorageProvider;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LocalStorageService implements IFileStorageService {

    private final StorageProperties storageProperties;
    private Path rootDirectory;

    @Override
    public StorageProvider getProvider() {
        return StorageProvider.LOCAL;
    }

    @PostConstruct
    public void initialize() throws IOException {

        rootDirectory = Paths.get(storageProperties.getLocalPath())
                .toAbsolutePath()
                .normalize();

        Files.createDirectories(rootDirectory);
    }

    @Override
    public FileStorageResultDto upload(MultipartFile file, String folder) throws Exception {

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());

        String extension = getExtension(originalFilename);

        String storedFilename = UUID.randomUUID() + extension;

        LocalDate today = LocalDate.now(ZoneOffset.UTC);

        Path targetDirectory = rootDirectory
                .resolve(folder)
                .resolve(String.valueOf(today.getYear()))
                .resolve(String.format("%02d", today.getMonthValue()))
                .resolve(String.format("%02d", today.getDayOfMonth()));

        Files.createDirectories(targetDirectory);

        Path targetFile = targetDirectory.resolve(storedFilename);

        Files.copy(
                file.getInputStream(),
                targetFile,
                StandardCopyOption.REPLACE_EXISTING);

        String objectName = folder
                + "/"
                + today.getYear()
                + "/"
                + String.format("%02d", today.getMonthValue())
                + "/"
                + String.format("%02d", today.getDayOfMonth())
                + "/"
                + storedFilename;

        String url = storageProperties.getLocalBaseUrl()
                + "/"
                + objectName.replace("\\", "/");

        return FileStorageResultDto.builder()
                .objectName(objectName)
                .storedFilename(storedFilename)
                .url(url)
                .mimeType(file.getContentType())
                .size(file.getSize())
                .build();
    }

    @Override
    public InputStream download(String objectName) throws Exception {
        Path file = rootDirectory.resolve(objectName);

        return Files.newInputStream(file);
    }

    @Override
    public void delete(String objectName) throws Exception {
        Path file = rootDirectory.resolve(objectName);

        Files.deleteIfExists(file);
    }

    @Override
    public String generateUrl(String objectName) throws Exception {
        return storageProperties.getLocalBaseUrl()
                + "/"
                + objectName.replace("\\", "/");
    }

    @Override
    public boolean exists(String objectName) throws Exception {
        return Files.exists(rootDirectory.resolve(objectName));
    }

    private String getExtension(String filename) {

        int index = filename.lastIndexOf('.');

        if (index == -1) {
            return "";
        }

        return filename.substring(index);
    }
}
