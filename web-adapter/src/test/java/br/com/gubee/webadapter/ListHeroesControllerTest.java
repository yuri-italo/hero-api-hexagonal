package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ListHeroesController.class)
@ContextConfiguration(classes = ListHeroesController.class)
class ListHeroesControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    ListHeroesUseCase listHeroesUseCase;
    @Test
    void listSucceeds() throws Exception {
        // given
        List<HeroModelApiIn> heroModelApiInList = givenHeroList();

        // when
        ResultActions resultActions = mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/heroes")
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(2))
                .andExpect(jsonPath("$.[0].id").value(heroModelApiInList.get(0).getId().toString()))
                .andExpect(jsonPath("$.[1].id").value(heroModelApiInList.get(1).getId().toString()));
    }

    private List<HeroModelApiIn> givenHeroList() {
        List<HeroModelApiIn> heroModelApiInList = List.of(createHeroModelApiIn(),createHeroAnotherModelApiIn());

        when(listHeroesUseCase.findAll()).thenReturn(heroModelApiInList);

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