package br.com.gubee.domain;

import br.com.gubee.configuration.PowerStats;
import br.com.gubee.configuration.exception.InvalidStatsException;
import br.com.gubee.configuration.exception.NullPowerStatsAttributeException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PowerStatsTest {

    @Test
    void createPowerStatsWithAllArgumentsShouldSucceeds() {
        // given
        UUID uuid = UUID.randomUUID();
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // when
        PowerStats powerStats = new PowerStats(uuid,strength,agility,dexterity,intelligence,createdAt,updatedAt);

        // then
        assertNotNull(powerStats);
        assertEquals(uuid,powerStats.getId());
        assertEquals(strength,powerStats.getStrength());
        assertEquals(agility,powerStats.getAgility());
        assertEquals(dexterity,powerStats.getDexterity());
        assertEquals(intelligence,powerStats.getIntelligence());
        assertEquals(createdAt,powerStats.getCreatedAt());
        assertEquals(updatedAt,powerStats.getUpdatedAt());
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenIdIsNull() {
        // given
        UUID uuid = null;
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;
        Instant createdAt = Instant.now();
        Instant updatedAt = Instant.now();

        // when
        NullPowerStatsAttributeException e = assertThrows(NullPowerStatsAttributeException.class, () -> new PowerStats(
                uuid, strength, agility, dexterity, intelligence, createdAt, updatedAt)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"null attribute not allowed.");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenCreatedAtIsNull() {
        // given
        UUID uuid = UUID.randomUUID();
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;
        Instant createdAt = null;
        Instant updatedAt = Instant.now();

        // when
        NullPowerStatsAttributeException e = assertThrows(NullPowerStatsAttributeException.class, () -> new PowerStats(
                uuid, strength, agility, dexterity, intelligence, createdAt, updatedAt)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"null attribute not allowed.");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenUpdatedAtIsNull() {
        // given
        UUID uuid = UUID.randomUUID();
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;
        Instant createdAt = Instant.now();
        Instant updatedAt = null;

        // when
        NullPowerStatsAttributeException e = assertThrows(NullPowerStatsAttributeException.class, () -> new PowerStats(
                uuid, strength, agility, dexterity, intelligence, createdAt, updatedAt)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"null attribute not allowed.");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenStrengthIsSmallerThanZero() {
        // given
        int strength = -10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenAgilityIsSmallerThanZero() {
        // given
        int strength = 10;
        int agility = -9;
        int dexterity = 8;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenDexterityIsSmallerThanZero() {
        // given
        int strength = 10;
        int agility = 9;
        int dexterity = -8;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenIntelligenceIsSmallerThanZero() {
        // given
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = -7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }


    @Test
    void createPowerStatsShouldNotSucceedsWhenStrengthIsGreaterThanTen() {
        // given
        int strength = 11;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenAgilityIsGreaterThanTen() {
        // given
        int strength = 10;
        int agility = 11;
        int dexterity = 8;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenDexterityIsGreaterThanTen() {
        // given
        int strength = 10;
        int agility = 9;
        int dexterity = 11;
        int intelligence = 7;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void createPowerStatsShouldNotSucceedsWhenIntelligenceIsGreaterThanTen() {
        // given
        int strength = 10;
        int agility = 9;
        int dexterity = 8;
        int intelligence = 11;

        // when
        InvalidStatsException e = assertThrows(InvalidStatsException.class, () -> new PowerStats(
                strength, agility, dexterity, intelligence)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Stats must be in a 0 to 10 range");
    }

    @Test
    void compareStatsShouldSucceeds() {
        // given
        PowerStats powerStats = new PowerStats(
                UUID.randomUUID(), 10, 9, 8, 7, Instant.now(), Instant.now()
        );

        PowerStats powerStats2 = new PowerStats(
                UUID.randomUUID(), 9, 8, 7, 6, Instant.now(), Instant.now()
        );

        // when
        PowerStats.compareStats(powerStats,powerStats2);

        // then
        assertEquals(1,powerStats.getStrength());
        assertEquals(1,powerStats.getAgility());
        assertEquals(1,powerStats.getDexterity());
        assertEquals(1,powerStats.getIntelligence());

        assertEquals(-1,powerStats2.getStrength());
        assertEquals(-1,powerStats2.getAgility());
        assertEquals(-1,powerStats2.getDexterity());
        assertEquals(-1,powerStats2.getIntelligence());
    }
}