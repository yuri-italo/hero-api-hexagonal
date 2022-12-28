package br.com.gubee.api.out;

import br.com.gubee.api.out.requests.UpdateHeroRequestApiOut;

import java.util.UUID;

public interface UpdateHeroPort {
    void update(UUID heroId, UpdateHeroRequestApiOut updateHeroRequestApiOut);
}
