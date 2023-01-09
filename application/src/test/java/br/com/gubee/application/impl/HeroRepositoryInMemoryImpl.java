package br.com.gubee.application.impl;


import br.com.gubee.api.out.*;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.UpdateHeroRequestApiOut;
import br.com.gubee.configuration.Hero;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.*;

@RequiredArgsConstructor
public class HeroRepositoryInMemoryImpl implements
        RegisterHeroPort, GetHeroByIdPort, GetHeroesByNamePort, FindHeroByNamePort, ListHeroesPort, UpdateHeroPort,
        DeleteHeroByIdPort
{
    public static Map<UUID, HeroModelApiOut> heroStorage = new HashMap<>();
    @Override
    public UUID registerHero(RegisterHeroRequest request, UUID powerStatsId) {

        for(Map.Entry<UUID,HeroModelApiOut> entry : heroStorage.entrySet())
            if (entry.getValue().getName().equals(request.getName()))
                throw new IllegalArgumentException();

        HeroModelApiOut heroModelApiOut = createHeroModelApiOut(request, powerStatsId);

        heroStorage.put(heroModelApiOut.getId(),heroModelApiOut);

        return heroModelApiOut.getId();
    }

    @Override
    public Optional<HeroModelApiOut> findById(UUID uuid) {
        return Optional.ofNullable(heroStorage.get(uuid));
    }

    @Override
    public List<HeroModelApiOut> findManyByName(String search) {
        List<HeroModelApiOut> heroes = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : heroStorage.entrySet())
            if (entry.getValue().getName().contains(search))
                heroes.add(entry.getValue());

        return heroes;
    }

    @Override
    public Optional<HeroModelApiOut> findByName(String search) {
        HeroModelApiOut hero = null;

        for(Map.Entry<UUID,HeroModelApiOut> entry : heroStorage.entrySet())
            if (entry.getValue().getName().equalsIgnoreCase(search))
                hero = entry.getValue();

        return Optional.ofNullable(hero);
    }

    @Override
    public List<HeroModelApiOut> findAll() {
        List<HeroModelApiOut> heroes = new ArrayList<>();

        for(Map.Entry<UUID,HeroModelApiOut> entry : heroStorage.entrySet())
            heroes.add(entry.getValue());

        return heroes;
    }

    @Override
    public void update(UUID uuid, UpdateHeroRequestApiOut request) {
        HeroModelApiOut heroModelApiOut = heroStorage.get(uuid);

        HeroModelApiOut modifiedHero = HeroModelApiOut.builder()
                .id(heroModelApiOut.getId())
                .name(request.getName())
                .race(request.getRace())
                .enabled(request.isEnabled())
                .createdAt(heroModelApiOut.getCreatedAt())
                .updatedAt(Instant.now())
                .powerStatsId(heroModelApiOut.getPowerStatsId())
                .build();

        heroStorage.put(uuid,modifiedHero);
    }

    @Override
    public void delete(UUID uuid) {
        heroStorage.remove(uuid);
    }

    private HeroModelApiOut createHeroModelApiOut(RegisterHeroRequest request, UUID powerStatsId) {
        Hero hero = new Hero(request.getName(),request.getRace(),powerStatsId);
        UUID uuid = UUID.randomUUID();
        Instant now = Instant.now();

        hero.setId(uuid);
        hero.setCreatedAt(now);
        hero.setUpdatedAt(now);
        hero.setEnabled(true);

        return HeroModelApiOut.builder()
                .id(hero.getId())
                .name(hero.getName())
                .race(hero.getRace().toString())
                .createdAt(hero.getCreatedAt())
                .updatedAt(hero.getUpdatedAt())
                .enabled(hero.isEnabled())
                .powerStatsId(hero.getPowerStatsId())
                .build();
    }
}
