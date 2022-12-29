package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.exception.HeroIdNotFoundException;

import java.util.UUID;

public class HeroByIdService implements HeroByIdUseCase {

    private final GetHeroByIdPort getHeroByIdPort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public HeroByIdService(GetHeroByIdPort getHeroByIdPort, GetPowerStatsByIdPort getPowerStatsByIdPort) {
        this.getHeroByIdPort = getHeroByIdPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Override
    public HeroModelApiIn findById(UUID heroId) {
        HeroModelApiOut heroModelApiOutOptional = findHeroOrFail(heroId);
        PowerStatsModelApiOut powerStats = getPowerStatsByIdPort.findById(heroModelApiOutOptional.getPowerStatsId());

        return createHeroModelApiIn(heroModelApiOutOptional, powerStats);
    }

    private HeroModelApiIn createHeroModelApiIn(HeroModelApiOut hero, PowerStatsModelApiOut powerStats) {
        return HeroModelApiIn.builder()
                .id(hero.getId())
                .name(hero.getName())
                .race(hero.getRace())
                .createdAt(hero.getCreatedAt())
                .updatedAt(hero.getUpdatedAt())
                .enabled(hero.isEnabled())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .build();

    }

    private HeroModelApiOut findHeroOrFail(UUID heroId) {
        return getHeroByIdPort.findById(heroId)
                .orElseThrow(() -> new HeroIdNotFoundException(heroId));
    }
}
