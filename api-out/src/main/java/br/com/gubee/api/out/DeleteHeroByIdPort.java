package br.com.gubee.api.out;

import java.util.UUID;

public interface DeleteHeroByIdPort {
    void delete(UUID heroId);
}
