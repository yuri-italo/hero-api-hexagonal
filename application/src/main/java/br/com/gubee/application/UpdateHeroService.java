package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.UpdateHeroUseCase;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.UpdateHeroPort;
import br.com.gubee.api.out.UpdatePowerStatsPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.api.out.requests.UpdateHeroRequestApiOut;
import br.com.gubee.api.out.requests.UpdatePowerStatsRequestApiOut;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;

import java.util.UUID;

public class UpdateHeroService implements UpdateHeroUseCase {
    private final GetHeroByIdPort getHeroByIdPort;
    private final UpdateHeroPort updateHeroPort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;
    private final UpdatePowerStatsPort updatePowerStatsPort;

    public UpdateHeroService(
            GetHeroByIdPort getHeroByIdPort, 
            UpdateHeroPort updateHeroPort, 
            GetPowerStatsByIdPort getPowerStatsByIdPort, 
            UpdatePowerStatsPort updatePowerStatsPort
    ) {
        this.getHeroByIdPort = getHeroByIdPort;
        this.updateHeroPort = updateHeroPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
        this.updatePowerStatsPort = updatePowerStatsPort;
    }

    @Override
    public HeroModelApiIn update(UUID heroId, UpdateHeroRequest updateHeroRequest) {
        HeroModelApiOut heroModelApiOut = findHeroOrFail(heroId);

        HeroModelApiOut updatedHeroModelApiOut = updateHero(heroId, updateHeroRequest, heroModelApiOut);
        updateHeroPowerStats(updateHeroRequest, updatedHeroModelApiOut);

        return createHeroModelApiIn(heroId);
    }

    private HeroModelApiOut updateHero(UUID heroId, UpdateHeroRequest updateHeroRequest, HeroModelApiOut heroModelApiOut) {
        updateHeroPort.update(heroId, createUpdateHeroRequestApiOut(heroModelApiOut, updateHeroRequest));
        return heroModelApiOut;
    }

    private void updateHeroPowerStats(UpdateHeroRequest updateHeroRequest, HeroModelApiOut heroModelApiOut) {
        PowerStatsModelApiOut powerStats = getPowerStatsByIdPort.findById(heroModelApiOut.getPowerStatsId());
        UpdatePowerStatsRequestApiOut updatePowerStatsRequestApiOut = createUpdatePowerStatsRequestApiOut(powerStats, updateHeroRequest);
        updatePowerStatsPort.update(powerStats.getId(), updatePowerStatsRequestApiOut);
    }

    private HeroModelApiIn createHeroModelApiIn(UUID heroId) {
        HeroModelApiOut heroModelApiOut = getHeroByIdPort.findById(heroId)
                .orElseThrow(() -> new HeroIdNotFoundException(heroId));

        PowerStatsModelApiOut powerStatsModelApiOut = getPowerStatsByIdPort.findById(heroModelApiOut.getPowerStatsId());

        return HeroModelApiIn.builder()
                .id(heroModelApiOut.getId())
                .name(heroModelApiOut.getName())
                .race(heroModelApiOut.getRace())
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(heroModelApiOut.getUpdatedAt())
                .enabled(heroModelApiOut.isEnabled())
                .agility(powerStatsModelApiOut.getAgility())
                .dexterity(powerStatsModelApiOut.getDexterity())
                .intelligence(powerStatsModelApiOut.getIntelligence())
                .strength(powerStatsModelApiOut.getStrength())
                .build();
    }

    private UpdateHeroRequestApiOut createUpdateHeroRequestApiOut(HeroModelApiOut heroModelApiOut, UpdateHeroRequest updateHeroRequest) {
        UpdateHeroRequestApiOut updateHeroRequestApiOut = new UpdateHeroRequestApiOut(heroModelApiOut);

        if (!(updateHeroRequest.getName() == null || updateHeroRequest.getName().equals("")))
            updateHeroRequestApiOut.setName(updateHeroRequest.getName());

        if (!(updateHeroRequest.getRace() == null || updateHeroRequest.getRace().equals("")))
            updateHeroRequestApiOut.setRace(updateHeroRequest.getRace());

        if (updateHeroRequest.getEnabled() != updateHeroRequestApiOut.isEnabled())
            updateHeroRequestApiOut.setEnabled(updateHeroRequest.getEnabled());

        return updateHeroRequestApiOut;
    }

    private UpdatePowerStatsRequestApiOut createUpdatePowerStatsRequestApiOut(PowerStatsModelApiOut powerStats, UpdateHeroRequest updateHeroRequest) {
        UpdatePowerStatsRequestApiOut updatePowerStatsRequestApiOut = UpdatePowerStatsRequestApiOut.builder()
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .build();

        if (updateHeroRequest.getAgility() != null && updateHeroRequest.getAgility() != powerStats.getAgility())
            updatePowerStatsRequestApiOut.setAgility(updateHeroRequest.getAgility());

        if (updateHeroRequest.getDexterity() != null && updateHeroRequest.getDexterity() != powerStats.getDexterity())
            updatePowerStatsRequestApiOut.setDexterity(updateHeroRequest.getDexterity());

        if (updateHeroRequest.getIntelligence() != null && updateHeroRequest.getIntelligence() != powerStats.getIntelligence())
            updatePowerStatsRequestApiOut.setIntelligence(updateHeroRequest.getIntelligence());

        if (updateHeroRequest.getStrength() != null && updateHeroRequest.getStrength() != powerStats.getStrength())
            updatePowerStatsRequestApiOut.setStrength(updateHeroRequest.getStrength());

        return updatePowerStatsRequestApiOut;
    }

    private HeroModelApiOut findHeroOrFail(UUID heroId) {
        return getHeroByIdPort.findById(heroId)
                .orElseThrow(() -> new HeroIdNotFoundException(heroId));
    }
}
