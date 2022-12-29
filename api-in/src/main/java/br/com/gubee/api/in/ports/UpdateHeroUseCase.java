package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.requests.UpdateHeroRequest;

import java.util.UUID;

public interface UpdateHeroUseCase {
    HeroModelApiIn update(UUID heroId, UpdateHeroRequest updateHeroRequest);
}
