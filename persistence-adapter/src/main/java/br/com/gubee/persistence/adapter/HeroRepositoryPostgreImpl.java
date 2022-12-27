package br.com.gubee.persistence.adapter;

import br.com.gubee.api.out.GetHeroByIdPort;
import br.com.gubee.api.out.GetHeroesByNamePort;
import br.com.gubee.api.out.RegisterHeroPort;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.persistence.adapter.mapper.HeroRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class HeroRepositoryPostgreImpl implements RegisterHeroPort, GetHeroByIdPort, GetHeroesByNamePort {

    private static final String CREATE_HERO_QUERY = "INSERT INTO hero" +
        " (name, race, power_stats_id)" +
        " VALUES (:name, :race, :powerStatsId) RETURNING id";

    private static final String FIND_HERO_BY_UUID_QUERY = "SELECT *" +
            " FROM hero" +
            " WHERE id = :uuid";

    private static final String FIND_HERO_BY_NAME_QUERY = "SELECT *" +
            " FROM hero" +
            " WHERE name ilike :search";

    private static final String UPDATE_HERO_BY_ID_QUERY = "UPDATE hero" +
            " SET name = :name, race = :race, enabled = :enabled, updated_at = now()" +
            " WHERE id = :id";

    private static final String DELETE_HERO_QUERY = "DELETE FROM hero" +
            " WHERE id = :id";

    private static final String FIND_ALL_HEROES_QUERY = "SELECT *" +
            " FROM hero" +
            " ORDER BY hero.name";

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UUID registerHero(RegisterHeroRequest request, UUID uuid) {
        final Map<String, Object> params = Map.of("name", request.getName(),
            "race", request.getRace(),
            "powerStatsId", uuid);

        return namedParameterJdbcTemplate.queryForObject(
            CREATE_HERO_QUERY,
            params,
            UUID.class);
    }

    @Override
    public Optional<HeroModelApiOut> findById(UUID uuid) {
        final Map<String, Object> params = Map.of("uuid",uuid);

        HeroModelApiOut hero;

        try {
            hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_BY_UUID_QUERY,
                    params,
                    new HeroRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("More than one Hero was found.");
        }

        return Optional.ofNullable(hero);
    }

    @Override
    public List<HeroModelApiOut> findManyByName(String search) {
        final Map<String, Object> params = Map.of("search","%" + search + "%");

        return namedParameterJdbcTemplate.query(
                FIND_HERO_BY_NAME_QUERY,
                params,
                new HeroRowMapper()
        );
    }
//
//    @Override
//    public Optional<Hero> findByName(String heroName) {
//
//        final Map<String, Object> params = Map.of("heroName",heroName);
//
//        Hero hero;
//
//        try {
//            hero = namedParameterJdbcTemplate.queryForObject(
//                    FIND_HERO_BY_NAME_QUERY,
//                    params,
//                    new HeroRowMapper()
//            );
//        } catch (EmptyResultDataAccessException e) {
//            return Optional.empty();
//        } catch (IncorrectResultSizeDataAccessException e) {
//            throw new RuntimeException("More than one Hero was found.");
//        }
//
//        return Optional.ofNullable(hero);
//    }
//
//    @Override
//    public List<Hero> findAll() {
//        return namedParameterJdbcTemplate.query(
//                FIND_ALL_HEROES_QUERY,
//                new HeroRowMapper()
//        );
//    }
//
//    @Override
//    public void update(Hero hero, UpdateHeroRequest updateHeroRequest) {
//        Hero modifiedHero = changeFields(hero, updateHeroRequest);
//
//        final Map<String, Object> params = Map.of("name", modifiedHero.getName(),
//                "race", modifiedHero.getRace().name(),
//                "enabled", modifiedHero.isEnabled(),
//                "id", modifiedHero.getId());
//
//        namedParameterJdbcTemplate.update(
//                UPDATE_HERO_BY_ID_QUERY,
//                params
//        );
//    }
//
//    @Override
//    public void delete(Hero hero) {
//        final Map<String, Object> params = Map.of("id", hero.getId());
//
//        namedParameterJdbcTemplate.update(
//                DELETE_HERO_QUERY,
//                params
//        );
//    }
//
//    private Hero changeFields(Hero hero, UpdateHeroRequest updateHeroRequest) {
//        if (updateHeroRequest.getName() != null && !hero.getName().equals(updateHeroRequest.getName()))
//            hero.setName(updateHeroRequest.getName());
//        if (updateHeroRequest.getRace() != null && !hero.getRace().equals(updateHeroRequest.getRace()))
//            hero.setRace(updateHeroRequest.getRace());
//        if (updateHeroRequest.getEnabled() != null && !hero.isEnabled() == updateHeroRequest.getEnabled())
//            hero.setEnabled(updateHeroRequest.getEnabled());
//
//        return hero;
//    }
}
