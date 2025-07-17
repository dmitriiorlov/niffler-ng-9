package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public void create(AuthorityEntity... authority) {
        for (AuthorityEntity authorityEntity : authority) {
            try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
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
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        List<AuthorityEntity> authorityEntities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority")
        ) {
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    while (rs.next()) {
                        AuthorityEntity authority = new AuthorityEntity();
                        authorityEntities.add(authority);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return authorityEntities;
    }
}