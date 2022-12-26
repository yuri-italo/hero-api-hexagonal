package br.com.gubee.api.out;

import br.com.gubee.api.out.requests.RegisterHeroRequest;

import java.util.UUID;

public interface RegisterHeroPort {
    UUID registerHero(RegisterHeroRequest registerHeroRequest, UUID uuid);
}
