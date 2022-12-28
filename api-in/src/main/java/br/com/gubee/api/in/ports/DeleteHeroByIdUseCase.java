package br.com.gubee.api.in.ports;

import java.util.UUID;

public interface DeleteHeroByIdUseCase {
    void delete(UUID heroModelApiIn);
}
