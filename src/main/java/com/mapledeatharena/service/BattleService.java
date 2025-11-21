package com.mapledeatharena.service;

import com.mapledeatharena.model.BattleResult;
import com.mapledeatharena.model.Character;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class BattleService {

    private final Random random = new Random();
    private final CharacterService characterService;

    public BattleService(CharacterService characterService, Random random) {
        this.characterService = characterService;

    }

    //Execute a battle between two characters. Fight!
    public BattleResult executeBattle(Character attacker, Character defender) {
        List<String> battleLog = new ArrayList<>();

        // Initial battle message
        battleLog.add(String.format(
                "Battle between %s (%s) - %d HP and %s (%s) - %d HP begins!",
                attacker.getName(), attacker.getJob(), attacker.getHealthPoints(),
                defender.getName(), defender.getJob(), defender.getHealthPoints()
        ));

        Character currentAttacker = attacker;
        Character currentDefender = defender;

        // Battle rounds continue until one character dies
        while (attacker.isAlive() && defender.isAlive()) {

            // Determine who goes first this round - with invisible rerolls on draws
            SpeedRoll speedRoll = determineFirstAttacker(attacker, defender);

            battleLog.add(String.format(
                    "%s %d speed was faster than %s %d speed and will begin this round.",
                    speedRoll.faster.getName(), speedRoll.fasterSpeed,
                    speedRoll.slower.getName(), speedRoll.slowerSpeed
            ));

            currentAttacker = speedRoll.faster;
            currentDefender = speedRoll.slower;

            // First attack
            int damage = calculateDamage(currentAttacker);
            currentDefender.takeDamage(damage);

            battleLog.add(String.format(
                    "%s attacks %s for %d, %s has %d HP remaining.",
                    currentAttacker.getName(), currentDefender.getName(), damage,
                    currentDefender.getName(), currentDefender.getHealthPoints()
            ));

            if (currentDefender.isDead()) {
                break;
            }

            // Counter-attack and swap roles
            damage = calculateDamage(currentDefender);
            currentAttacker.takeDamage(damage);

            battleLog.add(String.format(
                    "%s attacks %s for %d, %s has %d HP remaining.",
                    currentDefender.getName(), currentAttacker.getName(), damage,
                    currentAttacker.getName(), currentAttacker.getHealthPoints()
            ));
        }

        // Determine winner and loser
        Character winner = attacker.isAlive() ? attacker : defender;
        Character loser = attacker.isAlive() ? defender : attacker;

        battleLog.add(String.format(
                "%s wins the battle! %s still has %d HP remaining!",
                winner.getName(), winner.getName(), winner.getHealthPoints()
        ));

        // Update character states in storage
        characterService.update(attacker);
        characterService.update(defender);

        return new BattleResult(winner, loser, battleLog);
    }

    // Determine which character attacks first based on speed rolls
    // Re-rolls invisibly on draws - only shows final non-draw result
    private SpeedRoll determineFirstAttacker(Character char1, Character char2) {
        int speed1, speed2;

        do {
            speed1 = random.nextInt((int) char1.getSpeedModifier() + 1);
            speed2 = random.nextInt((int) char2.getSpeedModifier() + 1);
        } while (speed1 == speed2); // Invisible reroll on draw

        if (speed1 > speed2) {
            return new SpeedRoll(char1, speed1, char2, speed2);
        } else {
            return new SpeedRoll(char2, speed2, char1, speed1);
        }
    }


    // Calculate damage based on attack modifier
    private int calculateDamage(Character attacker) {
        return random.nextInt((int) attacker.getAttackModifier() + 1);
    }



    // Internal record to store speed roll results
    private record SpeedRoll(
            Character faster,
            int fasterSpeed,
            Character slower,
            int slowerSpeed
    ) {}
}