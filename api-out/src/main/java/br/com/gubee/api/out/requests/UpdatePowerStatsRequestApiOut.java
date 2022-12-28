package br.com.gubee.api.out.requests;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
public class UpdatePowerStatsRequestApiOut {
    private int agility;
    private int dexterity;
    private int intelligence;
    private int strength;

    public UpdatePowerStatsRequestApiOut(int agility, int dexterity, int intelligence, int strength) {
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
        this.strength = strength;
    }
}
