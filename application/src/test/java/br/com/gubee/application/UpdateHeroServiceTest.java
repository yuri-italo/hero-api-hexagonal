package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.UpdateHeroPort;
import br.com.gubee.api.out.UpdatePowerStatsPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class UpdateHeroServiceTest {
    private final GetHeroByIdPort getHeroByIdPort = Mockito.mock(GetHeroByIdPort.class);
    private final UpdateHeroPort updateHeroPort = Mockito.mock(UpdateHeroPort.class);
    private final GetPowerStatsByIdPort getPowerStatsByIdPort = Mockito.mock(GetPowerStatsByIdPort.class);
    private final UpdatePowerStatsPort updatePowerStatsPort = Mockito.mock(UpdatePowerStatsPort.class);
    private final UpdateHeroService updateHeroService = new UpdateHeroService(
            getHeroByIdPort,updateHeroPort,getPowerStatsByIdPort,updatePowerStatsPort
    );

    @Test
    void updateSucceeds() {
        // given
        UUID uuid = UUID.randomUUID();
        HeroModelApiOut hero = givenExistingHeroId(uuid);
        PowerStatsModelApiOut powerStats = givenPowerStats(hero.getPowerStatsId());
        UpdateHeroRequest updateHeroRequest = givenUpdateHeroRequest();
        givenUpdatedHero(updateHeroRequest,hero,powerStats);

        // when
        HeroModelApiIn updatedHero = updateHeroService.update(uuid, updateHeroRequest);

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
        givenNotExistingHeroId(uuid);
        UpdateHeroRequest updateHeroRequest = givenUpdateHeroRequest();

        // when
        HeroIdNotFoundException e = assertThrows(
                HeroIdNotFoundException.class, () -> updateHeroService.update(uuid, updateHeroRequest)
        );

        // then
        assertNotNull(e);
        assertEquals(e.getMessage(),String.format("Hero ID not found: %s", uuid));
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

    private PowerStatsModelApiOut givenPowerStats(UUID powerStatsId) {
        PowerStatsModelApiOut powerStatsModelApiOut = PowerStatsModelApiOut.builder()
                .id(powerStatsId)
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .build();

        when(getPowerStatsByIdPort.findById(powerStatsId)).thenReturn(powerStatsModelApiOut);

        return powerStatsModelApiOut;
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

    private void givenUpdatedHero(
            UpdateHeroRequest updateHeroRequest, HeroModelApiOut hero, PowerStatsModelApiOut powerStats
    ) {
        HeroModelApiOut heroModelApiOut = HeroModelApiOut.builder()
                .id(hero.getId())
                .name(updateHeroRequest.getName())
                .race(updateHeroRequest.getRace())
                .powerStatsId(hero.getPowerStatsId())
                .enabled(updateHeroRequest.getEnabled())
                .updatedAt(hero.getUpdatedAt())
                .createdAt(hero.getCreatedAt())
                .build();

        when(getHeroByIdPort.findById(hero.getId())).thenReturn(Optional.of(heroModelApiOut));

        PowerStatsModelApiOut powerStatsModelApiOut = PowerStatsModelApiOut.builder()
                .id(powerStats.getId())
                .agility(updateHeroRequest.getAgility())
                .dexterity(updateHeroRequest.getDexterity())
                .intelligence(updateHeroRequest.getIntelligence())
                .strength(updateHeroRequest.getStrength())
                .createdAt(powerStats.getCreatedAt())
                .updatedAt(powerStats.getUpdatedAt())
                .build();

        when(getPowerStatsByIdPort.findById(powerStats.getId())).thenReturn(powerStatsModelApiOut);
    }
}