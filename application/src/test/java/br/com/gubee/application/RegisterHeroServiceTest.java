package br.com.gubee.application;

import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegisterHeroServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final GetHeroByIdPort getHeroByIdPort = heroRepositoryInMemory;
    private final RegisterHeroService registerHeroService = new RegisterHeroService(
            registerHeroPort,registerPowerStatsPort
    );

    @Test
    void registerHeroSucceeds() {
        // given
        CreateHeroRequest request = givenHeroRequest();

        // when
        UUID uuid = registerHeroService.registerHero(request);

        // then
        assertNotNull(uuid);
        assertTrue(getHeroByIdPort.findById(uuid).isPresent());

        HeroModelApiOut createdHero = getHeroByIdPort.findById(uuid).get();
        assertEquals(request.getName(),createdHero.getName());
        assertEquals(request.getRace(),createdHero.getRace());
        assertTrue(createdHero.isEnabled());
        assertNotNull(createdHero.getCreatedAt());
        assertNotNull(createdHero.getUpdatedAt());
    }

    @Test
    void registerHeroShouldNotSucceedsWhenHeroNameExists() {
        // given
        CreateHeroRequest request = givenHeroRequest();
        registerHeroService.registerHero(request);


        // when
        IllegalArgumentException e = assertThrows(
                IllegalArgumentException.class, () -> registerHeroService.registerHero(request)
        );

        // then
        assertNotNull(e);
    }

    private CreateHeroRequest givenHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Super Shock")
                .race("HUMAN")
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }
}