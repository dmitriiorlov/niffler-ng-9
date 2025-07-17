package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    public AuthUserEntity createUser(AuthUserEntity user) {
        return transaction(connection -> {
                    AuthUserDao userDao = new AuthUserDaoJdbc(connection);
                    AuthUserEntity ue = userDao.create(user);
                    AuthAuthorityDao authorityDao = new AuthAuthorityDaoJdbc(connection);
                    user.getAuthorities().forEach(authority -> authority.setUserId(user.getId()));
                    authorityDao.create(user.getAuthorities().toArray(new AuthorityEntity[0]));
                    return ue;
                },
                CFG.authJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }

    public void createAuthorities(AuthorityEntity... authorities) {
        transaction(connection -> {
                    AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc(connection);
                    authAuthorityDao.create(authorities);
                },
                CFG.authJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }
}
