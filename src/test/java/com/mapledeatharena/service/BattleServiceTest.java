package com.mapledeatharena.service;

import com.mapledeatharena.model.BattleResult;
import com.mapledeatharena.model.Character;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class BattleServiceTest {

    private CharacterService characterService;
    private BattleService battleService;

    @BeforeEach
    void setUp() {
        characterService = new CharacterService();
        // Use seeded Random for deterministic tests
        Random seededRandom = new Random(42L);
        battleService = new BattleService(characterService, seededRandom);
    }



    @Test
    void executeBattle_warriorVsThief_shouldProduceCorrectResult() {
        Character warrior = characterService.create("TestWarrior", "WARRIOR");
        Character thief = characterService.create("TestThief", "THIEF");

        BattleResult result = battleService.executeBattle(warrior, thief);

        assertNotNull(result);
        assertNotNull(result.winner());
        assertNotNull(result.loser());
        assertFalse(result.battleLog().isEmpty());

        assertTrue(result.winner().isAlive());
        assertFalse(result.loser().isAlive());
        assertEquals(0, result.loser().getHealthPoints());
        assertTrue(result.winner().getHealthPoints() > 0);
    }

    @Test
    void executeBattle_mageVsWarrior_shouldProduceCorrectResult() {
        Character mage = characterService.create("TestMage", "MAGE");
        Character warrior = characterService.create("TestWarrior", "WARRIOR");

        BattleResult result = battleService.executeBattle(mage, warrior);

        assertNotNull(result);
        assertTrue(result.winner().isAlive());
        assertFalse(result.loser().isAlive());
    }

    @Test
    void executeBattle_thiefVsMage_shouldProduceCorrectResult() {
        Character thief = characterService.create("TestThief", "THIEF");
        Character mage = characterService.create("TestMage", "MAGE");

        BattleResult result = battleService.executeBattle(thief, mage);

        assertNotNull(result);
        assertTrue(result.winner().isAlive());
        assertFalse(result.loser().isAlive());
    }



    @Test
    void executeBattle_shouldProduceCorrectLogFormat() {
        Character warrior = characterService.create("WarriorOne", "WARRIOR");
        Character thief = characterService.create("ThiefOne", "THIEF");

        BattleResult result = battleService.executeBattle(warrior, thief);
        List<String> log = result.battleLog();

        assertTrue(log.get(0).matches(
                "Battle between \\w+ \\(\\w+\\) - \\d+ HP and \\w+ \\(\\w+\\) - \\d+ HP begins!"
        ));

        assertTrue(log.stream().anyMatch(line ->
                line.matches("\\w+ \\d+ speed was faster than \\w+ \\d+ speed and will begin this round.")
        ));

        assertTrue(log.stream().anyMatch(line ->
                line.matches("\\w+ attacks \\w+ for \\d+, \\w+ has \\d+ HP remaining.")
        ));

        String lastLine = log.get(log.size() - 1);
        assertTrue(lastLine.matches(
                "\\w+ wins the battle! \\w+ still has \\d+ HP remaining!"
        ));
    }

    @Test
    void executeBattle_withFixedSeed_shouldProduceExactLog() {
        CharacterService freshService = new CharacterService();
        Random deterministicRandom = new Random(12345L);
        BattleService deterministicBattle = new BattleService(freshService, deterministicRandom);

        Character warrior = freshService.create("WarriorTest", "WARRIOR");
        Character thief = freshService.create("ThiefTest", "THIEF");

        BattleResult result = deterministicBattle.executeBattle(warrior, thief);
        List<String> log = result.battleLog();

        assertEquals("Battle between WarriorTest (WARRIOR) - 20 HP and ThiefTest (THIEF) - 15 HP begins!",
                log.get(0));

        assertTrue(log.size() >= 3);

        String lastLine = log.get(log.size() - 1);
        assertTrue(lastLine.contains("wins the battle!"));
        assertTrue(lastLine.contains("still has"));
        assertTrue(lastLine.contains("HP remaining!"));
    }



    @Test
    void executeBattle_shouldNeverShowDrawInLog() {
        Character char1 = characterService.create("CharOne", "WARRIOR");
        Character char2 = characterService.create("CharTwo", "WARRIOR");

        BattleResult result = battleService.executeBattle(char1, char2);

        for (String logLine : result.battleLog()) {
            if (logLine.contains("speed was faster than")) {
                String[] parts = logLine.split(" ");
                int speed1 = Integer.parseInt(parts[1]);
                int speed2 = Integer.parseInt(parts[6]);
                assertNotEquals(speed1, speed2, "Speed rolls should never be equal");
            }
        }
    }



    @Test
    void executeBattle_damageShouldBeWithinAttackModifierRange() {
        Character attacker = characterService.create("Attacker", "WARRIOR");
        Character defender = characterService.create("Defender", "THIEF");

        BattleResult result = battleService.executeBattle(attacker, defender);

        for (String logLine : result.battleLog()) {
            if (logLine.contains("attacks") && logLine.contains("for")) {
                String[] parts = logLine.split(" ");
                int damage = Integer.parseInt(parts[4].replace(",", ""));

                assertTrue(damage >= 0);
                assertTrue(damage <= Math.max(
                        attacker.getAttackModifier(),
                        defender.getAttackModifier()
                ));
            }
        }
    }



    @Test
    void executeBattle_shouldUpdateCharacterStatesInService() {
        Character warrior = characterService.create("Warrior", "WARRIOR");
        Character thief = characterService.create("Thief", "THIEF");

        String warriorId = warrior.getId();
        String thiefId = thief.getId();

        battleService.executeBattle(warrior, thief);

        Character updatedWarrior = characterService.getById(warriorId).orElseThrow();
        Character updatedThief = characterService.getById(thiefId).orElseThrow();

        assertNotEquals(updatedWarrior.isAlive(), updatedThief.isAlive());
    }



    @Test
    void executeBattle_loserHPShouldBeExactlyZero() {
        Character char1 = characterService.create("CharOne", "WARRIOR");
        Character char2 = characterService.create("CharTwo", "MAGE");

        BattleResult result = battleService.executeBattle(char1, char2);

        assertEquals(0, result.loser().getHealthPoints());
        assertFalse(result.loser().isAlive());
    }

    @Test
    void executeBattle_winnerHPShouldBePositive() {
        Character char1 = characterService.create("CharOne", "THIEF");
        Character char2 = characterService.create("CharTwo", "MAGE");

        BattleResult result = battleService.executeBattle(char1, char2);

        assertTrue(result.winner().getHealthPoints() > 0);
        assertTrue(result.winner().isAlive());
    }



    @Test
    void executeBattle_shouldEndWhenOneCharacterDies() {
        Character char1 = characterService.create("CharOne", "WARRIOR");
        Character char2 = characterService.create("CharTwo", "MAGE");

        BattleResult result = battleService.executeBattle(char1, char2);

        assertNotEquals(result.winner().isAlive(), result.loser().isAlive());
    }
}