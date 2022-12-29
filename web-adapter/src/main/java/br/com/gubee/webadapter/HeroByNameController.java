package br.com.gubee.webadapter;

import br.com.gubee.api.in.ports.HeroesByNameUseCase;
import br.com.gubee.webadapter.dto.HeroDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroes")
public class HeroByNameController {
    private final HeroesByNameUseCase heroesByNameUseCase;

    public HeroByNameController(HeroesByNameUseCase heroesByNameUseCase) {
        this.heroesByNameUseCase = heroesByNameUseCase;
    }

    @GetMapping(value = "/search/{search}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public List<HeroDTO> findManyByName(@PathVariable String search) {
        return HeroDTO.toCollectionDTO(heroesByNameUseCase.findManyByName(search));
    }
}
