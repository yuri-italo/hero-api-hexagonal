package br.com.gubee.api.out.requests;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
@Builder
@Getter
public class RegisterPowerStatsRequest {
    @Min(value = 0, message = "message.powerstats.strength.min")
    @Max(value = 10, message = "message.powerstats.strength.max")
    @NotNull(message = "message.powerstats.strength.mandatory")
    private final int strength;

    @Min(value = 0, message = "message.powerstats.agility.min")
    @Max(value = 10, message = "message.powerstats.agility.max")
    @NotNull(message = "message.powerstats.agility.mandatory")
    private final int agility;

    @Min(value = 0, message = "message.powerstats.dexterity.min")
    @Max(value = 10, message = "message.powerstats.dexterity.max")
    @NotNull(message = "message.powerstats.dexterity.mandatory")
    private final int dexterity;

    @Min(value = 0, message = "message.powerstats.intelligence.min")
    @Max(value = 10, message = "message.powerstats.intelligence.max")
    @NotNull(message = "message.powerstats.intelligence.mandatory")
    private final int intelligence;

    public RegisterPowerStatsRequest(int strength, int agility, int dexterity, int intelligence) {
        this.strength = strength;
        this.agility = agility;
        this.dexterity = dexterity;
        this.intelligence = intelligence;
    }
}