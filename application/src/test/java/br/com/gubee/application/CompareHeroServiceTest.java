package br.com.gubee.application;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroNameNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompareHeroServiceTest {
    private final FindHeroByNamePort findHeroByNamePort = new HeroRepositoryInMemoryImpl();
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = new PowerStatsRepositoryInMemoryImpl();
    private final CompareHeroService compareHeroService = new CompareHeroService(
            findHeroByNamePort,getPowerStatsByIdPort
    );

    @AfterEach
    void setUp() {
        cleanStorage();
    }

    @Test
    void compareSucceeds() {
        // given
        String heroName = "batman";
        String heroName2 = "spider";

        HeroModelApiOut heroModelApiOut = createHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut.getId(),heroModelApiOut);
        PowerStatsModelApiOut powerStatsModelApiOut = createPowerStatsModelApiOut(heroModelApiOut);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut.getId(),powerStatsModelApiOut);

        HeroModelApiOut heroModelApiOut2 = createAnotherHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut2.getId(),heroModelApiOut2);
        PowerStatsModelApiOut powerStatsModelApiOut2 = createAnotherPowerStatsModelApiOut(heroModelApiOut2);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut2.getId(),powerStatsModelApiOut2);


        // when
        CompareHeroApiIn comparedHeroes = compareHeroService.compare(heroName, heroName2);

        // then
        assertNotNull(comparedHeroes);
        assertNotNull(comparedHeroes.getHeroModelApiIn());
        assertNotNull(comparedHeroes.getHeroModelApiIn2());

        HeroModelApiIn heroModelApiIn = comparedHeroes.getHeroModelApiIn();
        HeroModelApiIn heroModelApiIn2 = comparedHeroes.getHeroModelApiIn2();

        assertEquals(heroModelApiOut.getId(),heroModelApiIn.getId());
        assertEquals(heroModelApiOut.getName(),heroModelApiIn.getName());
        assertEquals(heroModelApiOut.getRace(),heroModelApiIn.getRace());
        assertEquals(heroModelApiOut.getCreatedAt(),heroModelApiIn.getCreatedAt());
        assertEquals(heroModelApiOut.getUpdatedAt(),heroModelApiIn.getUpdatedAt());
        assertEquals(powerStatsModelApiOut.getAgility() - powerStatsModelApiOut2.getAgility(),heroModelApiIn.getAgility());
        assertEquals(powerStatsModelApiOut.getDexterity() - powerStatsModelApiOut2.getDexterity(),heroModelApiIn.getDexterity());
        assertEquals(powerStatsModelApiOut.getIntelligence() - powerStatsModelApiOut2.getIntelligence(),heroModelApiIn.getIntelligence());
        assertEquals(powerStatsModelApiOut.getStrength() - powerStatsModelApiOut2.getStrength(),heroModelApiIn.getStrength());

        assertEquals(heroModelApiOut2.getId(),heroModelApiIn2.getId());
        assertEquals(heroModelApiOut2.getName(),heroModelApiIn2.getName());
        assertEquals(heroModelApiOut2.getRace(),heroModelApiIn2.getRace());
        assertEquals(heroModelApiOut2.getCreatedAt(),heroModelApiIn2.getCreatedAt());
        assertEquals(heroModelApiOut2.getUpdatedAt(),heroModelApiIn2.getUpdatedAt());
        assertEquals(powerStatsModelApiOut2.getAgility() - powerStatsModelApiOut.getAgility(),heroModelApiIn2.getAgility());
        assertEquals(powerStatsModelApiOut2.getDexterity() - powerStatsModelApiOut.getDexterity(),heroModelApiIn2.getDexterity());
        assertEquals(powerStatsModelApiOut2.getIntelligence() - powerStatsModelApiOut.getIntelligence(),heroModelApiIn2.getIntelligence());
        assertEquals(powerStatsModelApiOut2.getStrength() - powerStatsModelApiOut.getStrength(),heroModelApiIn2.getStrength());
    }

    @Test
    void compareShouldNotSucceedsWhenHeroNameNotExists() {
        // given
        String heroName = "Superman";
        String heroName2 = "Jessica";

        // when
        HeroNameNotFoundException e = assertThrows(
                HeroNameNotFoundException.class, () -> compareHeroService.compare(heroName, heroName2)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Name \"" + heroName + "\" was not found.");
    }

    private PowerStatsModelApiOut createPowerStatsModelApiOut(HeroModelApiOut heroModelApiOut) {
        return PowerStatsModelApiOut.builder()
                .id(heroModelApiOut.getPowerStatsId())
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(heroModelApiOut.getUpdatedAt())
                .build();
    }

    private HeroModelApiOut createHeroModelApiOut() {
        return HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name("Batman")
                .race("HUMAN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();
    }

    private PowerStatsModelApiOut createAnotherPowerStatsModelApiOut(HeroModelApiOut heroModelApiOut) {
        return PowerStatsModelApiOut.builder()
                .id(heroModelApiOut.getPowerStatsId())
                .agility(1)
                .dexterity(3)
                .intelligence(5)
                .strength(7)
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(heroModelApiOut.getUpdatedAt())
                .build();
    }

    private HeroModelApiOut createAnotherHeroModelApiOut() {
        return HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name("Spider")
                .race("ALIEN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();
    }

    private void cleanStorage() {
        List<HeroModelApiOut> heroes = new ArrayList<>();
        List<PowerStatsModelApiOut> powerStats = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : HeroRepositoryInMemoryImpl.heroStorage.entrySet())
            heroes.add(entry.getValue());

        heroes.forEach(h -> PowerStatsRepositoryInMemoryImpl.powerStatsStorage.remove(h.getId()));
    }
}