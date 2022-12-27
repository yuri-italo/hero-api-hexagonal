package br.com.gubee.api.out;

import br.com.gubee.api.out.model.HeroModelApiOut;

import java.util.Optional;

public interface FindHeroByNamePort {

    Optional<HeroModelApiOut> findByName(String hero1Name);
}
