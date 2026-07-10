package com.techtechnicworld.astroPrediction.dto;

import java.time.LocalDate;
import java.time.LocalTime;

import com.techtechnicworld.enums.Gender;

import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UpdateProfileRequest(

        @Size(min = 3, max = 100, message = "Full name must be between 3 and 100 characters") String fullName,

        @Pattern(regexp = "^[6-9]\\d{9}$", message = "Phone number must be a valid 10-digit Indian mobile number") String phone,

        @Past(message = "Date of birth must be in the past") LocalDate dateOfBirth,

        LocalTime birthTime,

        @Size(min = 2, max = 150, message = "Birth place must be between 2 and 150 characters") String birthPlace,

        Gender gender,

        @Size(max = 50, message = "Preferred language cannot exceed 50 characters") String preferredLanguage,

        @Size(max = 100, message = "Timezone cannot exceed 100 characters") String timezone

) {
}