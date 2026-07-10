package com.techtechnicworld.astroPrediction.dto;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

public record AstrologerApplicationRequest(

        @NotBlank(message = "Bio is required.") 
        @Size(max = 2000, message = "Bio must not exceed 2000 characters.")
        String bio,

        @NotNull(message = "Experience years is required.") 
        @PositiveOrZero(message = "Experience years cannot be negative.") 
        Integer experienceYears,

        @NotEmpty(message = "At least one language is required.")
        @Size(max = 10, message = "A maximum of 10 languages is allowed.")
        List<
            @NotBlank(message = "Language cannot be blank.")
            @Size(max = 50, message = "Language must not exceed 50 characters.") 
            String
        > languages,

        @NotEmpty(message = "At least one expertise is required.")
        @Size(max = 20, message = "A maximum of 20 expertise areas is allowed.")
        List<
            @NotBlank(message = "Expertise cannot be blank.")
            @Size(max = 100, message = "Expertise must not exceed 100 characters.")
            String
        > expertise,

        @NotNull(message = "Pricing per minute is required.")
        @DecimalMin(value = "0.0", inclusive = false, message = "Pricing per minute must be greater than 0.")
        BigDecimal pricingPerMinute
) {
}
