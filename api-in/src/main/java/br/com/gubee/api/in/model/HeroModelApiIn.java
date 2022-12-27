package br.com.gubee.api.in.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

@Builder
@Getter
public class HeroModelApiIn {
    private UUID id;
    private String name;
    private String race;
    private Instant createdAt;
    private Instant updatedAt;
    private boolean enabled;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
}
