package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import br.com.gubee.api.in.ports.HeroByIdUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class DeleteHeroController {
    private final HeroByIdUseCase heroByIdUseCase;
    private final DeleteHeroByIdUseCase deleteHeroByIdUseCase;

    public DeleteHeroController(HeroByIdUseCase heroByIdUseCase, DeleteHeroByIdUseCase deleteHeroByIdUseCase) {
        this.heroByIdUseCase = heroByIdUseCase;
        this.deleteHeroByIdUseCase = deleteHeroByIdUseCase;
    }

    @DeleteMapping("{heroId}")
    public ResponseEntity<Void> delete(@PathVariable UUID heroId) {
        Optional<HeroModelApiIn> optionalHeroModelApiIn = heroByIdUseCase.findById(heroId);

        if (optionalHeroModelApiIn.isEmpty())
            return ResponseEntity.notFound().build();

        deleteHeroByIdUseCase.delete(heroId);

        return ResponseEntity.noContent().build();
    }
}
