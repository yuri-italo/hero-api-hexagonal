package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.HeroModelApiIn;

import java.util.List;

public interface HeroesByNameUseCase {
    List<HeroModelApiIn> findManyByName(String search);
}
