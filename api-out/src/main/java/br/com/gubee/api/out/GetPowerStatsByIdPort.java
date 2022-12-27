package br.com.gubee.api.out;

import br.com.gubee.api.out.model.PowerStatsModelApiOut;

import java.util.UUID;

public interface GetPowerStatsByIdPort {
    PowerStatsModelApiOut findById(UUID uuid);
}
