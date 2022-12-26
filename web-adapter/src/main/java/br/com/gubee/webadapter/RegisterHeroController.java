package br.com.gubee.webadapter;

import br.com.gubee.api.in.ports.RegisterHeroUseCase;
import br.com.gubee.api.in.requests.CreateHeroRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.UUID;

import static java.lang.String.format;

@RestController
@RequestMapping("/api/v1/heroes")
public class RegisterHeroController {
    private final RegisterHeroUseCase registerHeroUseCase;

    public RegisterHeroController(RegisterHeroUseCase registerHeroUseCase) {
        this.registerHeroUseCase = registerHeroUseCase;
    }

    @PostMapping
    public ResponseEntity<?> registerHero(@RequestBody CreateHeroRequest createHeroRequest) {
        UUID uuid = registerHeroUseCase.registerHero(createHeroRequest);
        return ResponseEntity.created(URI.create(format("/api/v1/heroes/%s", uuid))).build();
    }
}
