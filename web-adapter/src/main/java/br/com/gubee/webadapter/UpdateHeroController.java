package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.UpdateHeroUseCase;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.webadapter.dto.UpdateHeroDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class UpdateHeroController {
    private final UpdateHeroUseCase updateHeroUseCase;

    public UpdateHeroController(UpdateHeroUseCase updateHeroUseCase) {
        this.updateHeroUseCase = updateHeroUseCase;
    }

    @PatchMapping("{heroId}")
    public ResponseEntity<?> update(@PathVariable UUID heroId, @RequestBody @Valid UpdateHeroRequest updateHeroRequest) {
        Optional<HeroModelApiIn> updatedHero = updateHeroUseCase.update(heroId, updateHeroRequest);

        if (updatedHero.isEmpty())
            return ResponseEntity.notFound().build();

        return ResponseEntity.ok(new UpdateHeroDTO(updatedHero.get()));
    }
}
