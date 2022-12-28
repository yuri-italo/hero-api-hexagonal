package br.com.gubee.webadapter.dto;

import br.com.gubee.api.in.model.HeroModelApiIn;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Getter
@NoArgsConstructor
public class ComparedHeroDTO {
    private UUID id;
    private String name;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

    public ComparedHeroDTO(HeroModelApiIn heroModelApiIn) {
        this.id = heroModelApiIn.getId();
        this.name = heroModelApiIn.getName();
        this.agility = heroModelApiIn.getAgility();
        this.dexterity = heroModelApiIn.getDexterity();
        this.intelligence = heroModelApiIn.getIntelligence();
        this.strength = heroModelApiIn.getStrength();
    }
}
