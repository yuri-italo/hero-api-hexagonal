package br.com.gubee.api.out;

import br.com.gubee.api.out.model.HeroModelApiOut;

import java.util.List;

public interface ListHeroesPort {

    List<HeroModelApiOut> findAll();
}
