package br.com.gubee.interview.configuration;

import br.com.gubee.api.in.ports.*;
import br.com.gubee.api.out.*;
import br.com.gubee.application.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    private final RegisterHeroPort registerHeroPort;
    private final RegisterPowerStatsPort registerPowerStatsPort;
    private final GetHeroByIdPort getHeroByIdPort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;
    private final GetHeroesByNamePort getHeroesByNamePort;
    private final ListHeroesPort listHeroesPort;
    private final FindHeroByNamePort findHeroByNamePort;
    private final UpdateHeroPort updateHeroPort;
    private final UpdatePowerStatsPort updatePowerStatsPort;
    private final DeleteHeroByIdPort deleteHeroByIdPort;
    private final DeletePowerStatsByIdPort deletePowerStatsByIdPort;

    public BeanConfiguration(
            RegisterHeroPort registerHeroPort,
            RegisterPowerStatsPort registerPowerStatsPort,
            GetHeroByIdPort getHeroByIdPort,
            GetPowerStatsByIdPort getPowerStatsByIdPort,
            GetHeroesByNamePort getHeroesByNamePort,
            ListHeroesPort listHeroesPort,
            FindHeroByNamePort findHeroByNamePort,
            UpdateHeroPort updateHeroPort,
            UpdatePowerStatsPort updatePowerStatsPort,
            DeleteHeroByIdPort deleteHeroByIdPort,
            DeletePowerStatsByIdPort deletePowerStatsByIdPort
    ) {
        this.registerHeroPort = registerHeroPort;
        this.registerPowerStatsPort = registerPowerStatsPort;
        this.getHeroByIdPort = getHeroByIdPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
        this.getHeroesByNamePort = getHeroesByNamePort;
        this.listHeroesPort = listHeroesPort;
        this.findHeroByNamePort = findHeroByNamePort;
        this.updateHeroPort = updateHeroPort;
        this.updatePowerStatsPort = updatePowerStatsPort;
        this.deleteHeroByIdPort = deleteHeroByIdPort;
        this.deletePowerStatsByIdPort = deletePowerStatsByIdPort;
    }

    @Bean
    public RegisterHeroUseCase registerHeroUseCase() {
        return new RegisterHeroService(registerHeroPort,registerPowerStatsPort);
    }

    @Bean
    public HeroByIdUseCase findById() {
        return new HeroByIdService(getHeroByIdPort, getPowerStatsByIdPort);
    }

    @Bean
    public HeroesByNameUseCase findManyByName() {
        return new HeroesByNameService(getHeroesByNamePort,getPowerStatsByIdPort);
    }

    @Bean
    public ListHeroesUseCase findAll() {
        return new ListHeroesService(listHeroesPort,getPowerStatsByIdPort);
    }

    @Bean
    public CompareHeroesUseCase compare() {
        return new CompareHeroService(findHeroByNamePort,getPowerStatsByIdPort);
    }

    @Bean
    public UpdateHeroUseCase update() {
        return new UpdateHeroService(getHeroByIdPort,updateHeroPort,getPowerStatsByIdPort,updatePowerStatsPort);
    }

    @Bean
    public DeleteHeroByIdUseCase delete() {
        return new DeleteHeroByIdService(getHeroByIdPort,deleteHeroByIdPort,deletePowerStatsByIdPort);
    }
}
