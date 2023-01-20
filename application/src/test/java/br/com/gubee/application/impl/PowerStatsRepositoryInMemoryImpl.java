package br.com.gubee.application.impl;


import br.com.gubee.api.out.DeletePowerStatsByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.api.out.UpdatePowerStatsPort;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.api.out.requests.UpdatePowerStatsRequestApiOut;
import br.com.gubee.configuration.PowerStats;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class PowerStatsRepositoryInMemoryImpl implements
        RegisterPowerStatsPort, GetPowerStatsByIdPort, UpdatePowerStatsPort, DeletePowerStatsByIdPort {
    private final Map<UUID, PowerStatsModelApiOut> powerStatsStorage = new HashMap<>();

    @Override
    public UUID registerPowerStats(RegisterPowerStatsRequest request) {
        PowerStatsModelApiOut powerStatsModelApiOut = createPowerStatsApiOut(request);
        UUID id = powerStatsModelApiOut.getId();

        powerStatsStorage.put(id,powerStatsModelApiOut);

        return id;
    }

    private PowerStatsModelApiOut createPowerStatsApiOut(RegisterPowerStatsRequest request) {
        PowerStats powerStats = new PowerStats(
                request.getStrength(),request.getAgility(),request.getDexterity(),request.getIntelligence()
        );

        Instant now = Instant.now();
        powerStats.setId(UUID.randomUUID());
        powerStats.setCreatedAt(now);
        powerStats.setUpdatedAt(now);

        return PowerStatsModelApiOut.builder()
                .id(powerStats.getId())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .createdAt(powerStats.getCreatedAt())
                .updatedAt(powerStats.getUpdatedAt())
                .build();
    }

    @Override
    public PowerStatsModelApiOut findById(UUID powerStatsId) {
        return powerStatsStorage.get(powerStatsId);
    }

    @Override
    public void update(UUID id, UpdatePowerStatsRequestApiOut request) {
        PowerStatsModelApiOut powerStatsModelApiOut = powerStatsStorage.get(id);

        PowerStatsModelApiOut modifiedPowerStats = PowerStatsModelApiOut.builder()
                .id(id)
                .agility(request.getAgility())
                .dexterity(request.getDexterity())
                .intelligence(request.getIntelligence())
                .strength(request.getStrength())
                .createdAt(powerStatsModelApiOut.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        powerStatsStorage.put(id,modifiedPowerStats);
    }

    @Override
    public void delete(UUID powerStatsId) {
        powerStatsStorage.remove(powerStatsId);
    }

}
