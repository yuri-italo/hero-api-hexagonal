package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.model.HeroModelApiIn;

import java.util.UUID;

public interface HeroByIdUseCase {
    HeroModelApiIn findById(UUID heroId);
}
