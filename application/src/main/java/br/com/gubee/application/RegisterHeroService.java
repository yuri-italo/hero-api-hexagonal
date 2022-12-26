package br.com.gubee.application;

import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.configuration.Hero;

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
        RegisterPowerStatsRequest powerStatsRequest = getRegisterPowerStatsRequest(createHeroRequest);
        UUID uuid = registerPowerStatsPort.registerPowerStats(powerStatsRequest);

        Hero hero = new Hero(createHeroRequest.getName(),createHeroRequest.getRace(),uuid);
        RegisterHeroRequest registerHeroRequest = getRegisterHeroRequest(hero);

        return registerHeroPort.registerHero(registerHeroRequest,uuid);
    }

    private RegisterHeroRequest getRegisterHeroRequest(Hero hero) {
        return RegisterHeroRequest.builder()
                .name(hero.getName())
                .race(hero.getRace().toString())
                .build();
    }

    private RegisterPowerStatsRequest getRegisterPowerStatsRequest(CreateHeroRequest createHeroRequest) {
        return RegisterPowerStatsRequest.builder()
                .agility(createHeroRequest.getAgility())
                .dexterity(createHeroRequest.getDexterity())
                .intelligence(createHeroRequest.getIntelligence())
                .strength(createHeroRequest.getStrength())
                .build();
    }
}
