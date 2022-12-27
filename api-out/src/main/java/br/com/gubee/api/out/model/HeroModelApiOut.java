package br.com.gubee.api.out.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class HeroModelApiOut {
    private UUID id;

    private String name;

    private String race;

    private UUID powerStatsId;

    private Instant createdAt;

    private Instant updatedAt;

    private boolean enabled;
}
