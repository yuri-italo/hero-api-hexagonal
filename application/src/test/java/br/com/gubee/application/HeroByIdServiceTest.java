package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class HeroByIdServiceTest {
    private final GetHeroByIdPort getHeroByIdPort = mock(GetHeroByIdPort.class);
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = mock(GetPowerStatsByIdPort.class);
    private final HeroByIdService heroByIdService = new HeroByIdService(getHeroByIdPort,getPowerStatsByIdPort);

    @Test
    void findByIdSucceeds() {
        // given
        UUID uuid = UUID.randomUUID();
        HeroModelApiOut hero = givenExistingHeroId(uuid);
        PowerStatsModelApiOut powerStats = givenExistingPowerStatsId(hero.getPowerStatsId());

        // when
        HeroModelApiIn heroById = heroByIdService.findById(uuid);

        // then
        assertNotNull(heroById);
        assertEquals(hero.getId(),heroById.getId());
        assertEquals(hero.getName(),heroById.getName());
        assertEquals(hero.getRace(),heroById.getRace());
        assertEquals(hero.getCreatedAt(),heroById.getCreatedAt());
        assertEquals(hero.getCreatedAt(),heroById.getCreatedAt());
        assertEquals(hero.isEnabled(),heroById.isEnabled());
        assertEquals(powerStats.getAgility(),heroById.getAgility());
        assertEquals(powerStats.getDexterity(),heroById.getDexterity());
        assertEquals(powerStats.getIntelligence(),heroById.getIntelligence());
        assertEquals(powerStats.getStrength(),heroById.getStrength());
    }

    @Test
    void findByIdShouldNotSucceedsWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();
        givenNotExistingHeroId(uuid);

        // when
        HeroIdNotFoundException e = assertThrows(HeroIdNotFoundException.class, () -> heroByIdService.findById(uuid));

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),"Hero ID not found: " + uuid);
    }

    private HeroModelApiOut givenExistingHeroId(UUID uuid) {
        HeroModelApiOut heroModelApiOut = HeroModelApiOut.builder()
                .id(uuid)
                .name("Batman")
                .race("HUMAN")
                .powerStatsId(UUID.randomUUID())
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .enabled(true)
                .build();

        when(getHeroByIdPort.findById(uuid)).thenReturn(Optional.of(heroModelApiOut));

        return heroModelApiOut;
    }

    private void givenNotExistingHeroId(UUID uuid) {
        when(getHeroByIdPort.findById(uuid)).thenReturn(Optional.empty());
    }

    private PowerStatsModelApiOut givenExistingPowerStatsId(UUID powerStatsId) {
        PowerStatsModelApiOut powerStatsModelApiOut = PowerStatsModelApiOut.builder()
                .id(powerStatsId)
                .strength(1)
                .agility(5)
                .dexterity(10)
                .intelligence(3)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(getPowerStatsByIdPort.findById(powerStatsId)).thenReturn(powerStatsModelApiOut);

        return powerStatsModelApiOut;
    }
}