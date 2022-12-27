package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.HeroesByNameUseCase;
import br.com.gubee.webadapter.dto.HeroDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroes")
public class HeroByNameController {
    private final HeroesByNameUseCase heroesByNameUseCase;

    public HeroByNameController(HeroesByNameUseCase heroesByNameUseCase) {
        this.heroesByNameUseCase = heroesByNameUseCase;
    }

    @GetMapping(value = "/search/{search}", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<HeroDTO> findManyByName(@PathVariable String search) {
        List<HeroModelApiIn> heroes = heroesByNameUseCase.findManyByName(search);

        return HeroDTO.toCollectionDTO(heroes);
    }
}
