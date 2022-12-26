package br.com.gubee.configuration;

import br.com.gubee.configuration.enums.Race;

import java.time.Instant;
import java.util.UUID;


public class Hero {
    private UUID id;

    private String name;

    private Race race;

    private UUID powerStatsId;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean enabled;

    public Hero(String name, String race, UUID powerStatsId) {
        this.name = name;
        this.race = Race.valueOf(race);
        this.powerStatsId = powerStatsId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Race getRace() {
        return race;
    }

    public void setRace(Race race) {
        this.race = race;
    }

    public UUID getPowerStatsId() {
        return powerStatsId;
    }

    public void setPowerStatsId(UUID powerStatsId) {
        this.powerStatsId = powerStatsId;
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

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
