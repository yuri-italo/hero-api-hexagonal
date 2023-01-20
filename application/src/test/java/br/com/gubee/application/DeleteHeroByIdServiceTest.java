package br.com.gubee.application;

import br.com.gubee.api.out.*;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class DeleteHeroByIdServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final GetHeroByIdPort getHeroByIdPort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort= powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final DeleteHeroByIdPort deleteHeroByIdPort = heroRepositoryInMemory;
    private final DeletePowerStatsByIdPort deletePowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final DeleteHeroByIdService deleteHeroByIdService = new DeleteHeroByIdService(
            getHeroByIdPort,deleteHeroByIdPort,deletePowerStatsByIdPort
    );

    @Test
    void deleteSucceeds() {
        // given
        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(createRegisterPowerStatsRequest());
        UUID heroId = registerHeroPort.registerHero(createRegisterHeroRequest(), powerStatsId);

        // when
        deleteHeroByIdService.delete(heroId);

        // then
        assertTrue(getHeroByIdPort.findById(heroId).isEmpty());
        assertNull(getPowerStatsByIdPort.findById(powerStatsId));
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

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(1)
                .dexterity(3)
                .intelligence(5)
                .strength(7)
                .build();
    }

    private RegisterHeroRequest createRegisterHeroRequest() {
        return RegisterHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .build();
    }
}