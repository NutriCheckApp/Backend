package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.calendar.FileMetadata;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageSaverService implements FileSaverService {

    private final Path fileStorageLocation;

    @Autowired
    public ImageSaverService(@Value("${nutricheck.image.save-path}") String savePath,
                             @Value("${nutricheck.image.isRelative}") Boolean isRelative) {
        if (isRelative) {
            this.fileStorageLocation = Paths.get(".").toAbsolutePath().normalize();
        } else {
            this.fileStorageLocation = Paths.get(savePath).toAbsolutePath().normalize();
        }
        try {
            Files.createDirectories(this.fileStorageLocation);
        } catch (Exception ex) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @SneakyThrows
    @Override
    public FileMetadata saveFile(String userPrefix, MultipartFile image) {
        //
        String originalFileName = image.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        // check/create user's directory
        if (!Files.exists(this.fileStorageLocation.resolve(userPrefix))) {
            Files.createDirectory(this.fileStorageLocation.resolve(userPrefix));
        }
        //
        String filename = UUID.randomUUID() + extension;
        Path targetLocation = this.fileStorageLocation.resolve(userPrefix).resolve(filename);

        Files.copy(image.getInputStream(), targetLocation);

        return FileMetadata.builder()
                .fileName(filename)
                .fileUrl(targetLocation.toString())
                .fileSize(image.getSize())
                .build();
    }

    @SneakyThrows
    @Override
    public Resource getFile(String fileUrl) {

        Path targetLocation = Paths.get(fileUrl);
        log.info(targetLocation.toString());

        Resource resource = new UrlResource(targetLocation.toUri());

        // Check if the resource exists and is readable
        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("File not found or not readable: " + fileUrl);
        }
    }
}
