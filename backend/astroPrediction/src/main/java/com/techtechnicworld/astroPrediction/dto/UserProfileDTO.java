package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

import com.techtechnicworld.astroPrediction.entity.User;
import com.techtechnicworld.enums.Gender;

public record UserProfileDTO(
                Long id,
                String fullName,
                String email,
                Long profileImageId,
                String phone,
                LocalDate dateOfBirth,
                LocalTime birthTime,
                String birthPlace,
                Gender gender,
                String preferredLanguage,
                String timezone,
                List<String> roles,
                BigDecimal walletBalance,
                LocalDateTime createdAt) {

        public static UserProfileDTO from(User user) {
                List<String> roles = user.getUserRoles().stream()
                                .map(userRole -> userRole.getRole().getName().name())
                                .toList();

                BigDecimal walletBalance = user.getWallet() != null ? user.getWallet().getBalance()
                                : java.math.BigDecimal.ZERO;

                return new UserProfileDTO(
                                user.getId(),
                                user.getFullName(),
                                user.getEmail(),
                                user.getProfileImageId(),
                                user.getPhone(),
                                user.getDateOfBirth(),
                                user.getBirthTime(),
                                user.getBirthPlace(),
                                user.getGender(),
                                user.getPreferredLanguage(),
                                user.getTimezone(),
                                roles,
                                walletBalance,
                                user.getCreatedAt());
        }
}
