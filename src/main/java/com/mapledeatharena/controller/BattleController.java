package com.mapledeatharena.controller;

import com.mapledeatharena.dto.BattleRequest;
import com.mapledeatharena.dto.BattleResponse;
import com.mapledeatharena.model.BattleResult;
import com.mapledeatharena.model.Character;
import com.mapledeatharena.service.BattleService;
import com.mapledeatharena.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/characters")
public class BattleController {

    private final BattleService battleService;
    private final CharacterService characterService;

    public BattleController(BattleService battleService, CharacterService characterService) {
        this.battleService = battleService;
        this.characterService = characterService;
    }

    /**
     * Execute a battle between two characters
     */
    @PostMapping("/battle")
    public ResponseEntity<?> battle(@Valid @RequestBody BattleRequest request) {
        // Validate attacker exists
        Character attacker = characterService.getById(request.attackerId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Attacker with ID " + request.attackerId() + " not found"
                ));

        // Validate defender exists
        Character defender = characterService.getById(request.defenderId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Defender with ID " + request.defenderId() + " not found"
                ));

        // Validate both characters are alive
        if (!attacker.isAlive()) {
            return ResponseEntity.badRequest()
                    .body("Attacker " + attacker.getName() + " is already dead");
        }
        if (!defender.isAlive()) {
            return ResponseEntity.badRequest()
                    .body("Defender " + defender.getName() + " is already dead");
        }

        // Execute battle
        BattleResult result = battleService.executeBattle(attacker, defender);

        // Convert to response DTO
        BattleResponse response = BattleResponse.from(result);

        return ResponseEntity.ok(response);
    }
}