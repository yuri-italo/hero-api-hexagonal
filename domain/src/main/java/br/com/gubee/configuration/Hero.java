package br.com.gubee.configuration;

import br.com.gubee.configuration.enums.Race;
import br.com.gubee.configuration.exception.InvalidHeroAttributeException;
import br.com.gubee.configuration.exception.NullRaceException;
import br.com.gubee.configuration.exception.RaceNotFoundException;

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
        if (!isAValidHero(name,powerStatsId))
            throw new InvalidHeroAttributeException("Blank or null attributes are not allowed.");

        this.name = name;

        try {
            this.race = Race.valueOf(race);
        } catch (IllegalArgumentException e) {
            throw new RaceNotFoundException("\"" + race + "\"" + " is not a valid RACE ENUM.");
        } catch (NullPointerException e) {
            throw new NullRaceException("RACE must not be null.");
        }

        this.powerStatsId = powerStatsId;
    }

    private boolean isAValidHero(String name, UUID powerStatsId) {
        if (name == null || name.equals(""))
            return false;

        return powerStatsId != null;
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
