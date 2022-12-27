package br.com.gubee.application;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
import br.com.gubee.api.out.CompareHeroesPort;
import br.com.gubee.api.out.FindHeroByNamePort;
import br.com.gubee.api.out.model.HeroModelApiOut;

import java.util.List;
import java.util.Optional;

public class CompareHeroService implements CompareHeroesUseCase {
    private final CompareHeroesPort compareHeroesPort;
    private final FindHeroByNamePort findHeroByNamePort;

    public CompareHeroService(CompareHeroesPort compareHeroesPort, FindHeroByNamePort findHeroByNamePort) {
        this.compareHeroesPort = compareHeroesPort;
        this.findHeroByNamePort = findHeroByNamePort;
    }

    @Override
    public Optional<List<HeroModelApiIn>> compare(String hero1Name, String hero2Name) {
        Optional<HeroModelApiOut> hero1 = findHeroByNamePort.findByName(hero1Name);
        Optional<HeroModelApiOut> hero2 = findHeroByNamePort.findByName(hero2Name);

        if (hero1.isEmpty() || hero2.isEmpty())
            return Optional.empty();


        return Optional.empty();
    }
}
