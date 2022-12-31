package br.com.gubee.api.in.model;

import lombok.Getter;

@Getter
public class CompareHeroApiIn {
    private final HeroModelApiIn heroModelApiIn;
    private final HeroModelApiIn heroModelApiIn2;

    public CompareHeroApiIn(HeroModelApiIn heroModelApiIn, HeroModelApiIn heroModelApiIn2) {
        this.heroModelApiIn = heroModelApiIn;
        this.heroModelApiIn2 = heroModelApiIn2;
    }
}
