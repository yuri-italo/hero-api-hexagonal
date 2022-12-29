package br.com.gubee.webadapter;

import br.com.gubee.api.in.ports.DeleteHeroByIdUseCase;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class DeleteHeroController {
    private final DeleteHeroByIdUseCase deleteHeroByIdUseCase;

    public DeleteHeroController(DeleteHeroByIdUseCase deleteHeroByIdUseCase) {
        this.deleteHeroByIdUseCase = deleteHeroByIdUseCase;
    }

    @DeleteMapping("{heroId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable UUID heroId) {
        deleteHeroByIdUseCase.delete(heroId);
    }
}
