package br.com.gubee.application;

import br.com.gubee.api.out.DeleteHeroByIdPort;
import br.com.gubee.api.out.DeletePowerStatsByIdPort;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class DeleteHeroByIdServiceTest {
    private final GetHeroByIdPort getHeroByIdPort = mock(GetHeroByIdPort.class);
    private final DeleteHeroByIdPort deleteHeroByIdPort = mock(DeleteHeroByIdPort.class);
    private final DeletePowerStatsByIdPort deletePowerStatsByIdPort = mock(DeletePowerStatsByIdPort.class);
    private final DeleteHeroByIdService deleteHeroByIdService = new DeleteHeroByIdService(
            getHeroByIdPort,deleteHeroByIdPort,deletePowerStatsByIdPort
    );

    @Test
    void deleteSucceeds() {
        // given
        UUID uuid = UUID.randomUUID();
        HeroModelApiOut hero = givenExistingHeroId(uuid);

        // when
        deleteHeroByIdService.delete(hero.getId());

        // then
        HeroModelApiOut deletedHero = givenDeletedHeroId(uuid);
        assertNull(deletedHero);
    }

    @Test
    void deleteShouldNotSucceedsWhenIdNotExists() {
        // given
        UUID uuid = UUID.randomUUID();
        givenNotExistingHeroId(uuid);

        // when
        HeroIdNotFoundException e = assertThrows(
                HeroIdNotFoundException.class, () -> deleteHeroByIdService.delete(uuid)
        );

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

    private HeroModelApiOut givenDeletedHeroId(UUID heroId) {
        when(getHeroByIdPort.findById(heroId)).thenReturn(Optional.empty());
        return null;
    }
}