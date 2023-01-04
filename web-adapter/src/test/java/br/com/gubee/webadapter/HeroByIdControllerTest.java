package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.time.Instant;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HeroByIdController.class)
@ContextConfiguration(classes = HeroByIdController.class)
class HeroByIdControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    HeroByIdUseCase heroByIdUseCase;

    @Test
    void getByIdSucceeds() throws Exception {
        // given
        HeroModelApiIn heroModelApiIn = givenHeroModelApiIn();

        // when
        ResultActions resultActions = mockMvc.perform(get("/api/v1/heroes/{heroId}", heroModelApiIn.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.id").value(heroModelApiIn.getId().toString()))
                .andExpect(jsonPath("$.name").value(heroModelApiIn.getName()))
                .andExpect(jsonPath("$.race").value(heroModelApiIn.getRace()))
                .andExpect(jsonPath("$.enabled").value(heroModelApiIn.isEnabled()))
                .andExpect(jsonPath("$.createdAt").value(heroModelApiIn.getCreatedAt().toString()))
                .andExpect(jsonPath("$.updatedAt").value(heroModelApiIn.getUpdatedAt().toString()))
                .andExpect(jsonPath("$.powerStats.agility").value(heroModelApiIn.getAgility()))
                .andExpect(jsonPath("$.powerStats.dexterity").value(heroModelApiIn.getDexterity()))
                .andExpect(jsonPath("$.powerStats.intelligence").value(heroModelApiIn.getIntelligence()))
                .andExpect(jsonPath("$.powerStats.strength").value(heroModelApiIn.getStrength()));
    }

    private HeroModelApiIn givenHeroModelApiIn() {

        HeroModelApiIn heroModelApiIn = HeroModelApiIn.builder()
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

        when(heroByIdUseCase.findById(heroModelApiIn.getId())).thenReturn(heroModelApiIn);

        return heroModelApiIn;
    }
}