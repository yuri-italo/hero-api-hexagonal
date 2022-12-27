package br.com.gubee.persistence.adapter.mapper;

import br.com.gubee.api.out.model.PowerStatsModelApiOut;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PowerStatsRowMapper implements RowMapper<PowerStatsModelApiOut> {
    @Override
    public PowerStatsModelApiOut mapRow(ResultSet rs, int rowNum) throws SQLException {
        return PowerStatsModelApiOut.builder()
                .id((UUID) rs.getObject("id"))
                .strength(rs.getInt("strength"))
                .agility(rs.getInt("agility"))
                .dexterity(rs.getInt("dexterity"))
                .intelligence(rs.getInt("intelligence"))
                .createdAt(rs.getTimestamp("created_at").toInstant())
                .updatedAt(rs.getTimestamp("updated_at").toInstant())
                .build();
    }
}
