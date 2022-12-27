package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.webadapter.dto.HeroDTO;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class HeroByIdController {
    private final HeroByIdUseCase heroByIdUseCase;

    public HeroByIdController(HeroByIdUseCase heroByIdUseCase) {
        this.heroByIdUseCase = heroByIdUseCase;
    }

    @GetMapping(value = "/{heroId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> getById(@PathVariable UUID heroId) {
        Optional<HeroModelApiIn> optionalHeroModel =  heroByIdUseCase.findById(heroId);

        return optionalHeroModel.map(
                heroModelApiIn -> ResponseEntity.ok(new HeroDTO(heroModelApiIn))).orElseGet(() -> ResponseEntity.notFound().build()
        );
    }
}
