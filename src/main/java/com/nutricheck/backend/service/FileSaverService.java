package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.calendar.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;

public interface FileSaverService {

    FileMetadata saveFile(String userPrefix, MultipartFile file);

    /**
     * Retrieves a file resource from the specified URL string.
     *
     * @param fileUrl The path or URL string of the resource to retrieve.
     */
    Resource getFile(String fileUrl);

    void deleteFile(String fileUrl);

    Path getUserFileStorageLocation(String username);
}
