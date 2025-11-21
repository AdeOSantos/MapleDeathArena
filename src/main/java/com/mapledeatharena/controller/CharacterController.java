package com.mapledeatharena.controller;

import com.mapledeatharena.dto.CreateCharacterRequest;
import com.mapledeatharena.model.Character;
import com.mapledeatharena.service.CharacterService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/characters")
public class CharacterController {

    private final CharacterService characterService;

    public CharacterController(CharacterService characterService) {
        this.characterService = characterService;
    }

    /**
     * Create a new character
     */
    @PostMapping
    public ResponseEntity<Character> createCharacter(@Valid @RequestBody CreateCharacterRequest request) {
        Character character = characterService.create(request.name(), request.job());
        return ResponseEntity.status(HttpStatus.CREATED).body(character);
    }

    /**
     * Get all characters
     */
    @GetMapping
    public ResponseEntity<List<Character>> getAllCharacters() {
        List<Character> characters = characterService.getAll();
        return ResponseEntity.ok(characters);
    }

    /**
     * Get character by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Character> getCharacterById(@PathVariable String id) {
        return characterService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}