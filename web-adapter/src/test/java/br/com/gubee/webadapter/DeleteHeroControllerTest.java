package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
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

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DeleteHeroController.class)
@ContextConfiguration(classes = DeleteHeroController.class)
class DeleteHeroControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    DeleteHeroByIdUseCase deleteHeroByIdUseCase;
    @Test
    void deleteSucceeds() throws Exception {
        // given
        HeroModelApiIn heroModelApiIn = createHeroModelApiIn();

        // when
        ResultActions resultActions = mockMvc.perform(delete("/api/v1/heroes/{heroId}", heroModelApiIn.getId())
                .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isNoContent());
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
}