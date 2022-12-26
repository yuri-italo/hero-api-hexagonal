package br.com.gubee.application;

import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.out.RegisterPowerStatsPort;

import java.util.UUID;

public class RegisterPowerStatsService {
    private final RegisterPowerStatsPort registerPowerStatsPort;

    public RegisterPowerStatsService(RegisterPowerStatsPort registerPowerStatsPort) {
        this.registerPowerStatsPort = registerPowerStatsPort;
    }

    public UUID registerPowerStats(CreateHeroRequest createHeroRequest) {

        return null;
    }


}
