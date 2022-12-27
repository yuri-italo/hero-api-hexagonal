package br.com.gubee.interview.configuration;

import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.in.ports.HeroesByNameUseCase;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.out.*;
import br.com.gubee.application.HeroByIdService;
import br.com.gubee.application.HeroesByNameService;
import br.com.gubee.application.ListHeroesService;
import br.com.gubee.application.RegisterHeroService;
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

    public BeanConfiguration(
            RegisterHeroPort registerHeroPort,
            RegisterPowerStatsPort registerPowerStatsPort,
            GetHeroByIdPort getHeroByIdPort,
            GetPowerStatsByIdPort getPowerStatsByIdPort,
            GetHeroesByNamePort getHeroesByNamePort,
            ListHeroesPort listHeroesPort
    ) {
        this.registerHeroPort = registerHeroPort;
        this.registerPowerStatsPort = registerPowerStatsPort;
        this.getHeroByIdPort = getHeroByIdPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
        this.getHeroesByNamePort = getHeroesByNamePort;
        this.listHeroesPort = listHeroesPort;
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
}
