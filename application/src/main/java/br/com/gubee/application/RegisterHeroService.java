package br.com.gubee.application;

import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.configuration.Hero;
import br.com.gubee.configuration.PowerStats;

import java.util.UUID;

public class RegisterHeroService implements RegisterHeroUseCase {
    private final RegisterHeroPort registerHeroPort;
    private final RegisterPowerStatsPort registerPowerStatsPort;

    public RegisterHeroService(RegisterHeroPort registerHeroPort, RegisterPowerStatsPort registerPowerStatsPort) {
        this.registerHeroPort = registerHeroPort;
        this.registerPowerStatsPort = registerPowerStatsPort;
    }

    @Override
    public UUID registerHero(CreateHeroRequest createHeroRequest) {
        PowerStats powerStats = createPowerStats(createHeroRequest);
        UUID uuid = registerPowerStatsPort.registerPowerStats(createRegisterPowerStatsRequest(powerStats));

        Hero hero = new Hero(createHeroRequest.getName(),createHeroRequest.getRace(),uuid);

        return registerHeroPort.registerHero(createRegisterHeroRequest(hero),uuid);
    }

    private RegisterHeroRequest createRegisterHeroRequest(Hero hero) {
        return RegisterHeroRequest.builder()
                .name(hero.getName())
                .race(hero.getRace().toString())
                .build();
    }

    private RegisterPowerStatsRequest createRegisterPowerStatsRequest(PowerStats powerStats) {
        return RegisterPowerStatsRequest.builder()
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .build();
    }

    private PowerStats createPowerStats(CreateHeroRequest createHeroRequest) {
        return new PowerStats(
                createHeroRequest.getAgility(),
                createHeroRequest.getDexterity(),
                createHeroRequest.getIntelligence(),
                createHeroRequest.getStrength()
        );
    }
}
