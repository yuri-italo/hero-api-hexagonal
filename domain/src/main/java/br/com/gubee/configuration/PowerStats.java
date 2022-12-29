package br.com.gubee.configuration;

import java.time.Instant;
import java.util.UUID;

public class PowerStats {

    private UUID id;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;

    public PowerStats(
            UUID id, int strength, int agility, int dexterity, int intelligence, Instant createdAt, Instant updatedAt
    ) {
        if (areValidFields(id,createdAt,updatedAt))
            throw new IllegalArgumentException();

        if (!areValidStats(strength,agility,dexterity,intelligence))
            throw new IllegalArgumentException();

        this.id = id;
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    private boolean areValidFields(UUID id, Instant createdAt, Instant updatedAt) {
        return id != null && createdAt != null && updatedAt != null;
    }

    public PowerStats(int strength, int agility, int dexterity, int intelligence) {
        if (!areValidStats(strength,agility,dexterity,intelligence))
            throw new IllegalArgumentException();

        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }

    private boolean areValidStats(int strength, int agility, int dexterity, int intelligence) {
        if (!isValidStats(strength))
            return false;

        if (!isValidStats(agility))
            return false;

        if (!isValidStats(dexterity))
            return false;

        return isValidStats(intelligence);
    }

    private boolean isValidStats(int stats) {
        return stats >= 0 && stats <= 10;
    }

    public static void compareStats(PowerStats powerStats, PowerStats powerStats2) {
        int agility = powerStats.getAgility();
        int dexterity = powerStats.getDexterity();
        int intelligence = powerStats.getIntelligence();
        int strength = powerStats.getStrength();

        powerStats.setStrength(powerStats.getStrength() - powerStats2.getStrength());
        powerStats.setAgility(powerStats.getAgility() - powerStats2.getAgility());
        powerStats.setDexterity(powerStats.getDexterity() - powerStats2.getDexterity());
        powerStats.setIntelligence(powerStats.getIntelligence() - powerStats2.getIntelligence());

        powerStats2.setStrength(powerStats2.getStrength() - strength);
        powerStats2.setAgility(powerStats2.getAgility() - agility);
        powerStats2.setDexterity(powerStats2.getDexterity() - dexterity);
        powerStats2.setIntelligence(powerStats2.getIntelligence() - intelligence);
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getDexterity() {
        return dexterity;
    }

    public void setDexterity(int dexterity) {
        this.dexterity = dexterity;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public void setIntelligence(int intelligence) {
        this.intelligence = intelligence;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
