package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.HeroModelApiIn;

import java.util.List;
import java.util.Optional;

public interface CompareHeroesUseCase {
    Optional<List<HeroModelApiIn>> compare(String hero1Name, String hero2Name);
}
