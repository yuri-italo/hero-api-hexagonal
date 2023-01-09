package br.com.gubee.application;

import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class RegisterHeroServiceTest {
    private final RegisterHeroPort registerHeroPort = new HeroRepositoryInMemoryImpl();
    private final RegisterPowerStatsPort registerPowerStatsPort = new PowerStatsRepositoryInMemoryImpl();
    private final GetHeroByIdPort getHeroByIdPort= new HeroRepositoryInMemoryImpl();
    private final RegisterHeroService registerHeroService = new RegisterHeroService(
            registerHeroPort,registerPowerStatsPort
    );

    @AfterEach
    void setUp() {
        cleanStorage();
    }

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

    private void cleanStorage() {
        List<HeroModelApiOut> heroes = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : HeroRepositoryInMemoryImpl.heroStorage.entrySet())
            heroes.add(entry.getValue());

        heroes.forEach(h -> PowerStatsRepositoryInMemoryImpl.powerStatsStorage.remove(h.getId()));
    }
}