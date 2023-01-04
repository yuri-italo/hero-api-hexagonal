package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroesByNameUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeroByNameController.class)
@ContextConfiguration(classes = HeroByNameController.class)
class HeroByNameControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    HeroesByNameUseCase heroesByNameUseCase;

    @Test
    void findManyByNameSucceeds() throws Exception {
        // given
        String search = "man";
        List<HeroModelApiIn> heroes = givenHeroModelApiInList(search);

        // when
        final ResultActions resultActions = mockMvc.perform(get("/api/v1/heroes/search/{search}",search)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].name").value(heroes.get(0).getName()))
                .andExpect(jsonPath("$.[1].name").value(heroes.get(1).getName()));

        assertTrue(heroes.get(0).getName().contains(search));
        assertTrue(heroes.get(1).getName().contains(search));
    }

    private List<HeroModelApiIn> givenHeroModelApiInList(String search) {
        List<HeroModelApiIn> heroModelApiInList = List.of(createHeroModelApiIn(),createHeroAnotherModelApiIn());

        when(heroesByNameUseCase.findManyByName(search)).thenReturn(heroModelApiInList);

        return heroModelApiInList;
    }

    private HeroModelApiIn createHeroModelApiIn() {
        return HeroModelApiIn.builder()
                .id(UUID.randomUUID())
                .name("Batman")
                .race("HUMAN")
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .agility(10)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private HeroModelApiIn createHeroAnotherModelApiIn() {
        return  HeroModelApiIn.builder()
                .id(UUID.randomUUID())
                .name("Superman")
                .race("HUMAN")
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .agility(10)
                .dexterity(10)
                .intelligence(10)
                .strength(10)
                .build();
    }
}