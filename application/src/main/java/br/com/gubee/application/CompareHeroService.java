package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.PowerStats;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CompareHeroService implements CompareHeroesUseCase {
    private final FindHeroByNamePort findHeroByNamePort;

    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public CompareHeroService(FindHeroByNamePort findHeroByNamePort, GetPowerStatsByIdPort getPowerStatsByIdPort) {
        this.findHeroByNamePort = findHeroByNamePort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Override
    public Optional<List<HeroModelApiIn>> compare(String hero1Name, String hero2Name) {
        Optional<HeroModelApiOut> hero1 = findHeroByNamePort.findByName(hero1Name);
        Optional<HeroModelApiOut> hero2 = findHeroByNamePort.findByName(hero2Name);

        if (hero1.isEmpty() || hero2.isEmpty())
            return Optional.empty();

        HeroModelApiOut heroModelApiOut = hero1.get();
        HeroModelApiOut heroModelApiOut2 = hero2.get();

        List<HeroModelApiIn> comparedHeroes = getComparedHeroes(heroModelApiOut, heroModelApiOut2);

        return Optional.of(comparedHeroes);
    }

    private List<HeroModelApiIn> getComparedHeroes(HeroModelApiOut heroModelApiOut, HeroModelApiOut heroModelApiOut2) {
        List<HeroModelApiIn> comparedHeroes = new ArrayList<>();
        List<PowerStats> powerStatsList = createPowerStatsList(heroModelApiOut, heroModelApiOut2);
        PowerStats.compareStats(powerStatsList.get(0),powerStatsList.get(1));

        comparedHeroes.add(createHeroModelApiIn(heroModelApiOut,powerStatsList.get(0)));
        comparedHeroes.add(createHeroModelApiIn(heroModelApiOut2,powerStatsList.get(1)));
        return comparedHeroes;
    }

    private List<PowerStats> createPowerStatsList(HeroModelApiOut heroModelApiOut, HeroModelApiOut heroModelApiOut2) {
        List<PowerStats> heroesPowerStats = new ArrayList<>();
        PowerStatsModelApiOut powerStatsModelApiOut = getPowerStatsByIdPort.findById(heroModelApiOut.getPowerStatsId());
        PowerStatsModelApiOut powerStatsModelApiOut2 = getPowerStatsByIdPort.findById(heroModelApiOut2.getPowerStatsId());

        PowerStats powerStats = new PowerStats(
                powerStatsModelApiOut.getId(),
                powerStatsModelApiOut.getStrength(),
                powerStatsModelApiOut.getAgility(),
                powerStatsModelApiOut.getDexterity(),
                powerStatsModelApiOut.getIntelligence(),
                powerStatsModelApiOut.getCreatedAt(),
                powerStatsModelApiOut.getUpdatedAt()
        );

        PowerStats powerStats2 = new PowerStats(
                powerStatsModelApiOut2.getId(),
                powerStatsModelApiOut2.getStrength(),
                powerStatsModelApiOut2.getAgility(),
                powerStatsModelApiOut2.getDexterity(),
                powerStatsModelApiOut2.getIntelligence(),
                powerStatsModelApiOut2.getCreatedAt(),
                powerStatsModelApiOut2.getUpdatedAt()
        );

        heroesPowerStats.add(powerStats);
        heroesPowerStats.add(powerStats2);

        return heroesPowerStats;
    }

    private HeroModelApiIn createHeroModelApiIn(HeroModelApiOut heroModelApiOut, PowerStats powerStats) {
        return HeroModelApiIn.builder()
                .id(heroModelApiOut.getId())
                .name(heroModelApiOut.getName())
                .race(heroModelApiOut.getRace())
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(heroModelApiOut.getUpdatedAt())
                .enabled(heroModelApiOut.isEnabled())
                .agility(powerStats.getAgility())
                .dexterity(powerStats.getDexterity())
                .intelligence(powerStats.getIntelligence())
                .strength(powerStats.getStrength())
                .build();
    }
}
