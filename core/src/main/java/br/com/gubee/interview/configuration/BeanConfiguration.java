package br.com.gubee.interview.configuration;

import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetPowerStatsByIdPort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.application.HeroByIdService;
import br.com.gubee.application.RegisterHeroService;
import br.com.gubee.configuration.JdbcConfiguration;
import br.com.gubee.persistence.adapter.HeroRepositoryPostgreImpl;
import br.com.gubee.persistence.adapter.PowerStatsRepositoryPostgreImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@Import(JdbcConfiguration.class)
@EnableJdbcRepositories(basePackageClasses = {HeroRepositoryPostgreImpl.class, PowerStatsRepositoryPostgreImpl.class})
public class BeanConfiguration {

    private final RegisterHeroPort registerHeroPort;
    private final RegisterPowerStatsPort registerPowerStatsPort;
    private final GetHeroByIdPort getHeroByIdPort;
    private final GetPowerStatsByIdPort getPowerStatsByIdPort;

    public BeanConfiguration(
            RegisterHeroPort registerHeroPort,
            RegisterPowerStatsPort registerPowerStatsPort,
            GetHeroByIdPort getHeroByIdPort,
            GetPowerStatsByIdPort getPowerStatsByIdPort
    ) {
        this.registerHeroPort = registerHeroPort;
        this.registerPowerStatsPort = registerPowerStatsPort;
        this.getHeroByIdPort = getHeroByIdPort;
        this.getPowerStatsByIdPort = getPowerStatsByIdPort;
    }

    @Bean
    public RegisterHeroUseCase registerHeroUseCase() {
        return new RegisterHeroService(registerHeroPort,registerPowerStatsPort);
    }

    @Bean
    public HeroByIdUseCase findById() {
        return new HeroByIdService(getHeroByIdPort, getPowerStatsByIdPort);
    }
}
