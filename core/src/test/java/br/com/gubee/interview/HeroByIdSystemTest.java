package br.com.gubee.interview;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.webadapter.dto.HeroDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@Testcontainers
class HeroByIdSystemTest {
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
        registry.add("jdbc.url",container::getJdbcUrl);
        registry.add("jdbc.username",container::getUsername);
        registry.add("jdbc.password",container::getPassword);
    }

    @BeforeEach
    void setUp() {
        dbCleaner();
    }

    @Test
    void heroById() {
        // given
        HeroModelApiIn heroModelApiIn = givenHero();

        // when
        ResponseEntity<HeroDTO> result = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/v1/heroes/"+ heroModelApiIn.getId(),
                HeroDTO.class
        );

        // then
        assertNotNull(result);
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertNotNull(result.getBody());

        HeroDTO response = result.getBody();
        assertEquals(heroModelApiIn.getId(),response.getId());
        assertEquals(heroModelApiIn.getName(),response.getName());
        assertEquals(heroModelApiIn.getRace(),response.getRace());
        assertEquals(heroModelApiIn.getCreatedAt().toString(),response.getCreatedAt());
        assertEquals(heroModelApiIn.getUpdatedAt().toString(),response.getUpdatedAt());
        assertEquals(heroModelApiIn.getAgility(),response.getPowerStats().getAgility());
        assertEquals(heroModelApiIn.getDexterity(),response.getPowerStats().getDexterity());
        assertEquals(heroModelApiIn.getIntelligence(),response.getPowerStats().getIntelligence());
        assertEquals(heroModelApiIn.getStrength(),response.getPowerStats().getStrength());
    }

    private HeroModelApiIn givenHero() {
        return heroByIdUseCase.findById(registerHeroUseCase.registerHero(createHeroRequest()));
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

    private void dbCleaner() {
        listHeroesUseCase.findAll().forEach(heroModelApiIn -> deleteHeroByIdUseCase.delete(heroModelApiIn.getId()));
    }
}