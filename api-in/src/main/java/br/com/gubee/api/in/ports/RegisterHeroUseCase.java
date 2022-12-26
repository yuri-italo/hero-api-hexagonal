package br.com.gubee.api.in.ports;

import br.com.gubee.api.in.requests.CreateHeroRequest;

import java.util.UUID;

public interface RegisterHeroUseCase {
    UUID registerHero(CreateHeroRequest createHeroRequest);
}
