package br.com.gubee.webadapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class TwoComparedHeroDTO {
    private ComparedHeroDTO hero;
    private ComparedHeroDTO otherHero;

    public TwoComparedHeroDTO(ComparedHeroDTO hero, ComparedHeroDTO otherHero) {
        this.hero = hero;
        this.otherHero = otherHero;
    }
}
