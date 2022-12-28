package br.com.gubee.persistence.adapter;

import br.com.gubee.api.out.*;
import br.com.gubee.api.out.model.HeroModelApiOut;
import br.com.gubee.api.out.requests.RegisterHeroRequest;
import br.com.gubee.api.out.requests.UpdateHeroRequestApiOut;
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
public class HeroRepositoryPostgreImpl
        implements RegisterHeroPort, GetHeroByIdPort, GetHeroesByNamePort, ListHeroesPort, FindHeroByNamePort,
        UpdateHeroPort, DeleteHeroByIdPort
{

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

    @Override
    public Optional<HeroModelApiOut> findByName(String search) {

        final Map<String, Object> params = Map.of("search",search);

        HeroModelApiOut hero;

        try {
            hero = namedParameterJdbcTemplate.queryForObject(
                    FIND_HERO_BY_NAME_QUERY,
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
    public List<HeroModelApiOut> findAll() {
        return namedParameterJdbcTemplate.query(
                FIND_ALL_HEROES_QUERY,
                new HeroRowMapper()
        );
    }

    @Override
    public void update(UUID uuid, UpdateHeroRequestApiOut updateHeroRequestApiOut) {

        final Map<String, Object> params = Map.of("name", updateHeroRequestApiOut.getName(),
                "race", updateHeroRequestApiOut.getRace(),
                "enabled", updateHeroRequestApiOut.isEnabled(),
                "id", uuid);

        namedParameterJdbcTemplate.update(
                UPDATE_HERO_BY_ID_QUERY,
                params
        );
    }

    @Override
    public void delete(UUID heroId) {
        final Map<String, Object> params = Map.of("id", heroId);

        namedParameterJdbcTemplate.update(
                DELETE_HERO_QUERY,
                params
        );
    }
}
