package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.UpdateHeroUseCase;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UpdateHeroController.class)
@ContextConfiguration(classes = UpdateHeroController.class)
class UpdateHeroControllerTest {
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    UpdateHeroUseCase updateHeroUseCase;

    @Test
    void updateSucceeds() throws Exception {
        // given
        HeroModelApiIn heroModelApiIn = givenHeroModelApiIn();
        UpdateHeroRequest updateHeroRequest = givenUpdatedHero(heroModelApiIn);

        final String body = objectMapper.writeValueAsString(updateHeroRequest);

        // when
        final ResultActions resultActions = mockMvc.perform(patch("/api/v1/heroes/{heroId}",heroModelApiIn.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(heroModelApiIn.getId()))
                .andExpect(jsonPath("$.name").value(updateHeroRequest.getName()))
                .andExpect(jsonPath("$.race").value(updateHeroRequest.getRace()))
                .andExpect(jsonPath("$.createdAt").value(heroModelApiIn.getCreatedAt()))
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andExpect(jsonPath("$.powerStats.agility").value(updateHeroRequest.getAgility()))
                .andExpect(jsonPath("$.powerStats.dexterity").value(updateHeroRequest.getDexterity()))
                .andExpect(jsonPath("$.powerStats.intelligence").value(updateHeroRequest.getIntelligence()))
                .andExpect(jsonPath("$.powerStats.strength").value(updateHeroRequest.getStrength()));
    }

    private HeroModelApiIn givenHeroModelApiIn() {
        return HeroModelApiIn.builder()
                .id(UUID.randomUUID())
                .name("Batman")
                .race("HUMAN")
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .agility(5)
                .dexterity(9)
                .intelligence(8)
                .strength(7)
                .build();
    }

    private UpdateHeroRequest givenUpdatedHero(HeroModelApiIn heroModelApiIn) {
        UpdateHeroRequest request = UpdateHeroRequest.builder()
                .name("SuperMan")
                .race("DIVINE")
                .agility(6)
                .dexterity(4)
                .intelligence(3)
                .strength(2)
                .enabled(false)
                .build();

        HeroModelApiIn updatedHero = HeroModelApiIn.builder()
                .id(heroModelApiIn.getId())
                .name(request.getName())
                .race(request.getRace())
                .agility(request.getAgility())
                .dexterity(request.getDexterity())
                .intelligence(request.getIntelligence())
                .strength(request.getStrength())
                .enabled(request.getEnabled())
                .createdAt(heroModelApiIn.getCreatedAt())
                .updatedAt(Instant.now())
                .build();

        when(updateHeroUseCase.update(heroModelApiIn.getId(),request)).thenReturn(updatedHero);

        return request;
    }
}