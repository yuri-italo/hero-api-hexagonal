package br.com.gubee.webadapter.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class UpdatePowerStatsDTO {
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

    public UpdatePowerStatsDTO(int strength, int agility, int dexterity, int intelligence) {
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }
}
