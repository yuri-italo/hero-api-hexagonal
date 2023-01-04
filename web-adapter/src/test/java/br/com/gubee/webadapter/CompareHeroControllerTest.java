package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
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

@WebMvcTest(CompareHeroController.class)
@ContextConfiguration(classes = CompareHeroController.class)
class CompareHeroControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    CompareHeroesUseCase compareHeroesUseCase;

    @Test
    void compareSucceeds() throws Exception {
        // given
        String name = "batman";
        String name2 = "spider";

        HeroModelApiIn hero = createHeroModelApiIn(name);
        HeroModelApiIn otherHero = createAnotherHeroModelApiIn(name2);
        givenHeroes(hero,otherHero);

        // when
        ResultActions resultActions = mockMvc.perform(
                get("/api/v1/heroes/compare?hero1Name=" + name + "&hero2Name=" + name2)
                        .accept(MediaType.APPLICATION_JSON)
        );

        // then
        resultActions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isNotEmpty())
                .andExpect(jsonPath("$.hero.id").value(hero.getId().toString()))
                .andExpect(jsonPath("$.hero.name").value(hero.getName()))
                .andExpect(jsonPath("$.hero.strength").value(hero.getStrength() - otherHero.getStrength()))
                .andExpect(jsonPath("$.hero.agility").value(hero.getAgility() - otherHero.getAgility()))
                .andExpect(jsonPath("$.hero.dexterity").value(hero.getDexterity() - otherHero.getDexterity()))
                .andExpect(jsonPath("$.hero.intelligence").value(hero.getIntelligence() - otherHero.getIntelligence()))
                .andExpect(jsonPath("$.otherHero.id").value(otherHero.getId().toString()))
                .andExpect(jsonPath("$.otherHero.name").value(otherHero.getName()))
                .andExpect(jsonPath("$.otherHero.strength").value(otherHero.getStrength() - hero.getStrength()))
                .andExpect(jsonPath("$.otherHero.agility").value(otherHero.getAgility() - hero.getAgility()))
                .andExpect(jsonPath("$.otherHero.dexterity").value(otherHero.getDexterity() - hero.getDexterity()))
                .andExpect(jsonPath("$.otherHero.intelligence").value(otherHero.getIntelligence() - hero.getIntelligence()));
    }

    private void givenHeroes(HeroModelApiIn hero, HeroModelApiIn hero2) {
        HeroModelApiIn comparedHero = createComparedHeroModelApiIn(hero,hero2);
        HeroModelApiIn comparedHero2 = createComparedHeroModelApiIn(hero2,hero);

        CompareHeroApiIn compareHeroApiIn = new CompareHeroApiIn(comparedHero,comparedHero2);

        when(compareHeroesUseCase.compare(hero.getName(),hero2.getName())).thenReturn(compareHeroApiIn);
    }

    private HeroModelApiIn createHeroModelApiIn(String heroName) {
        return HeroModelApiIn.builder()
                .id(UUID.randomUUID())
                .name(heroName)
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

    private HeroModelApiIn createAnotherHeroModelApiIn(String heroName) {
        return HeroModelApiIn.builder()
                .id(UUID.randomUUID())
                .name(heroName)
                .race("ALIEN")
                .enabled(true)
                .createdAt(Instant.now())
                .updatedAt(Instant.now())
                .agility(6)
                .dexterity(5)
                .intelligence(4)
                .strength(3)
                .build();
    }

    private HeroModelApiIn createComparedHeroModelApiIn(HeroModelApiIn hero, HeroModelApiIn heroToCompare) {
        return HeroModelApiIn.builder()
                .id(hero.getId())
                .name(hero.getName())
                .createdAt(hero.getCreatedAt())
                .updatedAt(hero.getUpdatedAt())
                .enabled(hero.isEnabled())
                .agility(hero.getAgility() - heroToCompare.getAgility())
                .dexterity(hero.getDexterity() - heroToCompare.getDexterity())
                .intelligence(hero.getIntelligence() - heroToCompare.getIntelligence())
                .strength(hero.getStrength() - heroToCompare.getStrength())
                .build();
    }
}