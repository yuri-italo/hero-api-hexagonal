package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.CompareHeroApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
import br.com.gubee.webadapter.dto.ComparedHeroDTO;
import br.com.gubee.webadapter.dto.TwoComparedHeroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping("/api/v1/heroes")
public class CompareHeroController {
    private final CompareHeroesUseCase compareHeroesUseCase;

    public CompareHeroController(CompareHeroesUseCase compareHeroesUseCase) {
        this.compareHeroesUseCase = compareHeroesUseCase;
    }

    @GetMapping(value = "/compare", produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public TwoComparedHeroDTO compare(@RequestParam String hero1Name, @RequestParam String hero2Name)  {
        CompareHeroApiIn comparedHeroes = compareHeroesUseCase.compare(hero1Name,hero2Name);
        return getTwoComparedHeroDTO(comparedHeroes);
    }

    private TwoComparedHeroDTO getTwoComparedHeroDTO(CompareHeroApiIn comparedHeroes) {
        return new TwoComparedHeroDTO(
                new ComparedHeroDTO(comparedHeroes.getHeroModelApiIn()),
                new ComparedHeroDTO(comparedHeroes.getHeroModelApiIn2())
        );
    }
}
