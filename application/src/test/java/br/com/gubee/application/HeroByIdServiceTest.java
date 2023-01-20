package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class HeroByIdServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final GetHeroByIdPort getHeroByIdPort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final HeroByIdService heroByIdService = new HeroByIdService(getHeroByIdPort,getPowerStatsByIdPort);

    @Test
    void findByIdSucceeds() {
        // given
        RegisterPowerStatsRequest powerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest heroRequest = createRegisterHeroRequest();

        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(powerStatsRequest);
        UUID heroId = registerHeroPort.registerHero(heroRequest,powerStatsId);

        // when
        HeroModelApiIn heroById = heroByIdService.findById(heroId);

        // then
        assertNotNull(heroById);
        assertEquals(heroId,heroById.getId());
        assertEquals(heroRequest.getName(),heroById.getName());
        assertEquals(heroRequest.getRace(),heroById.getRace());
        assertNotNull(heroById.getCreatedAt());
        assertNotNull(heroById.getUpdatedAt());
        assertEquals(powerStatsRequest.getAgility(),heroById.getAgility());
        assertEquals(powerStatsRequest.getDexterity(),heroById.getDexterity());
        assertEquals(powerStatsRequest.getIntelligence(),heroById.getIntelligence());
        assertEquals(powerStatsRequest.getStrength(),heroById.getStrength());
    }

    @Test
    void findByIdShouldNotSucceedsWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();

        // when
        HeroIdNotFoundException e = assertThrows(HeroIdNotFoundException.class, () -> heroByIdService.findById(uuid));

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