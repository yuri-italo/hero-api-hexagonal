package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.ListHeroesPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class ListHeroesServiceTest {
    private final ListHeroesPort listHeroesPort = Mockito.mock(ListHeroesPort.class);
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = Mockito.mock(GetPowerStatsByIdPort.class);
    private final ListHeroesService listHeroesService = new ListHeroesService(listHeroesPort,getPowerStatsByIdPort);

    @Test
    void findAllSucceeds() {
        // given
        List<HeroModelApiOut> heroes = givenExistingHeroes();
        givenHeroList(heroes);

        // when
        List<HeroModelApiIn> allHeroes = listHeroesService.findAll();

        // then
        assertNotNull(allHeroes);
        assertEquals(heroes.size(),allHeroes.size());
        assertEquals(heroes.get(0).getId(),allHeroes.get(0).getId());
        assertEquals(heroes.get(1).getId(),allHeroes.get(1).getId());
    }

    @Test
    void findAllReturnEmptyListWhenNoHeroRegistered() {
        // given
        givenNoHeroes();

        // when
        List<HeroModelApiIn> allHeroes = listHeroesService.findAll();

        // then
        assertNotNull(allHeroes);
        assertEquals(0,allHeroes.size());
    }

    private List<HeroModelApiOut> givenExistingHeroes() {
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

        when(listHeroesPort.findAll()).thenReturn(heroes);

        return heroes;
    }

    private void givenNoHeroes() {
        List<HeroModelApiOut> heroes = new ArrayList<>();
        when(listHeroesPort.findAll()).thenReturn(heroes);
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