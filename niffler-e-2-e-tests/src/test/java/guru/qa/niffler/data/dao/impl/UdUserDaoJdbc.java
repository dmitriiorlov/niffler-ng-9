package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserDaoJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();

    @Override
    public UdUserEntity createUser(UdUserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (currency, firstname, full_name, photo, photo_small, surname, username) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getCurrency().name());
            ps.setString(2, user.getFirstname());
            ps.setString(3, user.getFullname());
            ps.setBytes(4, user.getPhoto());
            ps.setBytes(5, user.getPhotoSmall());
            ps.setString(6, user.getSurname());
            ps.setString(7, user.getUsername());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    generatedKey = rs.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
                user.setId(generatedKey);
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UdUserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UdUserEntity ue = new UdUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    ue.setFirstname(rs.getString("firstname"));
                    ue.setFullname(rs.getString("full_name"));
                    ue.setPhoto(rs.getBytes("photo"));
                    ue.setPhotoSmall(rs.getBytes("photo_small"));
                    ue.setSurname(rs.getString("surname"));
                    ue.setUsername(rs.getString("username"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UdUserEntity> findByUserName(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setObject(1, username);
            ps.execute();
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    UdUserEntity ue = new UdUserEntity();
                    ue.setId(rs.getObject("id", UUID.class));
                    ue.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                    ue.setFirstname(rs.getString("firstname"));
                    ue.setFullname(rs.getString("full_name"));
                    ue.setPhoto(rs.getBytes("photo"));
                    ue.setPhotoSmall(rs.getBytes("photo_small"));
                    ue.setSurname(rs.getString("surname"));
                    ue.setUsername(rs.getString("username"));
                    return Optional.of(ue);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(UdUserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UdUserEntity> findAll() {
        List<UdUserEntity> udUserEntities = new ArrayList<>();
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\"")) {
            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    while (rs.next()) {
                        UdUserEntity user = new UdUserEntity();
                        user.setId(rs.getObject("id", UUID.class));
                        user.setUsername(rs.getString("username"));
                        user.setCurrency(CurrencyValues.valueOf(rs.getString("currency")));
                        user.setFirstname(rs.getString("firstname"));
                        user.setSurname(rs.getString("surname"));
                        user.setPhoto(rs.getBytes("photo"));
                        user.setPhotoSmall(rs.getBytes("photo_small"));
                        user.setFullname(rs.getString("full_name"));
                        udUserEntities.add(user);
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return udUserEntities;
    }
}
