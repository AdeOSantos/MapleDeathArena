package com.mapledeatharena.service;

import com.mapledeatharena.model.Character;
import com.mapledeatharena.model.Job;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.regex.Pattern;

@Service
public class CharacterService {
    private final ConcurrentHashMap<String, Character> characters = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    // Pattern for name validation: 4-15 characters, only letters and underscores
    private static final Pattern NAME_PATTERN = Pattern.compile("^[a-zA-Z_]{4,15}$");


    // Create a new character with validation
    public Character create(String name, String jobName) {

        // Validate name
        validateName(name);

        // Validate and parse job
        Job job = validateJob(jobName);

        // Generate unique ID
        String id = String.valueOf(idGenerator.getAndIncrement());

        // Create character with job-based stats
        Character character = Character.createNewCharacter(id, name, job);

        // Store in concurrent map
        characters.put(id, character);

        return character;
    }



    // Get all characters
    public List<Character> getAll() {
        return characters.values().stream().toList();
    }


    // Get character by ID
    public Optional<Character> getById(String id) {
        return Optional.ofNullable(characters.get(id));
    }



    // Validate character name
    private void validateName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (!NAME_PATTERN.matcher(name).matches()) {
            throw new IllegalArgumentException(
                    "Name must contain only letters or underscores and be 4-15 characters long"
            );
        }
    }



    // Validate and parse job name
    private Job validateJob(String jobName) {
        if (jobName == null || jobName.isBlank()) {
            throw new IllegalArgumentException("Job cannot be empty");
        }
        try {
            return Job.valueOf(jobName.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Job must be one of: WARRIOR, THIEF, MAGE"
            );
        }
    }


    // Update character state - used after battles
    public void update(Character character) {
        characters.put(character.getId(), character);
    }


    // Clear all characters - useful for testing
    public void clear() {
        characters.clear();
        idGenerator.set(1);
    }
}