package br.com.gubee.webadapter.dto;

import br.com.gubee.api.in.model.HeroModelApiIn;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class UpdateHeroDTO {
    private final UUID id;
    private final String name;
    private final String race;
    private final String createdAt;
    private final String updatedAt;
    private final boolean enabled;
    private final UpdatePowerStatsDTO powerStats;

    public UpdateHeroDTO(HeroModelApiIn heroModelApiIn) {
        this.id = heroModelApiIn.getId();
        this.name = heroModelApiIn.getName();
        this.race = heroModelApiIn.getRace();
        this.createdAt = heroModelApiIn.getCreatedAt().toString();
        this.updatedAt = heroModelApiIn.getUpdatedAt().toString();
        this.enabled = heroModelApiIn.isEnabled();
        this.powerStats = UpdatePowerStatsDTO.builder()
                .agility(heroModelApiIn.getAgility())
                .dexterity(heroModelApiIn.getDexterity())
                .intelligence(heroModelApiIn.getIntelligence())
                .strength(heroModelApiIn.getStrength())
                .build();
    }

    public static List<UpdateHeroDTO> toCollectionDTO(List<HeroModelApiIn> heroModelApiInList) {
        List<UpdateHeroDTO> heroes = new ArrayList<>();

        for (var hero : heroModelApiInList)
            heroes.add(new UpdateHeroDTO(hero));

        return heroes;
    }
}
