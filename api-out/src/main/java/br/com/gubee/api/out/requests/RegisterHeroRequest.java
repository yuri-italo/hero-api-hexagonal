package br.com.gubee.api.out.requests;

import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Builder
@Getter
public class RegisterHeroRequest {
    @NotBlank(message = "message.name.mandatory")
    private final String name;

    @NotNull(message = "message.race.mandatory")
    private final String race;

    public RegisterHeroRequest(String name, String race) {
        this.name = name;
        this.race = race;
    }
}