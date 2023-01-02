package br.com.gubee.application;

import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.configuration.PowerStats;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RegisterHeroServiceTest {
    private final RegisterHeroPort registerHeroPort = mock(RegisterHeroPort.class);
    private final RegisterPowerStatsPort registerPowerStatsPort = mock(RegisterPowerStatsPort.class);
    private final RegisterHeroService registerHeroService = new RegisterHeroService(
            registerHeroPort,registerPowerStatsPort
    );
    @Test
    void registerHeroSucceeds() {
        // given
        CreateHeroRequest request = givenHeroRequest();
        PowerStats powerStats = givenPowerStats(request);

        UUID heroId = givenHero(request, powerStats.getId());

        // when
        UUID uuid = registerHeroService.registerHero(request);

        // then
        Assertions.assertNotNull(uuid);
        Assertions.assertEquals(heroId,uuid);
    }

    private PowerStats givenPowerStats(CreateHeroRequest createHeroRequest) {
        PowerStats powerStats = new PowerStats(
                UUID.randomUUID(),
                createHeroRequest.getStrength(),
                createHeroRequest.getAgility(),
                createHeroRequest.getDexterity(),
                createHeroRequest.getIntelligence(),
                Instant.now(),
                Instant.now()
        );

        RegisterPowerStatsRequest request = RegisterPowerStatsRequest.builder()
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .build();

        when(registerPowerStatsPort.registerPowerStats(request)).thenReturn(powerStats.getId());

        return powerStats;
    }

    private UUID givenHero(CreateHeroRequest createHeroRequest, UUID powerStatsId) {
        UUID uuid = UUID.randomUUID();

        RegisterHeroRequest request = RegisterHeroRequest.builder()
                .name(createHeroRequest.getName())
                .race(createHeroRequest.getRace())
                .build();

        when(registerHeroPort.registerHero(request,powerStatsId)).thenReturn(uuid);

        return uuid;
    }

    private CreateHeroRequest givenHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }
}