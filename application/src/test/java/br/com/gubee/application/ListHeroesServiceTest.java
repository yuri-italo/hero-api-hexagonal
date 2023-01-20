package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.ListHeroesPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ListHeroesServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final ListHeroesPort listHeroesPort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final ListHeroesService listHeroesService = new ListHeroesService(listHeroesPort,getPowerStatsByIdPort);

    @Test
    void findAllSucceeds() {
        // given
        RegisterPowerStatsRequest powerStatsRequest = createRegisterPowerStatsRequest();
        RegisterPowerStatsRequest powerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(powerStatsRequest);
        UUID powerStatsId2 = registerPowerStatsPort.registerPowerStats(powerStatsRequest2);

        RegisterHeroRequest heroRequest = createRegisterHeroRequest();
        RegisterHeroRequest heroRequest2 = createAnotherRegisterHeroRequest();
        UUID heroId = registerHeroPort.registerHero(heroRequest, powerStatsId);
        UUID heroId2 = registerHeroPort.registerHero(heroRequest2, powerStatsId2);

        // when
        List<HeroModelApiIn> allHeroes = listHeroesService.findAll();

        // then
        assertNotNull(allHeroes);
        List<UUID> uuids = allHeroes.stream().map(HeroModelApiIn::getId).toList();
        assertEquals(2,allHeroes.size());
        assertTrue(uuids.contains(heroId));
        assertTrue(uuids.contains(heroId2));
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