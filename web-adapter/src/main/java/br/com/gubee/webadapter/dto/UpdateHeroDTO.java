package br.com.gubee.webadapter.dto;

import br.com.gubee.api.in.model.HeroModelApiIn;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@NoArgsConstructor
public class UpdateHeroDTO {
    private UUID id;
    private String name;
    private String race;
    private String createdAt;
    private String updatedAt;
    private boolean enabled;
    private UpdatePowerStatsDTO powerStats;

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
