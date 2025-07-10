package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private final Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public List<AuthorityEntity> create(AuthorityEntity... authority) {
        List<AuthorityEntity> ae = new ArrayList<>();
        for (AuthorityEntity authorityEntity : authority) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO authority (user_id, authority) " +
                            "VALUES (?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, authorityEntity.getUserId());
                ps.setString(2, authorityEntity.getAuthority().name());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                authorityEntity.setId(generatedKey);
                ae.add(authorityEntity);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return ae;
    }
}
