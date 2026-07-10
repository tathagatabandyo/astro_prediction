package com.techtechnicworld.astroPrediction.service.astrologer;

import java.math.BigDecimal;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AstrologerApplicationRequest;
import com.techtechnicworld.astroPrediction.dto.AstrologerDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerProfileDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerSearchRequest;
import com.techtechnicworld.astroPrediction.dto.PageResponse;

public interface IAstrologerService {
ApiResponse<AstrologerProfileDTO> applyForOnboarding(AstrologerApplicationRequest request);
    ApiResponse<AstrologerProfileDTO> updateProfile(AstrologerApplicationRequest request);
    ApiResponse<Void> toggleOnlineStatus();
    ApiResponse<PageResponse<AstrologerDTO>> searchAstrologers(AstrologerSearchRequest request);
    ApiResponse<AstrologerDTO> getAstrologerById(Long id);
    ApiResponse<java.util.List<AstrologerDTO>> getFeaturedAstrologers();
    ApiResponse<java.util.List<AstrologerDTO>> getOnlineAstrologers();
    ApiResponse<Void> updatePricing(BigDecimal pricePerMinute);
    ApiResponse<java.util.List<AstrologerProfileDTO>> getPendingApprovals();
    ApiResponse<Void> approveAstrologer(Long id);
    ApiResponse<Void> rejectAstrologer(Long id, String reason);
}
