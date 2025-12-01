package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.calendar.FileMetadata;
import com.nutricheck.backend.exception.exception.DeleteFileException;
import com.nutricheck.backend.exception.exception.DirectoryCreationException;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
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
            throw new DirectoryCreationException(ex);
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
            try {
                Files.createDirectory(this.fileStorageLocation.resolve(userPrefix));
            } catch (IOException e) {
                throw new DirectoryCreationException(e);
            }
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

    @Override
    public Resource getFile(String fileUrl) {

        Path targetLocation = Paths.get(fileUrl);
        log.info(targetLocation.toString());

        try {
            Resource resource = new UrlResource(targetLocation.toUri());
            // Check if the resource exists and is readable
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new DeleteFileException(fileUrl);
            }
        } catch (MalformedURLException e) {
            throw new DeleteFileException(fileUrl, e);
        }
    }

    @Override
    public void deleteFile(String fileUrl) {
        Path fileToDelete = Paths.get(fileUrl);
        try {
            boolean deleted = Files.deleteIfExists(fileToDelete);
            if (deleted) {
                log.info("Successfully deleted file: {}", fileToDelete);
            } else {
                log.warn("File not found or could not be deleted: {}", fileToDelete);
            }
        } catch (IOException e) {
            throw new DeleteFileException(fileUrl, e);
        }
    }
}
