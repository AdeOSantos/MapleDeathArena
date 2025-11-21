package com.mapledeatharena.dto;

import com.mapledeatharena.model.Character;
import com.mapledeatharena.model.BattleResult;

import java.util.List;

public record BattleResponse(
        CharacterSummary winner,
        CharacterSummary loser,
        List<String> battleLog
) {
    public record CharacterSummary(
            String id,
            String name,
            String job,
            int remainingHealthPoints,
            boolean alive
    ) {
        public static CharacterSummary from(Character character) {
            return new CharacterSummary(
                    character.getId(),
                    character.getName(),
                    character.getJob().name(),
                    character.getHealthPoints(),
                    character.isAlive()
            );
        }
    }

    public static BattleResponse from(BattleResult battleResult) {
        return new BattleResponse(
                CharacterSummary.from(battleResult.winner()),
                CharacterSummary.from(battleResult.loser()),
                battleResult.battleLog()
        );
    }
}