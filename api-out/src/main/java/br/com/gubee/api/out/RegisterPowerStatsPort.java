package br.com.gubee.api.out;

import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;

import java.util.UUID;

public interface RegisterPowerStatsPort {
    UUID registerPowerStats(RegisterPowerStatsRequest registerPowerStatsRequest);
}
