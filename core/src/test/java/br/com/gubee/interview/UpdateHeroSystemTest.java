package br.com.gubee.interview;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.webadapter.dto.UpdateHeroDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, properties = "spring.profiles.active=test")
@Testcontainers
class UpdateHeroSystemTest {
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
    void updateHero() {
        // given
        HeroModelApiIn heroModelApiIn = givenHero();
        UpdateHeroRequest request = givenUpdateHeroRequest();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        headers.add("accept", "application/json");

        HttpEntity<UpdateHeroRequest> requestEntity = new HttpEntity<>(request, headers);

        // when
        ResponseEntity<UpdateHeroDTO> result = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/heroes/" + heroModelApiIn.getId(),
                HttpMethod.PUT,
                requestEntity,
                UpdateHeroDTO.class
        );

        // then
        assertNotNull(result);
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertNotNull(result.getBody());

        assertEquals(request.getName(),result.getBody().getName());
        assertEquals(request.getRace(),result.getBody().getRace());
        assertEquals(request.getEnabled(),result.getBody().isEnabled());
        assertEquals(request.getAgility(),result.getBody().getPowerStats().getAgility());
        assertEquals(request.getDexterity(),result.getBody().getPowerStats().getDexterity());
        assertEquals(request.getIntelligence(),result.getBody().getPowerStats().getIntelligence());
        assertEquals(request.getStrength(),result.getBody().getPowerStats().getStrength());
        assertEquals(heroModelApiIn.getCreatedAt().toString(),result.getBody().getCreatedAt());
        assertNotSame(heroModelApiIn.getUpdatedAt().toString(),result.getBody().getUpdatedAt());
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

    private UpdateHeroRequest givenUpdateHeroRequest() {
        return UpdateHeroRequest.builder()
                .name("Spider")
                .race("ALIEN")
                .agility(5)
                .dexterity(4)
                .intelligence(3)
                .strength(2)
                .enabled(false)
                .build();
    }

    private void dbCleaner() {
        listHeroesUseCase.findAll().forEach(heroModelApiIn -> deleteHeroByIdUseCase.delete(heroModelApiIn.getId()));
    }
}