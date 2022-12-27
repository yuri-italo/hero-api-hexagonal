package br.com.gubee.api.out;

import br.com.gubee.api.out.model.HeroModelApiOut;

import java.util.Optional;
import java.util.UUID;

public interface GetHeroByIdPort {
    Optional<HeroModelApiOut> findById(UUID uuid);
}
