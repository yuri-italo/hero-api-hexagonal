package br.com.gubee.application;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.configuration.PowerStats;
import br.com.gubee.configuration.exception.HeroNameNotFoundException;

public class CompareHeroService implements CompareHeroesUseCase {
    private final String MSG_NAME_NOT_FOUND = "Name \"%s\" was not found.";
    private final FindHeroByNamePort findHeroByNamePort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public CompareHeroService(FindHeroByNamePort findHeroByNamePort, GetPowerStatsByIdPort getPowerStatsByIdPort) {
        this.findHeroByNamePort = findHeroByNamePort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Override
    public CompareHeroApiIn compare(String hero1Name, String hero2Name) {
        HeroModelApiOut hero1 = findHeroOrFail(hero1Name);
        HeroModelApiOut hero2 = findHeroOrFail(hero2Name);

        PowerStats powerStats = createPowerStats(getPowerStatsByIdPort.findById(hero1.getPowerStatsId()));
        PowerStats powerStats2 = createPowerStats(getPowerStatsByIdPort.findById(hero2.getPowerStatsId()));
        PowerStats.compareStats(powerStats,powerStats2);

        return new CompareHeroApiIn(createHeroModelApiIn(hero1, powerStats), createHeroModelApiIn(hero2, powerStats2));
    }

    private PowerStats createPowerStats(PowerStatsModelApiOut powerStatsModelApiOut) {
        return new PowerStats(
                powerStatsModelApiOut.getId(),
                powerStatsModelApiOut.getStrength(),
                powerStatsModelApiOut.getAgility(),
                powerStatsModelApiOut.getDexterity(),
                powerStatsModelApiOut.getIntelligence(),
                powerStatsModelApiOut.getCreatedAt(),
                powerStatsModelApiOut.getUpdatedAt()
        );
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

    private HeroModelApiOut findHeroOrFail(String heroName) {
        return findHeroByNamePort.findByName(heroName)
                .orElseThrow(() -> new HeroNameNotFoundException(String.format(MSG_NAME_NOT_FOUND, heroName)));
    }
}
