package br.com.gubee.application;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.exception.HeroNameNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CompareHeroServiceTest {
    private final FindHeroByNamePort findHeroByNamePort = mock(FindHeroByNamePort.class);
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = mock(GetPowerStatsByIdPort.class);
    private final CompareHeroService compareHeroService = new CompareHeroService(
            findHeroByNamePort,getPowerStatsByIdPort
    );

    @Test
    void compareSucceeds() {
        // given
        String heroName = "Batman";
        String heroName2 = "Thor";

        HeroModelApiOut hero = givenExistingHeroName(heroName);
        HeroModelApiOut hero2 = givenOtherExistingHeroName(heroName2);
        PowerStatsModelApiOut stats = givenHeroPowerStatsId(hero.getPowerStatsId());
        PowerStatsModelApiOut stats2 = givenOtherHeroPowerStatsId(hero2.getPowerStatsId());

        // when
        CompareHeroApiIn comparedHeroes = compareHeroService.compare(heroName, heroName2);

        // then
        assertNotNull(comparedHeroes);
        assertNotNull(comparedHeroes.getHeroModelApiIn());
        assertNotNull(comparedHeroes.getHeroModelApiIn2());

        HeroModelApiIn heroModelApiIn = comparedHeroes.getHeroModelApiIn();
        HeroModelApiIn heroModelApiIn2 = comparedHeroes.getHeroModelApiIn2();

        assertEquals(hero.getId(),heroModelApiIn.getId());
        assertEquals(hero.getName(),heroModelApiIn.getName());
        assertEquals(hero.getRace(),heroModelApiIn.getRace());
        assertEquals(hero.getCreatedAt(),heroModelApiIn.getCreatedAt());
        assertEquals(hero.getUpdatedAt(),heroModelApiIn.getUpdatedAt());
        assertEquals(stats.getAgility() - stats2.getAgility(),heroModelApiIn.getAgility());
        assertEquals(stats.getDexterity() - stats2.getDexterity(),heroModelApiIn.getDexterity());
        assertEquals(stats.getIntelligence() - stats2.getIntelligence(),heroModelApiIn.getIntelligence());
        assertEquals(stats.getStrength() - stats2.getStrength(),heroModelApiIn.getStrength());

        assertEquals(hero2.getId(),heroModelApiIn2.getId());
        assertEquals(hero2.getName(),heroModelApiIn2.getName());
        assertEquals(hero2.getRace(),heroModelApiIn2.getRace());
        assertEquals(hero2.getCreatedAt(),heroModelApiIn2.getCreatedAt());
        assertEquals(hero2.getUpdatedAt(),heroModelApiIn2.getUpdatedAt());
        assertEquals(stats2.getAgility() - stats.getAgility(),heroModelApiIn2.getAgility());
        assertEquals(stats2.getDexterity() - stats.getDexterity(),heroModelApiIn2.getDexterity());
        assertEquals(stats2.getIntelligence() - stats.getIntelligence(),heroModelApiIn2.getIntelligence());
        assertEquals(stats2.getStrength() - stats.getStrength(),heroModelApiIn2.getStrength());
    }

    @Test
    void compareShouldNotSucceedsWhenHeroNameNotExists() {
        // given
        String heroName = "Batman";
        String heroName2 = "Thor";

        givenNonExistingHeroName(heroName);
        givenNonExistingHeroName(heroName2);

        // when
        HeroNameNotFoundException e = assertThrows(
                HeroNameNotFoundException.class, () -> compareHeroService.compare(heroName, heroName2)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Name \"" + heroName + "\" was not found.");
    }

    private HeroModelApiOut givenExistingHeroName(String heroName) {
        HeroModelApiOut heroModelApiOut = HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name(heroName)
                .race("HUMAN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();

        when(findHeroByNamePort.findByName(heroName)).thenReturn(Optional.of(heroModelApiOut));

        return heroModelApiOut;
    }

    private HeroModelApiOut givenOtherExistingHeroName(String heroName) {
        HeroModelApiOut heroModelApiOut2 = HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name(heroName)
                .race("DIVINE")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();

        when(findHeroByNamePort.findByName(heroName)).thenReturn(Optional.of(heroModelApiOut2));

        return heroModelApiOut2;
    }

    private void givenNonExistingHeroName(String heroName) {
        when(findHeroByNamePort.findByName(heroName)).thenReturn(Optional.empty());
    }

    private PowerStatsModelApiOut givenHeroPowerStatsId(UUID powerStatsId) {
        PowerStatsModelApiOut powerStatsModelApiOut = PowerStatsModelApiOut.builder()
                .id(powerStatsId)
                .strength(1)
                .agility(5)
                .dexterity(10)
                .intelligence(3)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(getPowerStatsByIdPort.findById(powerStatsModelApiOut.getId())).thenReturn(powerStatsModelApiOut);

        return powerStatsModelApiOut;
    }

    private PowerStatsModelApiOut givenOtherHeroPowerStatsId(UUID powerStatsId2) {
        PowerStatsModelApiOut powerStatsModelApiOut2 = PowerStatsModelApiOut.builder()
                .id(powerStatsId2)
                .strength(9)
                .agility(8)
                .dexterity(7)
                .intelligence(6)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(getPowerStatsByIdPort.findById(powerStatsModelApiOut2.getId())).thenReturn(powerStatsModelApiOut2);

        return powerStatsModelApiOut2;
    }
}