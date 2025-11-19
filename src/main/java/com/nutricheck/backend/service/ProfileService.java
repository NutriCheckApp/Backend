package com.nutricheck.backend.service;

import com.nutricheck.backend.domain.User;
import com.nutricheck.backend.dto.*;

public interface ProfileService {

    ProfileResponse getProfile(User user);

    ProfileResponse updateProfile(User user, UpdateUserRequest updateRequest);
}
