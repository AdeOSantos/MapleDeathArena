package com.mapledeatharena.model;

import java.util.List;

public record BattleResult(
        Character winner,
        Character loser,
        List<String> battleLog
) {
}