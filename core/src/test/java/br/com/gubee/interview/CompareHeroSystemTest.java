package br.com.gubee.interview;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.persistence.adapter.HeroRepositoryPostgreImpl;
import br.com.gubee.persistence.adapter.PowerStatsRepositoryPostgreImpl;
import br.com.gubee.persistence.adapter.configuration.JdbcConfiguration;
import br.com.gubee.webadapter.dto.ComparedHeroDTO;
import br.com.gubee.webadapter.dto.TwoComparedHeroDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = {HeroRepositoryPostgreImpl.class, PowerStatsRepositoryPostgreImpl.class, JdbcConfiguration.class})
@Testcontainers
class CompareHeroSystemTest {
    @LocalServerPort
    Integer port;
    @Autowired
    TestRestTemplate restTemplate;
    @Autowired
    RegisterHeroUseCase registerHeroUseCase;
    @Autowired
    HeroByIdUseCase heroByIdUseCase;
    @Autowired
    ListHeroesUseCase listHeroesUseCase;
    @Autowired
    DeleteHeroByIdUseCase deleteHeroByIdUseCase;

    @Container
    private static PostgreSQLContainer container = new PostgreSQLContainer("postgres:14.6");

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url",container::getJdbcUrl);
        registry.add("spring.datasource.username",container::getUsername);
        registry.add("spring.datasource.password",container::getPassword);
    }

    @BeforeEach
    void setUp() {
        dbCleaner();
    }

    @Test
    void compare() {
        // given
        TwoComparedHeroDTO twoComparedHeroDTO = givenTwoComparedHeroes();
        String heroName = twoComparedHeroDTO.getHero().getName();
        String heroName2 = twoComparedHeroDTO.getOtherHero().getName();

        // when
        ResponseEntity<TwoComparedHeroDTO> result = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/heroes/compare?hero1Name=" + heroName + "&hero2Name=" + heroName2,
                TwoComparedHeroDTO.class
        );

        // then
        assertNotNull(result);
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertNotNull(result.getBody().getHero());
        assertNotNull(result.getBody().getOtherHero());

        TwoComparedHeroDTO response = result.getBody();
        assertEquals(twoComparedHeroDTO.getHero().getId(),response.getHero().getId());
        assertEquals(twoComparedHeroDTO.getHero().getName(),response.getHero().getName());
        assertEquals(twoComparedHeroDTO.getHero().getAgility(),response.getHero().getAgility());
        assertEquals(twoComparedHeroDTO.getHero().getDexterity(),response.getHero().getDexterity());
        assertEquals(twoComparedHeroDTO.getHero().getStrength(),response.getHero().getStrength());

        assertEquals(twoComparedHeroDTO.getOtherHero().getId(),response.getOtherHero().getId());
        assertEquals(twoComparedHeroDTO.getOtherHero().getName(),response.getOtherHero().getName());
        assertEquals(twoComparedHeroDTO.getOtherHero().getAgility(),response.getOtherHero().getAgility());
        assertEquals(twoComparedHeroDTO.getOtherHero().getDexterity(),response.getOtherHero().getDexterity());
        assertEquals(twoComparedHeroDTO.getOtherHero().getStrength(),response.getOtherHero().getStrength());
    }

    private TwoComparedHeroDTO givenTwoComparedHeroes() {
        HeroModelApiIn hero = heroByIdUseCase.findById(registerHeroUseCase.registerHero(createHeroRequest()));
        HeroModelApiIn hero2 = heroByIdUseCase.findById(registerHeroUseCase.registerHero(createAnotherHeroRequest()));

        ComparedHeroDTO comparedHeroDTO = compareStats(hero,hero2);
        ComparedHeroDTO comparedHeroDTO2 = compareStats(hero2,hero);

        return new TwoComparedHeroDTO(comparedHeroDTO,comparedHeroDTO2);
    }

    private CreateHeroRequest createHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private CreateHeroRequest createAnotherHeroRequest() {
        return CreateHeroRequest.builder()
                .name("Spider")
                .race("ALIEN")
                .agility(7)
                .dexterity(8)
                .intelligence(9)
                .strength(10)
                .build();
    }

    private ComparedHeroDTO compareStats(HeroModelApiIn hero, HeroModelApiIn heroToCompare) {
        HeroModelApiIn comparedHero = HeroModelApiIn.builder()
                .id(hero.getId())
                .name(hero.getName())
                .race(hero.getRace())
                .createdAt(hero.getCreatedAt())
                .updatedAt(hero.getUpdatedAt())
                .agility(hero.getAgility() - heroToCompare.getAgility())
                .dexterity(hero.getDexterity() - heroToCompare.getDexterity())
                .intelligence(hero.getIntelligence() - heroToCompare.getIntelligence())
                .strength(hero.getStrength() - heroToCompare.getStrength())
                .enabled(hero.isEnabled())
                .build();

        return new ComparedHeroDTO(comparedHero);
    }

    private void dbCleaner() {
        listHeroesUseCase.findAll().forEach(heroModelApiIn -> deleteHeroByIdUseCase.delete(heroModelApiIn.getId()));
    }
}