package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.CompareHeroesUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
@RestController
@RequestMapping("/api/v1/heroes")
public class CompareHeroController {
    private final CompareHeroesUseCase compareHeroesUseCase;

    public CompareHeroController(CompareHeroesUseCase compareHeroesUseCase) {
        this.compareHeroesUseCase = compareHeroesUseCase;
    }

    @GetMapping(value = "/compare", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<?> compare(@RequestParam String hero1Name, @RequestParam String hero2Name)  {
        Optional<List<HeroModelApiIn>> heroes = compareHeroesUseCase.compare(hero1Name,hero2Name);
    }
}
