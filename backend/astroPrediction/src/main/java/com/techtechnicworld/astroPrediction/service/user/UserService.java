package com.techtechnicworld.astroPrediction.service.user;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.UpdateProfileRequest;
import com.techtechnicworld.astroPrediction.dto.UserProfileDTO;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    @Override
    public ApiResponse<UserProfileDTO> getCurrentUser() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getCurrentUser'");
    }

    @Override
    public ApiResponse<UserProfileDTO> updateProfile(UpdateProfileRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

    @Override
    public ApiResponse<String> uploadProfileImage(MultipartFile file) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'uploadProfileImage'");
    }

    @Override
    public ApiResponse<UserProfileDTO> getUserById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

}
