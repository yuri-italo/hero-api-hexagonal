package br.com.gubee.application;

import br.com.gubee.api.out.DeleteHeroByIdPort;
import br.com.gubee.api.out.DeletePowerStatsByIdPort;
import br.com.gubee.api.out.GetHeroByIdPort;
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

class DeleteHeroByIdServiceTest {
    private final GetHeroByIdPort getHeroByIdPort = new HeroRepositoryInMemoryImpl();
    private final DeleteHeroByIdPort deleteHeroByIdPort = new HeroRepositoryInMemoryImpl();
    private final DeletePowerStatsByIdPort deletePowerStatsByIdPort = new PowerStatsRepositoryInMemoryImpl();
    private final DeleteHeroByIdService deleteHeroByIdService = new DeleteHeroByIdService(
            getHeroByIdPort,deleteHeroByIdPort,deletePowerStatsByIdPort
    );

    @AfterEach
    void setUp() {
        cleanStorage();
    }

    @Test
    void deleteSucceeds() {
        // given
        HeroModelApiOut heroModelApiOut = createHeroModelApiOut();
        HeroRepositoryInMemoryImpl.heroStorage.put(heroModelApiOut.getId(),heroModelApiOut);
        PowerStatsModelApiOut powerStatsModelApiOut = createPowerStatsModelApiOut(heroModelApiOut);
        PowerStatsRepositoryInMemoryImpl.powerStatsStorage.put(powerStatsModelApiOut.getId(),powerStatsModelApiOut);

        // when
        deleteHeroByIdService.delete(heroModelApiOut.getId());

        // then
        assertNull(HeroRepositoryInMemoryImpl.heroStorage.get(heroModelApiOut.getId()));
        assertNull(PowerStatsRepositoryInMemoryImpl.powerStatsStorage.get(powerStatsModelApiOut.getId()));
    }

    @Test
    void deleteShouldNotSucceedsWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        HeroIdNotFoundException e = assertThrows(
                HeroIdNotFoundException.class, () -> deleteHeroByIdService.delete(uuid)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Hero ID not found: " + uuid);
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