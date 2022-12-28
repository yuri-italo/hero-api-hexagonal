package br.com.gubee.api.out.requests;

import br.com.gubee.api.out.model.HeroModelApiOut;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateHeroRequestApiOut {

    private String name;

    private String race;

    private boolean enabled;

    public UpdateHeroRequestApiOut(HeroModelApiOut heroModelApiOut) {
        this.name = heroModelApiOut.getName();
        this.race = heroModelApiOut.getRace();
        this.enabled = heroModelApiOut.isEnabled();
    }
}
