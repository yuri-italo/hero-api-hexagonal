package br.com.gubee.webadapter;

import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RegisterHeroController.class)
@ContextConfiguration(classes = RegisterHeroController.class)
class RegisterHeroControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    RegisterHeroUseCase registerHeroUseCase;

    @Test
    void registerHeroSucceeds() throws Exception {
        // given
        CreateHeroRequest request = givenHeroRequest();
        final String body = objectMapper.writeValueAsString(request);

        //when
        final ResultActions resultActions = mockMvc.perform(post("/api/v1/heroes")
                .contentType(MediaType.APPLICATION_JSON)
                .content(body));

        //then
        resultActions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"));
    }

    private CreateHeroRequest givenHeroRequest() {
        CreateHeroRequest request = CreateHeroRequest.builder()
                .name("Batman")
                .race("HUMAN")
                .agility(8)
                .dexterity(7)
                .intelligence(6)
                .strength(5)
                .build();

        Mockito.when(registerHeroUseCase.registerHero(request)).thenReturn(UUID.randomUUID());

        return request;
    }
}