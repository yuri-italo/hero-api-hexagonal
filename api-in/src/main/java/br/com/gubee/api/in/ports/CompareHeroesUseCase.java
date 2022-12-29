package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.CompareHeroApiIn;

public interface CompareHeroesUseCase {
    CompareHeroApiIn compare(String hero1Name, String hero2Name);
}
