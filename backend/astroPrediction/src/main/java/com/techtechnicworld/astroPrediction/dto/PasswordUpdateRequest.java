package com.techtechnicworld.astroPrediction.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record PasswordUpdateRequest(

    @NotBlank(message = "Token is required")
    String token,

    @NotBlank(message = "New password is required")
    @Pattern(
        regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
        message = """
                Password must:
                - Be at least 8 characters long
                - Contain at least one uppercase letter
                - Contain at least one lowercase letter
                - Contain at least one digit
                - Contain at least one special character (@$!%*?&)
                """
    )
    String newPassword

) {}