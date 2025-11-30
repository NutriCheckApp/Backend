package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import org.springframework.web.multipart.MultipartFile;

public interface ImageSaverService {

    String saveImage(User user, MultipartFile image);
}
