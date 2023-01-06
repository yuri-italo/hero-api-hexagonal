package br.com.gubee.webadapter;

import br.com.gubee.api.in.ports.UpdateHeroUseCase;
import br.com.gubee.api.in.requests.UpdateHeroRequest;
import br.com.gubee.webadapter.dto.UpdateHeroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/heroes")
public class UpdateHeroController {
    private final UpdateHeroUseCase updateHeroUseCase;

    public UpdateHeroController(UpdateHeroUseCase updateHeroUseCase) {
        this.updateHeroUseCase = updateHeroUseCase;
    }

    @PutMapping("{heroId}")
    @ResponseStatus(HttpStatus.OK)
    public UpdateHeroDTO update(@PathVariable UUID heroId, @RequestBody @Valid UpdateHeroRequest updateHeroRequest) {
        return new UpdateHeroDTO(updateHeroUseCase.update(heroId, updateHeroRequest));
    }
}
