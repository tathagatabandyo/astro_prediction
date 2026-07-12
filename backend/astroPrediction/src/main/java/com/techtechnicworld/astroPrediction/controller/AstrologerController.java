package com.techtechnicworld.astroPrediction.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.techtechnicworld.astroPrediction.dto.ApiResponse;
import com.techtechnicworld.astroPrediction.dto.AstrologerApplicationRequest;
import com.techtechnicworld.astroPrediction.dto.AstrologerDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerProfileDTO;
import com.techtechnicworld.astroPrediction.dto.AstrologerSearchRequest;
import com.techtechnicworld.astroPrediction.dto.PageResponse;
import com.techtechnicworld.astroPrediction.service.astrologer.AstrologerService;

import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequiredArgsConstructor
@RequestMapping("/astrologers")
public class AstrologerController {
    private final AstrologerService astrologerService;

    @PostMapping("/apply")
    public ResponseEntity<ApiResponse<AstrologerProfileDTO>> postMethodName(
            @RequestBody AstrologerApplicationRequest astrologerApplicationRequest) {
        return ResponseEntity.ok(astrologerService.applyForOnboarding(astrologerApplicationRequest));
    }

    @PutMapping("/profile")
    public ResponseEntity<ApiResponse<AstrologerProfileDTO>> updateProfile(
            @RequestBody AstrologerApplicationRequest request) {
        return ResponseEntity.ok(astrologerService.updateProfile(request));
    }

    @PostMapping("/toggle-online")
    public ResponseEntity<ApiResponse<Void>> toggleOnline() {
        return ResponseEntity.ok(astrologerService.toggleOnlineStatus());
    }

    @PostMapping("/pricing")
    public ResponseEntity<ApiResponse<Void>> updatePricing(@RequestParam BigDecimal price) {
        return ResponseEntity.ok(astrologerService.updatePricing(price));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse<PageResponse<AstrologerDTO>>> search(AstrologerSearchRequest request) {
        return ResponseEntity.ok(astrologerService.searchAstrologers(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<AstrologerDTO>> getById(@PathVariable Long id) {
        return ResponseEntity.ok(astrologerService.getAstrologerById(id));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<java.util.List<AstrologerDTO>>> getFeatured() {
        return ResponseEntity.ok(astrologerService.getFeaturedAstrologers());
    }

    @GetMapping("/online")
    public ResponseEntity<ApiResponse<java.util.List<AstrologerDTO>>> getOnline() {
        return ResponseEntity.ok(astrologerService.getOnlineAstrologers());
    }
}
