package com.mapledeatharena.service;

import com.mapledeatharena.model.Character;
import com.mapledeatharena.model.Job;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class CharacterServiceTest {

    private CharacterService characterService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterService();
    }

    @Test
    void createCharacter_withValidWarrior_shouldSucceed() {
        Character character = characterService.create("TestWarrior", "WARRIOR");

        assertNotNull(character);
        assertEquals("TestWarrior", character.getName());
        assertEquals(Job.WARRIOR, character.getJob());
        assertEquals("1", character.getId());
        assertEquals(20, character.getHealthPoints());
        assertEquals(20, character.getMaxHealthPoints());
        assertEquals(10, character.getStrength());
        assertEquals(5, character.getDexterity());
        assertEquals(5, character.getIntelligence());
        assertTrue(character.isAlive());
        assertEquals(1, character.getLevel());
        assertEquals(9.0, character.getAttackModifier(), 0.01);
        assertEquals(4.0, character.getSpeedModifier(), 0.01);
    }

    @Test
    void createCharacter_withValidThief_shouldSucceed() {
        Character character = characterService.create("Sneaky_Thief", "THIEF");

        assertEquals(Job.THIEF, character.getJob());
        assertEquals(15, character.getHealthPoints());
        assertEquals(4, character.getStrength());
        assertEquals(10, character.getDexterity());
        assertEquals(4, character.getIntelligence());
        assertEquals(12.0, character.getAttackModifier(), 0.01);
        assertEquals(8.0, character.getSpeedModifier(), 0.01);
    }

    @Test
    void createCharacter_withValidMage_shouldSucceed() {
        Character character = characterService.create("Magic_User", "MAGE");

        assertEquals(Job.MAGE, character.getJob());
        assertEquals(12, character.getHealthPoints());
        assertEquals(5, character.getStrength());
        assertEquals(6, character.getDexterity());
        assertEquals(10, character.getIntelligence());
        assertEquals(14.2, character.getAttackModifier(), 0.01);
        assertEquals(2.9, character.getSpeedModifier(), 0.01);
    }

    @ParameterizedTest
    @ValueSource(strings = {"warrior", "WARRIOR", "WaRrIoR"})
    void createCharacter_withCaseInsensitiveJob_shouldSucceed(String jobName) {
        Character character = characterService.create("TestChar", jobName);
        assertEquals(Job.WARRIOR, character.getJob());
    }

    @Test
    void createCharacter_withMinimumNameLength_shouldSucceed() {
        Character character = characterService.create("Test", "WARRIOR");
        assertEquals("Test", character.getName());
    }

    @Test
    void createCharacter_withMaximumNameLength_shouldSucceed() {
        Character character = characterService.create("MaxLengthNames", "WARRIOR");
        assertEquals("MaxLengthNames", character.getName());
    }

    @Test
    void createCharacter_withUnderscores_shouldSucceed() {
        Character character = characterService.create("Test_Name_War", "WARRIOR");
        assertEquals("Test_Name_War", character.getName());
    }

    @Test
    void createCharacter_shouldGenerateIncrementalIds() {
        Character char1 = characterService.create("FirstChar", "WARRIOR");
        Character char2 = characterService.create("SecondChar", "THIEF");
        Character char3 = characterService.create("ThirdChar", "MAGE");

        assertEquals("1", char1.getId());
        assertEquals("2", char2.getId());
        assertEquals("3", char3.getId());
    }

    @Test
    void createCharacter_withNullName_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create(null, "WARRIOR"));
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createCharacter_withEmptyName_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("", "WARRIOR"));
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createCharacter_withBlankName_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("   ", "WARRIOR"));
        assertEquals("Name cannot be empty", exception.getMessage());
    }

    @Test
    void createCharacter_withTooShortName_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("Bob", "WARRIOR"));
        assertTrue(exception.getMessage().contains("4-15 characters long"));
    }

    @Test
    void createCharacter_withTooLongName_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("ThisNameIsTooLong", "WARRIOR"));
        assertTrue(exception.getMessage().contains("4-15 characters long"));
    }

    @ParameterizedTest
    @ValueSource(strings = {"Test123", "Test Name", "Test-Name", "Test@Name", "Tést"})
    void createCharacter_withInvalidCharacters_shouldThrowException(String invalidName) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create(invalidName, "WARRIOR"));
        assertTrue(exception.getMessage().contains("letters or underscores"));
    }

    @Test
    void createCharacter_withNullJob_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("TestChar", null));
        assertEquals("Job cannot be empty", exception.getMessage());
    }

    @Test
    void createCharacter_withEmptyJob_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("TestChar", ""));
        assertEquals("Job cannot be empty", exception.getMessage());
    }

    @Test
    void createCharacter_withBlankJob_shouldThrowException() {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("TestChar", "   "));
        assertEquals("Job cannot be empty", exception.getMessage());
    }

    @ParameterizedTest
    @ValueSource(strings = {"KNIGHT", "ARCHER", "INVALID", "123"})
    void createCharacter_withInvalidJob_shouldThrowException(String invalidJob) {
        Exception exception = assertThrows(IllegalArgumentException.class,
                () -> characterService.create("TestChar", invalidJob));
        assertTrue(exception.getMessage().contains("WARRIOR, THIEF, MAGE"));
    }

    @Test
    void getAll_withNoCharacters_shouldReturnEmptyList() {
        List<Character> characters = characterService.getAll();
        assertTrue(characters.isEmpty());
    }

    @Test
    void getAll_withMultipleCharacters_shouldReturnAll() {
        characterService.create("CharWarrior", "WARRIOR");
        characterService.create("CharThief", "THIEF");
        characterService.create("CharMage", "MAGE");

        List<Character> characters = characterService.getAll();
        assertEquals(3, characters.size());
    }

    @Test
    void getById_withExistingId_shouldReturnCharacter() {
        Character created = characterService.create("TestChar", "WARRIOR");
        Optional<Character> found = characterService.getById(created.getId());

        assertTrue(found.isPresent());
        assertEquals("TestChar", found.get().getName());
    }

    @Test
    void getById_withNonExistingId_shouldReturnEmpty() {
        Optional<Character> found = characterService.getById("999");
        assertTrue(found.isEmpty());
    }

    @Test
    void update_withModifiedCharacter_shouldPersistChanges() {
        Character character = characterService.create("TestChar", "WARRIOR");
        character.takeDamage(10);

        characterService.update(character);

        Optional<Character> updated = characterService.getById(character.getId());
        assertTrue(updated.isPresent());
        assertEquals(10, updated.get().getHealthPoints());
    }

    @Test
    void clear_shouldRemoveAllCharactersAndResetIdGenerator() {
        characterService.create("Char1", "WARRIOR");
        characterService.create("Char2", "THIEF");

        characterService.clear();

        assertTrue(characterService.getAll().isEmpty());

        Character newChar = characterService.create("NewChar", "MAGE");
        assertEquals("1", newChar.getId());
    }
}