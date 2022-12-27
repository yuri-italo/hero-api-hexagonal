package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroesByNameUseCase;
import br.com.gubee.api.out.GetHeroesByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;

import java.util.ArrayList;
import java.util.List;

public class HeroesByNameService implements HeroesByNameUseCase {

    private final GetHeroesByNamePort getHeroesByNamePort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public HeroesByNameService(GetHeroesByNamePort getHeroesByNamePort, GetPowerStatsByIdPort getPowerStatsByIdPort) {
        this.getHeroesByNamePort = getHeroesByNamePort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Override
    public List<HeroModelApiIn> findManyByName(String search) {
        List<HeroModelApiIn> foundHeroes = new ArrayList<>();
        List<HeroModelApiOut> heroes = getHeroesByNamePort.findManyByName(search);

        for (var hero : heroes) {
            PowerStatsModelApiOut powerStats = getPowerStatsByIdPort.findById(hero.getPowerStatsId());
            foundHeroes.add(createHeroModelApiIn(hero, powerStats));
        }

        return foundHeroes;
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
}
