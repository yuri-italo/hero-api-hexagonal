package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetHeroesByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class HeroesByNameServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final GetHeroesByNamePort getHeroesByNamePort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final HeroesByNameService heroesByNameService = new HeroesByNameService(
            getHeroesByNamePort,getPowerStatsByIdPort
    );

    @Test
    void findManyByNameSucceeds() {
        // given
        String search = "man";
        RegisterPowerStatsRequest powerStatsRequest = createRegisterPowerStatsRequest();
        RegisterPowerStatsRequest powerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(powerStatsRequest);
        UUID powerStatsId2 = registerPowerStatsPort.registerPowerStats(powerStatsRequest2);

        RegisterHeroRequest heroRequest = createRegisterHeroRequest();
        RegisterHeroRequest heroRequest2 = createAnotherRegisterHeroRequest();
        registerHeroPort.registerHero(heroRequest, powerStatsId);
        registerHeroPort.registerHero(heroRequest2, powerStatsId2);

        // when
        List<HeroModelApiIn> foundHeroes = heroesByNameService.findManyByName(search);

        // then
        assertNotNull(foundHeroes);
        assertTrue(foundHeroes.get(0).getName().contains(search));
        assertTrue(foundHeroes.get(1).getName().contains(search));
    }

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest() {
        return RegisterPowerStatsRequest.builder()
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private RegisterPowerStatsRequest createAnotherRegisterPowerStatsRequest() {
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

    private RegisterHeroRequest createAnotherRegisterHeroRequest() {
        return RegisterHeroRequest.builder()
                .name("Superman")
                .race("DIVINE")
                .build();
    }
}