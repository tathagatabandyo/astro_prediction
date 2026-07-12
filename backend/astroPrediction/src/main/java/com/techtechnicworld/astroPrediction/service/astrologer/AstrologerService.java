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
import com.techtechnicworld.astroPrediction.repository.AstrologerProfileRepository;
import com.techtechnicworld.enums.VerificationStatus;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AstrologerService implements IAstrologerService {

    private final AstrologerProfileRepository astrologerProfileRepository;

    @Override
    public ApiResponse<AstrologerProfileDTO> applyForOnboarding(AstrologerApplicationRequest request) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'applyForOnboarding'");
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
