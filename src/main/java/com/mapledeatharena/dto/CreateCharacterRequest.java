package com.mapledeatharena.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CreateCharacterRequest(
        @NotBlank(message = "Name cannot be empty")
        @Pattern(
                regexp = "^[a-zA-Z_]{4,15}$",
                message = "Name must contain only letters or underscores and be 4-15 characters long"
        )
        String name,

        @NotNull(message = "Job cannot be null")
        @NotBlank(message = "Job cannot be empty")
        @Pattern(
                regexp = "^(WARRIOR|THIEF|MAGE)$",
                flags = Pattern.Flag.CASE_INSENSITIVE,
                message = "Job must be one of: WARRIOR, THIEF, MAGE"
        )
        String job
) {
}