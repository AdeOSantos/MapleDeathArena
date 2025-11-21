package com.mapledeatharena.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Character {
    private String id;
    private String name;
    private Job job;

    private int healthPoints;
    private int maxHealthPoints;
    private int strength;
    private int dexterity;
    private int intelligence;

    // Battle modifiers - calculated from Job.
    private double attackModifier;
    private double speedModifier;

    // Status
    @Builder.Default
    private boolean alive = true;

    // Future-proof: Level system - not implemented yet
    @Builder.Default
    private int level = 1;

    /**
     * Factory method to create a new character with job-based stats
     */
    public static Character createNewCharacter(String id, String name, Job job) {
        Character character = Character.builder()
                .id(id)
                .name(name)
                .job(job)
                .healthPoints(job.getBaseHealthPoints())
                .maxHealthPoints(job.getBaseHealthPoints())
                .strength(job.getBaseStrength())
                .dexterity(job.getBaseDexterity())
                .intelligence(job.getBaseIntelligence())
                .build();

        // Calculate modifiers using Job enum methods
        character.calculateModifiers();
        return character;
    }

    /**
     * Calculate attack and speed modifiers based on current stats
     */
    public void calculateModifiers() {
        this.attackModifier = job.calculateAttackModifier(strength, dexterity, intelligence);
        this.speedModifier = job.calculateSpeedModifier(strength, dexterity, intelligence);
    }

    /**
     * Take damage from an attack
     */
    public void takeDamage(int damage) {
        this.healthPoints = Math.max(0, this.healthPoints - damage);
        if (this.healthPoints == 0) {
            this.alive = false;
        }
    }

    /**
     * Simple check if character is dead
     */
    public boolean isDead() {
        return !alive;
    }
}