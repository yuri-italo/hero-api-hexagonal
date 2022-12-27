package br.com.gubee.webadapter.dto;

import br.com.gubee.api.in.model.HeroModelApiIn;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class ListHeroDTO {
    private final UUID id;
    private final String name;
    private final String race;
    private final int strength;
    private final int agility;
    private final int dexterity;
    private final int intelligence;

    public ListHeroDTO(HeroModelApiIn heroModelApiIn) {
        this.id = heroModelApiIn.getId();
        this.name = heroModelApiIn.getName();
        this.race = heroModelApiIn.getRace();
        this.strength = heroModelApiIn.getStrength();
        this.agility = heroModelApiIn.getAgility();
        this.dexterity = heroModelApiIn.getDexterity();
        this.intelligence = heroModelApiIn.getIntelligence();
    }

    public static List<ListHeroDTO> toCollectionDTO(List<HeroModelApiIn> heroModelApiIns) {
        List<ListHeroDTO> heroes = new ArrayList<>();

        for (var hero: heroModelApiIns)
            heroes.add(new ListHeroDTO(hero));

        return heroes;
    }
}
