package br.com.gubee.webadapter;

import br.com.gubee.api.in.model.HeroModelApiIn;
import br.com.gubee.api.in.ports.ListHeroesUseCase;
import br.com.gubee.webadapter.dto.ListHeroDTO;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/heroes")
public class ListHeroesController {
    private final ListHeroesUseCase listHeroesUseCase;

    public ListHeroesController(ListHeroesUseCase listHeroesUseCase) {
        this.listHeroesUseCase = listHeroesUseCase;
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ListHeroDTO> list() {
        List<HeroModelApiIn> heroes = listHeroesUseCase.findAll();
        return ListHeroDTO.toCollectionDTO(heroes);
    }
}
