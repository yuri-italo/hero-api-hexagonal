package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.UpdateHeroPort;
import br.com.gubee.api.out.UpdatePowerStatsPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateHeroServiceTest {
    private final GetHeroByIdPort getHeroByIdPort = new HeroRepositoryInMemoryImpl();
    private final UpdateHeroPort updateHeroPort = new HeroRepositoryInMemoryImpl();
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = new PowerStatsRepositoryInMemoryImpl();
    private final UpdatePowerStatsPort updatePowerStatsPort = new PowerStatsRepositoryInMemoryImpl();
    private final UpdateHeroService updateHeroService = new UpdateHeroService(
            getHeroByIdPort,updateHeroPort,getPowerStatsByIdPort,updatePowerStatsPort
    );

    @AfterEach
    void setUp() {
        cleanStorage();
    }

    @Test
    void updateSucceeds() {
        // given
        HeroModelApiOut heroModelApiOut = createHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut.getId(),heroModelApiOut);
        PowerStatsModelApiOut powerStatsModelApiOut = createPowerStatsModelApiOut(heroModelApiOut);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut.getId(),powerStatsModelApiOut);

        UpdateHeroRequest updateHeroRequest = givenUpdateHeroRequest();

        // when
        HeroModelApiIn updatedHero = updateHeroService.update(heroModelApiOut.getId(), updateHeroRequest);

        // then
        assertNotNull(updatedHero);
        assertEquals(updateHeroRequest.getName(),updatedHero.getName());
        assertEquals(updateHeroRequest.getRace(),updatedHero.getRace());
        assertEquals(updateHeroRequest.getEnabled(),updatedHero.isEnabled());
        assertEquals(updateHeroRequest.getAgility(),updatedHero.getAgility());
        assertEquals(updateHeroRequest.getDexterity(),updatedHero.getDexterity());
        assertEquals(updateHeroRequest.getIntelligence(),updatedHero.getIntelligence());
        assertEquals(updateHeroRequest.getStrength(),updatedHero.getStrength());
        assertNotNull(updatedHero.getCreatedAt());
        assertNotNull(updatedHero.getCreatedAt());
    }

    @Test
    void updateShouldNotSucceedsWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();
        UpdateHeroRequest updateHeroRequest = givenUpdateHeroRequest();

        // when
        HeroIdNotFoundException e = assertThrows(
                HeroIdNotFoundException.class, () -> updateHeroService.update(uuid, updateHeroRequest)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),String.format("Hero ID not found: %s", uuid));
    }

    private UpdateHeroRequest givenUpdateHeroRequest() {

        return UpdateHeroRequest.builder()
                .name("Spider-Man")
                .race("ALIEN")
                .agility(5)
                .dexterity(5)
                .intelligence(5)
                .strength(5)
                .enabled(false)
                .build();
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

    private void cleanStorage() {
        List<HeroModelApiOut> heroes = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : HeroRepositoryInMemoryImpl.heroStorage.entrySet())
            heroes.add(entry.getValue());

        heroes.forEach(h -> PowerStatsRepositoryInMemoryImpl.powerStatsStorage.remove(h.getId()));
    }
}