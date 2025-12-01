package com.nutricheck.backend.service;

import com.nutricheck.backend.dto.calendar.FileMetadata;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileSaverService {

    FileMetadata saveFile(String userPrefix, MultipartFile file);

    Resource getFile(String fileUrl);

    void deleteFile(String fileUrl);
}
