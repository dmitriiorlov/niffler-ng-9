package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

public class UserdataDbClient {
    private static final Config CFG = Config.getInstance();

    public UserEntity createUser(UserJson userJson) {
        return transaction(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    return userdataUserDao.createUser(UserEntity.fromJson(userJson));
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<UserEntity> findById(UUID id) {
        return transaction(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    return userdataUserDao.findById(id);
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public Optional<UserEntity> findByUserName(String username) {
        return transaction(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    return userdataUserDao.findByUserName(username);
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public void delete(UserJson user) {
        transaction(connection -> {
                    UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc(connection);
                    userdataUserDao.delete(UserEntity.fromJson(user));
                },
                CFG.userdataJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }
}
