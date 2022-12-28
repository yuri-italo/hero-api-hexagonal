package br.com.gubee.api.out;

import br.com.gubee.api.out.requests.UpdatePowerStatsRequestApiOut;

import java.util.UUID;

public interface UpdatePowerStatsPort {
    void update(UUID id, UpdatePowerStatsRequestApiOut updatePowerStatsRequestApiOut);
}
