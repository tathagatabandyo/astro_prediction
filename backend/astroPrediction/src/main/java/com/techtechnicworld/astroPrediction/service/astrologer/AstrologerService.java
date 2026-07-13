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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProfile'");
    }

    @Override
    public ApiResponse<Void> toggleOnlineStatus() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'toggleOnlineStatus'");
    }

    @Override
    public ApiResponse<PageResponse<AstrologerDTO>> searchAstrologers(AstrologerSearchRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchAstrologers'");
    }

    @Override
    public ApiResponse<AstrologerDTO> getAstrologerById(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAstrologerById'");
    }

    @Override
    public ApiResponse<List<AstrologerDTO>> getFeaturedAstrologers() {
        List<AstrologerProfileEntity> astrologerProfileEntities = astrologerProfileRepository
                .findTop8ByVerificationStatusAndDeletedAtIsNullOrderByAverageRatingDesc(VerificationStatus.APPROVED);

        return ApiResponse.success(AstrologerDTO.from(astrologerProfileEntities));
    }

    @Override
    public ApiResponse<List<AstrologerDTO>> getOnlineAstrologers() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getOnlineAstrologers'");
    }

    @Override
    public ApiResponse<Void> updatePricing(BigDecimal pricePerMinute) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updatePricing'");
    }

    @Override
    public ApiResponse<List<AstrologerProfileDTO>> getPendingApprovals() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getPendingApprovals'");
    }

    @Override
    public ApiResponse<Void> approveAstrologer(Long id) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'approveAstrologer'");
    }

    @Override
    public ApiResponse<Void> rejectAstrologer(Long id, String reason) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'rejectAstrologer'");
    }
}
