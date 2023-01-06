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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
class ListHeroesSystemTest {
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

    @BeforeEach
    void setUp() {
        dbCleaner();
    }

    @Test
    void listHeroes() {
        // given
        List<HeroDTO> heroDTOS = givenHeroList();
        HeroDTO heroDTO = heroDTOS.get(0);
        HeroDTO heroDTO2 = heroDTOS.get(1);

        HttpHeaders headers = new HttpHeaders();
        headers.set("accept", "application/json");

        HttpEntity<Void> requestEntity = new HttpEntity<>(null, headers);

        // when
        ResponseEntity<List<HeroDTO>> result = restTemplate.exchange(
                "http://localhost:" + port + "/api/v1/heroes/",
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                }
        );

        // then
        assertNotNull(result);
        assertEquals(result.getStatusCode(),HttpStatus.OK);
        assertNotNull(result.getBody());
        assertEquals(2,result.getBody().size());

        UUID uuid = result.getBody().get(0).getId();
        UUID uuid2 = result.getBody().get(1).getId();

        assertEquals(heroDTO.getId(),uuid);
        assertEquals(heroDTO2.getId(),uuid2);
    }

    private List<HeroDTO> givenHeroList() {
        HeroModelApiIn hero = heroByIdUseCase.findById(registerHeroUseCase.registerHero(createHeroRequest()));
        HeroModelApiIn hero2 = heroByIdUseCase.findById(registerHeroUseCase.registerHero(createAnotherHeroRequest()));

        return List.of(new HeroDTO(hero),new HeroDTO(hero2));
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
                .name("Superman")
                .race("DIVINE")
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .build();
    }

    private void dbCleaner() {
        listHeroesUseCase.findAll().forEach(heroModelApiIn -> deleteHeroByIdUseCase.delete(heroModelApiIn.getId()));
    }
}