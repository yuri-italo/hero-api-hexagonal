package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetHeroesByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeroesByNameServiceTest {
    private final GetHeroesByNamePort getHeroesByNamePort = mock(GetHeroesByNamePort.class);
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = mock(GetPowerStatsByIdPort.class);
    private final HeroesByNameService heroesByNameService = new HeroesByNameService(
            getHeroesByNamePort,getPowerStatsByIdPort
    );

    @Test
    void findManyByNameSucceeds() {
        // given
        String search = "man";
        List<HeroModelApiOut> heroes = givenExistingSearch(search);
        givenHeroList(heroes);

        // when
        List<HeroModelApiIn> foundHeroes = heroesByNameService.findManyByName(search);

        // then
        assertNotNull(foundHeroes);
        assertEquals(heroes.size(),foundHeroes.size());
        assertTrue(foundHeroes.get(0).getName().contains(search));
        assertTrue(foundHeroes.get(1).getName().contains(search));
    }

    @Test
    void findManyByNameShouldNotSucceedsWhenSearchNotExist() {
        // given
        String search = "man";
        givenNotExistingSearch(search);

        // when
        List<HeroModelApiIn> foundHeroes = heroesByNameService.findManyByName(search);

        // then
        assertNotNull(foundHeroes);
        assertEquals(0,foundHeroes.size());
    }

    private List<HeroModelApiOut> givenExistingSearch(String search) {
        HeroModelApiOut heroModelApiOut = HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name("Batman")
                .race("HUMAN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();


        HeroModelApiOut heroModelApiOut2 = HeroModelApiOut.builder()
                .id(UUID.randomUUID())
                .name("Superman")
                .race("HUMAN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();

        List<HeroModelApiOut> heroes = List.of(heroModelApiOut,heroModelApiOut2);

        when(getHeroesByNamePort.findManyByName(search)).thenReturn(heroes);

        return heroes;
    }

    private void givenNotExistingSearch(String search) {
        List<HeroModelApiOut> heroes = new ArrayList<>();
        when(getHeroesByNamePort.findManyByName(search)).thenReturn(heroes);
    }

    private void givenHeroList(List<HeroModelApiOut> heroes) {
        PowerStatsModelApiOut powerStats;

        for (var hero : heroes) {
            powerStats = PowerStatsModelApiOut.builder()
                    .id(hero.getPowerStatsId())
                    .strength(10)
                    .agility(10)
                    .dexterity(10)
                    .intelligence(10)
                    .createdAt(Instant.now())
                    .updatedAt(Instant.now())
                    .build();

            when(getPowerStatsByIdPort.findById(powerStats.getId())).thenReturn(powerStats);
        }
    }
}