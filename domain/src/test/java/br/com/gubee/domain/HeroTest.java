package br.com.gubee.domain;

import br.com.gubee.configuration.Hero;
import br.com.gubee.configuration.enums.Race;
import br.com.gubee.configuration.exception.InvalidHeroAttributeException;
import br.com.gubee.configuration.exception.NullRaceException;
import br.com.gubee.configuration.exception.RaceNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HeroTest {

    @Test
    void createAHeroSucceeds() {
        // given
        String name = "Batman";
        String race = "HUMAN";
        UUID powerStatsId = UUID.randomUUID();

        // when
        Hero hero = new Hero(name,race,powerStatsId);

        // then
        assertNotNull(hero);
        assertEquals(name,hero.getName());
        assertEquals(Race.valueOf(race),hero.getRace());
        assertEquals(powerStatsId,hero.getPowerStatsId());
    }

    @Test
    void createAHeroShouldNotSucceedsWhenNameIsNull() {
        // given
        String name = null;
        String race = "HUMAN";
        UUID powerStatsId = UUID.randomUUID();

        // when
        InvalidHeroAttributeException e = assertThrows(
                InvalidHeroAttributeException.class, () -> new Hero(name, race, powerStatsId)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Blank or null attributes are not allowed.");
    }

    @Test
    void createAHeroShouldNotSucceedsWhenNameIsBlank() {
        // given
        String name = "";
        String race = "HUMAN";
        UUID powerStatsId = UUID.randomUUID();

        // when
        InvalidHeroAttributeException e = assertThrows(
                InvalidHeroAttributeException.class, () -> new Hero(name, race, powerStatsId)
        );


        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Blank or null attributes are not allowed.");
    }

    @Test
    void createAHeroShouldNotSucceedsWhenRaceIsNull() {
        // given
        String name = "Batman";
        String race = null;
        UUID powerStatsId = UUID.randomUUID();

        // when
        NullRaceException e = assertThrows(
                NullRaceException.class, () -> new Hero(name, race, powerStatsId)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"RACE must not be null.");
    }

    @Test
    void createAHeroShouldNotSucceedsWhenRaceIsBlank() {
        // given
        String name = "Batman";
        String race = "";
        UUID powerStatsId = UUID.randomUUID();

        // when
        RaceNotFoundException e = assertThrows(
                RaceNotFoundException.class, () -> new Hero(name, race, powerStatsId)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"\"" + race + "\"" + " is not a valid RACE ENUM.");
    }

    @Test
    void createAHeroShouldNotSucceedsWhenRaceIsInvalid() {
        // given
        String name = "Batman";
        String race = "INVALID_RACE";
        UUID powerStatsId = UUID.randomUUID();

        // when
        RaceNotFoundException e = assertThrows(
                RaceNotFoundException.class, () -> new Hero(name, race, powerStatsId)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"\"" + race + "\"" + " is not a valid RACE ENUM.");
    }

    @Test
    void createAHeroShouldNotSucceedsWhenPowerStatsIdIsNull() {
        // given
        String name = "Batman";
        String race = "INVALID_RACE";
        UUID powerStatsId = null;

        // when
        InvalidHeroAttributeException e = assertThrows(
                InvalidHeroAttributeException.class, () -> new Hero(name, race, powerStatsId)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Blank or null attributes are not allowed.");
    }
}