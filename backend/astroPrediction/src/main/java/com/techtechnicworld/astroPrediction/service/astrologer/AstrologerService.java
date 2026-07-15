package com.techtechnicworld.astroPrediction.service.astrologer;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AstrologerApplicationRequest;
import com.techtechnicworld.astroPrediction.dto.AstrologerDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerProfileDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerSearchRequest;
import com.techtechnicworld.astroPrediction.dto.PageResponse;
import com.techtechnicworld.astroPrediction.entity.AstrologerProfileEntity;
import com.techtechnicworld.astroPrediction.entity.Role;
import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.astroPrediction.entity.UserRole;
import com.techtechnicworld.astroPrediction.exception.BadRequestException;
import com.techtechnicworld.astroPrediction.exception.ResourceNotFoundException;
import com.techtechnicworld.astroPrediction.repository.AstrologerProfileRepository;
import com.techtechnicworld.astroPrediction.repository.RoleRepository;
import com.techtechnicworld.astroPrediction.repository.UserRoleRepository;
import com.techtechnicworld.astroPrediction.security.SecurityUtils;
import com.techtechnicworld.enums.RoleName;
import com.techtechnicworld.enums.VerificationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AstrologerService implements IAstrologerService {

    private final AstrologerProfileRepository astrologerProfileRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;

    @Override
    public ApiResponse<AstrologerProfileDTO> applyForOnboarding(AstrologerApplicationRequest request) {
        User userEntity = SecurityUtils.getCurrentUserEntity();
        if (userEntity.getAstrologerProfile() != null) {
            throw new BadRequestException("Already applied as astrologer");
        }

        AstrologerProfileEntity astrologerProfileEntity = AstrologerProfileEntity.builder()
                .user(userEntity)
                .bio(request.bio())
                .experienceYears(request.experienceYears())
                .languages(request.languages())
                .expertise(request.expertise())
                .pricingPerMinute(request.pricingPerMinute())
                .verificationStatus(VerificationStatus.PENDING)
                .build();

        astrologerProfileRepository.save(astrologerProfileEntity);

        if (userEntity.getUserRoles().stream().noneMatch(ur -> ur.getRole().getName() == RoleName.ROLE_ASTROLOGER)) {
            Role astrologerRole = roleRepository.findByName(RoleName.ROLE_ASTROLOGER)
                    .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
            userRoleRepository.save(UserRole.builder().user(userEntity).role(astrologerRole).build());
        }

        return ApiResponse.success("Application submitted", AstrologerProfileDTO.from(astrologerProfileEntity));
    }

    @Override
    public ApiResponse<AstrologerProfileDTO> updateProfile(AstrologerApplicationRequest request) {
        User userEntity = SecurityUtils.getCurrentUserEntity();

        AstrologerProfileEntity astrologerProfileEntity = astrologerProfileRepository
                .findByUserIdAndDeletedAtIsNull(userEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        astrologerProfileEntity.setBio(request.bio());
        astrologerProfileEntity.setExperienceYears(request.experienceYears());
        astrologerProfileEntity.setLanguages(request.languages());
        astrologerProfileEntity.setExpertise(request.expertise());
        astrologerProfileEntity.setPricingPerMinute(request.pricingPerMinute());

        astrologerProfileRepository.save(astrologerProfileEntity);

        return ApiResponse.success("Profile updated", AstrologerProfileDTO.from(astrologerProfileEntity));
    }

    @Override
    public ApiResponse<Void> toggleOnlineStatus() {
        User userEntity = SecurityUtils.getCurrentUserEntity();

        AstrologerProfileEntity astrologerProfileEntity = astrologerProfileRepository
                .findByUserIdAndDeletedAtIsNull(userEntity.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        astrologerProfileEntity.setIsOnline(!astrologerProfileEntity.getIsOnline());

        astrologerProfileRepository.save(astrologerProfileEntity);

        return ApiResponse.success(
                "Status updated to " + (astrologerProfileEntity.getIsOnline().booleanValue() ? "online" : "offline"),
                null);
    }

    @Override
    public ApiResponse<PageResponse<AstrologerDTO>> searchAstrologers(AstrologerSearchRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAstrologers'");
    }

    @Override
    public ApiResponse<AstrologerDTO> getAstrologerById(Long id) {
        AstrologerProfileEntity astrologerProfileEntity = astrologerProfileRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Profile not found"));

        return ApiResponse.success(AstrologerDTO.from(astrologerProfileEntity));
    }

    @Override
    public ApiResponse<List<AstrologerDTO>> getFeaturedAstrologers() {
        List<AstrologerProfileEntity> astrologerProfileEntities = astrologerProfileRepository
                .findTop8ByVerificationStatusAndDeletedAtIsNullOrderByAverageRatingDesc(VerificationStatus.APPROVED);

        return ApiResponse.success(AstrologerDTO.from(astrologerProfileEntities));
    }

    @Override
    public ApiResponse<List<AstrologerDTO>> getOnlineAstrologers() {
        List<AstrologerProfileEntity> astrologerProfileEntities = astrologerProfileRepository
                .findByIsOnlineTrueAndVerificationStatusAndDeletedAtIsNull(VerificationStatus.APPROVED);

        return ApiResponse.success(AstrologerDTO.from(astrologerProfileEntities));
    }

    @Override
    public ApiResponse<Void> updatePricing(BigDecimal pricePerMinute) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePricing'");
    }

    @Override
    public ApiResponse<List<AstrologerProfileDTO>> getPendingApprovals() {
        List<AstrologerProfileEntity> astrologerProfileEntities = astrologerProfileRepository
                .findByVerificationStatusAndDeletedAtIsNull(VerificationStatus.PENDING);

        return ApiResponse.success(AstrologerProfileDTO.from(astrologerProfileEntities));
    }

    @Override
    public ApiResponse<Void> approveAstrologer(Long id) {
        AstrologerProfileEntity astrologerProfileEntity = astrologerProfileRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Astrologer not found"));
        astrologerProfileEntity.setVerificationStatus(VerificationStatus.APPROVED);
        astrologerProfileEntity.setIsVerified(true);

        astrologerProfileRepository.save(astrologerProfileEntity);

        return ApiResponse.success("Astrologer approved", null);
    }

    @Override
    public ApiResponse<Void> rejectAstrologer(Long id, String reason) {
        AstrologerProfileEntity astrologerProfileEntity = astrologerProfileRepository
                .findByIdAndDeletedAtIsNull(id)
                .orElseThrow(() -> new ResourceNotFoundException("Astrologer not found"));
        astrologerProfileEntity.setVerificationStatus(VerificationStatus.REJECTED);

        astrologerProfileRepository.save(astrologerProfileEntity);

        return ApiResponse.success("Astrologer rejected: " + reason, null);
    }
}
