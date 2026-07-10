package com.techtechnicworld.astroPrediction.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.UpdateProfileRequest;
import com.techtechnicworld.astroPrediction.dto.UserProfileDTO;
import com.techtechnicworld.astroPrediction.service.user.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping("/current")
    public ResponseEntity<ApiResponse<UserProfileDTO>> getCurrentUser() {
        return ResponseEntity.ok(userService.getCurrentUser());
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<UserProfileDTO>> updateProfile(
            @Valid @RequestBody UpdateProfileRequest updateProfileRequest) {
        return ResponseEntity.ok(userService.updateProfile(updateProfileRequest));
    }

    @PostMapping("/profile-image")
    public ResponseEntity<ApiResponse<String>> postMethodName(@RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(userService.uploadProfileImage(file));
    }
}
