package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.ListHeroesPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;

import java.util.ArrayList;
import java.util.List;

public class ListHeroesService implements ListHeroesUseCase {
    private final ListHeroesPort listHeroesPort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public ListHeroesService(ListHeroesPort listHeroesPort, GetPowerStatsByIdPort getPowerStatsByIdPort) {
        this.listHeroesPort = listHeroesPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Override
    public List<HeroModelApiIn> findAll() {
        List<HeroModelApiIn> heroModelApiInList = new ArrayList<>();
        List<HeroModelApiOut> heroes = listHeroesPort.findAll();

        for (var hero : heroes) {
            PowerStatsModelApiOut powerStats = getPowerStatsByIdPort.findById(hero.getPowerStatsId());
            heroModelApiInList.add(createHeroModelApiIn(hero,powerStats));
        }

        return heroModelApiInList;
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
