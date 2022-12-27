package br.com.gubee.api.out.model;

import lombok.Builder;
import lombok.Getter;

import java.time.Instant;
import java.util.UUID;
@Builder
@Getter
public class PowerStatsModelApiOut {
    private UUID id;
    private int strength;
    private int agility;
    private int dexterity;
    private int intelligence;
    private Instant createdAt;
    private Instant updatedAt;
}
