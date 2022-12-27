package br.com.gubee.persistence.adapter.mapper;

import br.com.gubee.api.out.model.HeroModelApiOut;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class HeroRowMapper implements RowMapper<HeroModelApiOut> {

    @Override
    public HeroModelApiOut mapRow(ResultSet rs, int rowNum) throws SQLException {
        return HeroModelApiOut.builder()
                .id((UUID) rs.getObject("id"))
                .name(rs.getString("name"))
                .race(rs.getString("race"))
                .powerStatsId((UUID) rs.getObject("power_stats_id"))
                .enabled(rs.getBoolean("enabled"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .build();
    }
}
