package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.api.out.*;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UpdateHeroServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final GetHeroByIdPort getHeroByIdPort = heroRepositoryInMemory;
    private final UpdateHeroPort updateHeroPort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final UpdatePowerStatsPort updatePowerStatsPort = powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final UpdateHeroService updateHeroService = new UpdateHeroService(
            getHeroByIdPort,updateHeroPort,getPowerStatsByIdPort,updatePowerStatsPort
    );

    @Test
    void updateSucceeds() {
        // given
        RegisterPowerStatsRequest powerStatsRequest = createRegisterPowerStatsRequest();
        RegisterHeroRequest heroRequest = createRegisterHeroRequest();

        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(powerStatsRequest);
        UUID heroId = registerHeroPort.registerHero(heroRequest,powerStatsId);

        UpdateHeroRequest updateHeroRequest = givenUpdateHeroRequest();

        // when
        HeroModelApiIn updatedHero = updateHeroService.update(heroId, updateHeroRequest);

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

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private RegisterHeroRequest createRegisterHeroRequest() {
        return RegisterHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .build();
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
}