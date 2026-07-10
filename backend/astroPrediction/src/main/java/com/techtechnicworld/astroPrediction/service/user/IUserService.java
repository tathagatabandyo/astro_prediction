package com.techtechnicworld.astroPrediction.service.user;

import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.UpdateProfileRequest;
import com.techtechnicworld.astroPrediction.dto.UserProfileDTO;

public interface IUserService {
    ApiResponse<UserProfileDTO> getCurrentUser();
    ApiResponse<UserProfileDTO> updateProfile(UpdateProfileRequest request);
    ApiResponse<String> uploadProfileImage(MultipartFile file);
    ApiResponse<UserProfileDTO> getUserById(Long id);
}
