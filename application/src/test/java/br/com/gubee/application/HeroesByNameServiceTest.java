package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetHeroesByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HeroesByNameServiceTest {
    private final GetHeroesByNamePort getHeroesByNamePort = new HeroRepositoryInMemoryImpl();
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = new PowerStatsRepositoryInMemoryImpl();
    private final HeroesByNameService heroesByNameService = new HeroesByNameService(
            getHeroesByNamePort,getPowerStatsByIdPort
    );

    @AfterEach
    void setUp() {
        cleanStorage();
    }

    @Test
    void findManyByNameSucceeds() {
        // given
        String search = "man";
        HeroModelApiOut heroModelApiOut = createHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut.getId(),heroModelApiOut);
        PowerStatsModelApiOut powerStatsModelApiOut = createPowerStatsModelApiOut(heroModelApiOut);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut.getId(),powerStatsModelApiOut);

        HeroModelApiOut heroModelApiOut2 = createAnotherHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut2.getId(),heroModelApiOut2);
        PowerStatsModelApiOut powerStatsModelApiOut2 = createAnotherPowerStatsModelApiOut(heroModelApiOut2);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut2.getId(),powerStatsModelApiOut2);

        // when
        List<HeroModelApiIn> foundHeroes = heroesByNameService.findManyByName(search);

        // then
        assertNotNull(foundHeroes);
        assertTrue(foundHeroes.get(0).getName().contains(search));
        assertTrue(foundHeroes.get(1).getName().contains(search));
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
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(heroModelApiOut.getUpdatedAt())
                .build();
    }

    private HeroModelApiOut createAnotherHeroModelApiOut() {
        return HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name("Superman")
                .race("DIVINE")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();
    }

    private void cleanStorage() {
        List<HeroModelApiOut> heroes = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : HeroRepositoryInMemoryImpl.heroStorage.entrySet())
            heroes.add(entry.getValue());

        heroes.forEach(h -> PowerStatsRepositoryInMemoryImpl.powerStatsStorage.remove(h.getId()));
    }
}