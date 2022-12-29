package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import br.com.gubee.webadapter.dto.HeroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class HeroByIdController {
    private final HeroByIdUseCase heroByIdUseCase;

    public HeroByIdController(HeroByIdUseCase heroByIdUseCase) {
        this.heroByIdUseCase = heroByIdUseCase;
    }

    @GetMapping(value = "/{heroId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public HeroDTO getById(@PathVariable UUID heroId) {
        HeroModelApiIn heroModelApiIn =  heroByIdUseCase.findById(heroId);
        return new HeroDTO(heroModelApiIn);
    }
}
