package br.com.gubee.application;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.application.impl.HeroRepositoryInMemoryImpl;
import br.com.gubee.application.impl.PowerStatsRepositoryInMemoryImpl;
import br.com.gubee.configuration.exception.HeroNameNotFoundException;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class CompareHeroServiceTest {
    HeroRepositoryInMemoryImpl heroRepositoryInMemory = new HeroRepositoryInMemoryImpl();
    PowerStatsRepositoryInMemoryImpl powerStatsRepositoryInMemory = new PowerStatsRepositoryInMemoryImpl();
    private final FindHeroByNamePort findHeroByNamePort = heroRepositoryInMemory;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = powerStatsRepositoryInMemory;
    private final RegisterHeroPort registerHeroPort = heroRepositoryInMemory;
    private final RegisterPowerStatsPort registerPowerStatsPort = powerStatsRepositoryInMemory;
    private final CompareHeroService compareHeroService = new CompareHeroService(
            findHeroByNamePort,getPowerStatsByIdPort
    );

    @Test
    void compareSucceeds() {
        // given
        String heroName = "batman";
        String heroName2 = "spider";

        RegisterPowerStatsRequest powerStatsRequest = createRegisterPowerStatsRequest();
        RegisterPowerStatsRequest powerStatsRequest2 = createAnotherRegisterPowerStatsRequest();
        UUID powerStatsId = registerPowerStatsPort.registerPowerStats(powerStatsRequest);
        UUID powerStatsId2 = registerPowerStatsPort.registerPowerStats(powerStatsRequest2);

        RegisterHeroRequest heroRequest = createRegisterHeroRequest();
        RegisterHeroRequest heroRequest2 = createAnotherRegisterHeroRequest();
        UUID heroId = registerHeroPort.registerHero(heroRequest, powerStatsId);
        UUID heroId2 = registerHeroPort.registerHero(heroRequest2, powerStatsId2);

        // when
        CompareHeroApiIn comparedHeroes = compareHeroService.compare(heroName, heroName2);

        // then
        assertNotNull(comparedHeroes);
        assertNotNull(comparedHeroes.getHeroModelApiIn());
        assertNotNull(comparedHeroes.getHeroModelApiIn2());

        HeroModelApiIn heroModelApiIn = comparedHeroes.getHeroModelApiIn();
        HeroModelApiIn heroModelApiIn2 = comparedHeroes.getHeroModelApiIn2();

        assertEquals(heroId,heroModelApiIn.getId());
        assertEquals(heroRequest.getName(),heroModelApiIn.getName());
        assertEquals(heroRequest.getRace(),heroModelApiIn.getRace());
        assertNotNull(heroModelApiIn.getCreatedAt());
        assertNotNull(heroModelApiIn.getUpdatedAt());
        assertEquals(powerStatsRequest.getAgility() - powerStatsRequest2.getAgility(),heroModelApiIn.getAgility());
        assertEquals(powerStatsRequest.getDexterity() - powerStatsRequest2.getDexterity(),heroModelApiIn.getDexterity());
        assertEquals(powerStatsRequest.getIntelligence() - powerStatsRequest2.getIntelligence(),heroModelApiIn.getIntelligence());
        assertEquals(powerStatsRequest.getStrength() - powerStatsRequest2.getStrength(),heroModelApiIn.getStrength());

        assertEquals(heroId2,heroModelApiIn2.getId());
        assertEquals(heroRequest2.getName(),heroModelApiIn2.getName());
        assertEquals(heroRequest2.getRace(),heroModelApiIn2.getRace());
        assertNotNull(heroModelApiIn2.getCreatedAt());
        assertNotNull(heroModelApiIn2.getUpdatedAt());
        assertEquals(powerStatsRequest2.getAgility() - powerStatsRequest.getAgility(),heroModelApiIn2.getAgility());
        assertEquals(powerStatsRequest2.getDexterity() - powerStatsRequest.getDexterity(),heroModelApiIn2.getDexterity());
        assertEquals(powerStatsRequest2.getIntelligence() - powerStatsRequest.getIntelligence(),heroModelApiIn2.getIntelligence());
        assertEquals(powerStatsRequest2.getStrength() - powerStatsRequest.getStrength(),heroModelApiIn2.getStrength());
    }

    @Test
    void compareShouldNotSucceedsWhenHeroNameNotExists() {
        // given
        String heroName = "Superman";
        String heroName2 = "Jessica";

        // when
        HeroNameNotFoundException e = assertThrows(
                HeroNameNotFoundException.class, () -> compareHeroService.compare(heroName, heroName2)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Name \"" + heroName + "\" was not found.");
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
                .name("Spider")
                .race("ALIEN")
                .build();
    }
}