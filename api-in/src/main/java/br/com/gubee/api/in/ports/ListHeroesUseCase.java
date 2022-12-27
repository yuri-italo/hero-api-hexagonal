package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.HeroModelApiIn;

import java.util.List;

public interface ListHeroesUseCase {
    List<HeroModelApiIn> findAll();
}
