package br.com.gubee.persistence.adapter;

import br.com.gubee.api.out.*;
import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import br.com.gubee.api.out.requests.RegisterPowerStatsRequest;
import br.com.gubee.api.out.requests.UpdatePowerStatsRequestApiOut;
import br.com.gubee.persistence.adapter.mapper.PowerStatsRowMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Repository
@RequiredArgsConstructor
public class PowerStatsRepositoryPostgreImpl implements RegisterPowerStatsPort,
        GetPowerStatsByIdPort, ListPowerStatsPort, UpdatePowerStatsPort, DeletePowerStatsByIdPort
{

    private static final String CREATE_POWER_STATS_QUERY = "INSERT INTO power_stats" +
        " (strength, agility, dexterity, intelligence)" +
        " VALUES (:strength, :agility, :dexterity, :intelligence) RETURNING id";

    private static final String FIND_POWER_STATS_BY_UUID_QUERY = "SELECT *" +
            " FROM power_stats" +
            " WHERE id = :uuid";

    private static final String FIND_ALL_POWER_STATS_QUERY = "SELECT *" +
            " FROM power_stats";

    private static final String UPDATE_POWER_STATS_BY_ID_QUERY = "UPDATE power_stats" +
            " SET strength = :strength," +
            " agility = :agility," +
            " dexterity = :dexterity," +
            " intelligence = :intelligence," +
            " updated_at = now()" +
            " WHERE id = :id";

    private static final String DELETE_POWER_STATS_BY_ID_QUERY = "DELETE FROM power_stats" +
            " WHERE id = :id";
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    @Override
    public UUID registerPowerStats(RegisterPowerStatsRequest request) {
        return namedParameterJdbcTemplate.queryForObject(
            CREATE_POWER_STATS_QUERY,
            new BeanPropertySqlParameterSource(request),
            UUID.class);
    }

    @Override
    public PowerStatsModelApiOut findById(UUID powerStatsId) {
        final Map<String, Object> params = Map.of("uuid",powerStatsId);

        PowerStatsModelApiOut powerStats;

        try {
            powerStats = namedParameterJdbcTemplate.queryForObject(
                    FIND_POWER_STATS_BY_UUID_QUERY,
                    params,
                    new PowerStatsRowMapper()
            );
        } catch (EmptyResultDataAccessException e) {
            return null;
        } catch (IncorrectResultSizeDataAccessException e) {
            throw new RuntimeException("More than one PowerStats was found.");
        }

        return powerStats;
    }

    @Override
    public List<PowerStatsModelApiOut> findAll() {
        return namedParameterJdbcTemplate.query(
                FIND_ALL_POWER_STATS_QUERY,
                new PowerStatsRowMapper()
        );
    }

    @Override
    public void update(UUID uuid, UpdatePowerStatsRequestApiOut updatePowerStatsRequestApiOut) {
        final Map<String, Object> params = Map.of("strength", updatePowerStatsRequestApiOut.getStrength(),
                "agility", updatePowerStatsRequestApiOut.getAgility(),
                "dexterity", updatePowerStatsRequestApiOut.getDexterity(),
                "intelligence", updatePowerStatsRequestApiOut.getIntelligence(),
                "id", uuid);

        namedParameterJdbcTemplate.update(
                UPDATE_POWER_STATS_BY_ID_QUERY,
                params
        );
    }

    @Override
    public void delete(UUID powerStatsId) {
        final Map<String, Object> params = Map.of("id", powerStatsId);

        namedParameterJdbcTemplate.update(
                DELETE_POWER_STATS_BY_ID_QUERY,
                params
        );
    }
}
