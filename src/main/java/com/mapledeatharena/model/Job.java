package com.mapledeatharena.model;

import lombok.Getter;

@Getter
public enum Job {
    WARRIOR(20, 10, 5, 5),
    THIEF(15, 4, 10, 4),
    MAGE(12, 5, 6, 10);

    private final int baseHealthPoints;
    private final int baseStrength;
    private final int baseDexterity;
    private final int baseIntelligence;

    Job(int baseHealthPoints, int baseStrength, int baseDexterity, int baseIntelligence) {
        this.baseHealthPoints = baseHealthPoints;
        this.baseStrength = baseStrength;
        this.baseDexterity = baseDexterity;
        this.baseIntelligence = baseIntelligence;
    }

    /**
     * Calculate attack modifier based on job-specific formula
     */
    public double calculateAttackModifier(int strength, int dexterity, int intelligence) {
        return switch (this) {
            case WARRIOR -> (0.8 * strength) + (0.2 * dexterity);
            case THIEF -> (0.25 * strength) + dexterity + (0.25 * intelligence);
            case MAGE -> (0.2 * strength) + (0.2 * dexterity) + (1.2 * intelligence);
        };
    }

    /**
     * Calculate speed modifier based on job-specific formula
     */
    public double calculateSpeedModifier(int strength, int dexterity, int intelligence) {
        return switch (this) {
            case WARRIOR -> (0.6 * dexterity) + (0.2 * intelligence);
            case THIEF -> 0.8 * dexterity;
            case MAGE -> (0.4 * dexterity) + (0.1 * strength);
        };
    }
}