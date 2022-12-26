package br.com.gubee.interview.configuration;

import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.RegisterPowerStatsPort;
import br.com.gubee.application.RegisterHeroService;
import br.com.gubee.persistence.adapter.HeroRepositoryPostgreImpl;
import br.com.gubee.persistence.adapter.PowerStatsRepositoryPostgreImpl;
import br.com.gubee.persistence.adapter.configuration.JdbcConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@org.springframework.context.annotation.Configuration
@Import(JdbcConfiguration.class)
@EnableJdbcRepositories(basePackageClasses = {HeroRepositoryPostgreImpl.class, PowerStatsRepositoryPostgreImpl.class})
public class Configuration {

    private final RegisterHeroPort registerHeroPort;
    private final RegisterPowerStatsPort registerPowerStatsPort;

    public Configuration(RegisterHeroPort registerHeroPort, RegisterPowerStatsPort registerPowerStatsPort) {
        this.registerHeroPort = registerHeroPort;
        this.registerPowerStatsPort = registerPowerStatsPort;
    }

    @Bean
    public RegisterHeroUseCase registerHeroUseCase() {
        return new RegisterHeroService(registerHeroPort,registerPowerStatsPort);
    }
}
