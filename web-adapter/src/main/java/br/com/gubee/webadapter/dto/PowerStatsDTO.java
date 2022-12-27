package br.com.gubee.webadapter.dto;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class PowerStatsDTO {
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;

    public PowerStatsDTO(int strength, int agility, int dexterity, int intelligence) {
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }
}
