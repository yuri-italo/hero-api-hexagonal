package br.com.gubee.application;

import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import br.com.gubee.api.out.DeleteHeroByIdPort;
import br.com.gubee.api.out.DeletePowerStatsByIdPort;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.model.HeroModelApiOut;

import java.util.UUID;

public class DeleteHeroByIdService implements DeleteHeroByIdUseCase {
    private final GetHeroByIdPort getHeroByIdPort;
    private final DeleteHeroByIdPort deleteHeroByIdPort;
    private final DeletePowerStatsByIdPort deletePowerStatsByIdPort;


    public DeleteHeroByIdService(GetHeroByIdPort getHeroByIdPort, DeleteHeroByIdPort deleteHeroByIdPort, DeletePowerStatsByIdPort deletePowerStatsByIdPort) {
        this.getHeroByIdPort = getHeroByIdPort;
        this.deleteHeroByIdPort = deleteHeroByIdPort;
        this.deletePowerStatsByIdPort = deletePowerStatsByIdPort;
    }

    @Override
    public void delete(UUID heroId) {
        HeroModelApiOut heroModelApiOut = getHeroByIdPort.findById(heroId)
                .orElseThrow(NullPointerException::new);

        UUID powerStatsId = heroModelApiOut.getPowerStatsId();

        deleteHeroByIdPort.delete(heroId);
        deletePowerStatsByIdPort.delete(powerStatsId);
    }
}
