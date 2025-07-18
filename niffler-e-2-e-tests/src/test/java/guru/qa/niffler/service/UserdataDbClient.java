package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.model.UdUserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

public class UserdataDbClient {
    private static final Config CFG = Config.getInstance();

    public UdUserEntity createUser(UdUserJson udUserJson) {
        return transaction(connection -> {
                    UdUserDao userdataUserDao = new UdUserDaoJdbc(connection);
                    return userdataUserDao.createUser(UdUserEntity.fromJson(udUserJson));
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<UdUserEntity> findById(UUID id) {
        return transaction(connection -> {
                    UdUserDao userdataUserDao = new UdUserDaoJdbc(connection);
                    return userdataUserDao.findById(id);
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public Optional<UdUserEntity> findByUserName(String username) {
        return transaction(connection -> {
                    UdUserDao userdataUserDao = new UdUserDaoJdbc(connection);
                    return userdataUserDao.findByUserName(username);
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public void delete(UdUserJson user) {
        transaction(connection -> {
                    UdUserDao userdataUserDao = new UdUserDaoJdbc(connection);
                    userdataUserDao.delete(UdUserEntity.fromJson(user));
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }
}
