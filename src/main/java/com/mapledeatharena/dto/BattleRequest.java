package com.mapledeatharena.dto;

import jakarta.validation.constraints.NotBlank;

public record BattleRequest(
        @NotBlank(message = "Attacker ID cannot be empty")
        String attackerId,

        @NotBlank(message = "Defender ID cannot be empty")
        String defenderId
) {
}