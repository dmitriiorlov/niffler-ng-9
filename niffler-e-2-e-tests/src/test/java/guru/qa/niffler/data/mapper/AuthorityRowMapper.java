package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class AuthorityRowMapper implements RowMapper<AuthorityEntity> {

    public static final AuthorityRowMapper instance = new AuthorityRowMapper();

    private AuthorityRowMapper() {
    }

    @Override
    public AuthorityEntity mapRow(ResultSet rs, int rowNum) throws SQLException {
        AuthorityEntity entity = new AuthorityEntity();
        entity.setId(rs.getObject("id", UUID.class));
        entity.setUserId(rs.getObject("userId", UUID.class));
        entity.setAuthority(rs.getObject("authority", Authority.class));
        return entity;
    }
}