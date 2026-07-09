package com.techtechnicworld.astroPrediction.service.user;

import com.techtechnicworld.astroPrediction.repository.UserRepository;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.CreateAttachmentRequest;
import com.techtechnicworld.astroPrediction.dto.UpdateProfileRequest;
import com.techtechnicworld.astroPrediction.dto.UserProfileDTO;
import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.astroPrediction.security.SecurityUtils;
import com.techtechnicworld.astroPrediction.service.attachment.AttachmentService;
import com.techtechnicworld.enums.AttachmentAccessType;
import com.techtechnicworld.enums.AttachmentCategory;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final AttachmentService attachmentService;

    @Override
    public ApiResponse<UserProfileDTO> getCurrentUser() {
        User userEntity = SecurityUtils.getCurrentUserEntity();

        return ApiResponse.success(UserProfileDTO.from(userEntity));
    }

    @Override
    @Transactional
    public ApiResponse<UserProfileDTO> updateProfile(UpdateProfileRequest request) {
        User userEntity = SecurityUtils.getCurrentUserEntity();
        boolean updated = false;

        updated |= updateIfChanged(
                trimToNull(request.fullName()),
                userEntity::getFullName,
                userEntity::setFullName);

        updated |= updateIfChanged(
                trimToNull(request.phone()),
                userEntity::getPhone,
                userEntity::setPhone);

        updated |= updateIfChanged(
                request.dateOfBirth(),
                userEntity::getDateOfBirth,
                userEntity::setDateOfBirth);

        updated |= updateIfChanged(
                request.birthTime(),
                userEntity::getBirthTime,
                userEntity::setBirthTime);

        updated |= updateIfChanged(
                trimToNull(request.birthPlace()),
                userEntity::getBirthPlace,
                userEntity::setBirthPlace);

        updated |= updateIfChanged(
                request.gender(),
                userEntity::getGender,
                userEntity::setGender);

        updated |= updateIfChanged(
                trimToNull(request.preferredLanguage()),
                userEntity::getPreferredLanguage,
                userEntity::setPreferredLanguage);

        updated |= updateIfChanged(
                trimToNull(request.timezone()),
                userEntity::getTimezone,
                userEntity::setTimezone);

        if (!updated) {
            return ApiResponse.error(
                    "At least one profile field must be provided for update.",
                    "NO_FIELDS_TO_UPDATE");
        }

        // No need to call save() because @Transactional + JPA dirty checking
        // will automatically persist the changes.
        // userRepository.save(userEntity);

        return ApiResponse.success(
                "Profile updated successfully.",
                UserProfileDTO.from(userEntity));
    }

    @Override
    @Transactional
    public ApiResponse<String> uploadProfileImage(MultipartFile file) {
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
        // User userEntity = SecurityUtils.getCurrentUserEntity();

        // attachmentService.createAttachments(
        //         new CreateAttachmentRequest(null, null, AttachmentCategory.PROFILE, AttachmentAccessType.PUBLIC, null), List.of(file));
    }

    @Override
    public ApiResponse<UserProfileDTO> getUserById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getUserById'");
    }

    private <T> boolean updateIfChanged(
            T newValue,
            java.util.function.Supplier<T> getter,
            java.util.function.Consumer<T> setter) {

        if (newValue == null) {
            return false;
        }

        T currentValue = getter.get();

        if (java.util.Objects.equals(currentValue, newValue)) {
            return false;
        }

        setter.accept(newValue);
        return true;
    }

    private String trimToNull(String value) {
        if (value == null) {
            return null;
        }

        value = value.trim();

        return value.isEmpty() ? null : value;
    }
}
