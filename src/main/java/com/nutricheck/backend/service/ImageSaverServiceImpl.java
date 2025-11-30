package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class ImageSaverServiceImpl implements ImageSaverService {

    private final Path fileStorageLocation;

    @Autowired
    public ImageSaverServiceImpl(@Value("${nutricheck.image.save-path}") String savePath,
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
    public String saveImage(User user, MultipartFile image) {

        String originalFileName = image.getOriginalFilename();
        String extension = "";
        if (originalFileName != null && originalFileName.contains(".")) {
            extension = originalFileName.substring(originalFileName.lastIndexOf("."));
        }
        String fileName = UUID.randomUUID() + extension;

        Path targetLocation = this.fileStorageLocation.resolve(fileName);
        Files.copy(image.getInputStream(), targetLocation);

        log.info(targetLocation.toAbsolutePath().toString());

        return targetLocation.toAbsolutePath().toString();
    }
}
