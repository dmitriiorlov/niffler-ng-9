package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UdUserEntity;
import guru.qa.niffler.data.tpl.DataSources;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UdUserJson;
import org.springframework.data.transaction.ChainedTransactionManager;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.Arrays;


public class UsersDbClient {

    private static final Config CFG = Config.getInstance();
    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoJdbc();
    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoJdbc();
    private final UdUserDao udUserDao = new UdUserDaoJdbc();

    private final AuthUserDao authUserDaoSpring = new AuthUserDaoSpringJdbc();
    private final AuthAuthorityDao authAuthorityDaoSpring = new AuthAuthorityDaoSpringJdbc();
    private final UdUserDao udUserDaoSpring = new UdUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    DataSources.dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    private final TransactionTemplate chainedTransactionTemplate = new TransactionTemplate(
            new ChainedTransactionManager(
                    new JdbcTransactionManager(
                            DataSources.dataSource(CFG.authJdbcUrl())
                    ),
                    new JdbcTransactionManager(
                            DataSources.dataSource(CFG.userdataJdbcUrl())
                    )
            )
    );

    public UdUserJson createUserJdbcXa(UdUserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.create(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUserId(createdAuthUser.getId());
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    authAuthorityDao.create(authorityEntities);
                    return UdUserJson.fromEntity(
                            udUserDao.createUser(UdUserEntity.fromJson(user))
                    );
                }
        );
    }

    public UdUserJson createUserJdbcTx(UdUserJson user) {
        return txTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
                    .map(e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    })
                    .toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(authorityEntities);

            return UdUserJson.fromEntity(udUserDao.createUser(UdUserEntity.fromJson(user)));
        });
    }

    public UdUserJson createUserJdbcChainedTx(UdUserJson user) {
        return chainedTransactionTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDao.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);

            authAuthorityDao.create(authorityEntities);
            return UdUserJson.fromEntity(
                    udUserDao.createUser(UdUserEntity.fromJson(user))
            );
        });
    }

    public UdUserJson createUserJdbc(UdUserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = authUserDao.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);
        authAuthorityDao.create(authorityEntities);
        return UdUserJson.fromEntity(udUserDao.createUser(UdUserEntity.fromJson(user)));
    }

    public UdUserJson createUserSpringJdbcXa(UdUserJson user) {
        return xaTransactionTemplate.execute(() -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);

            authAuthorityDaoSpring.create(authorityEntities);
            return UdUserJson.fromEntity(
                    udUserDaoSpring.createUser(UdUserEntity.fromJson(user))
            );
        });
    }

    public UdUserJson createUserSpringJdbcChainedTx(UdUserJson user) {
        return chainedTransactionTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                    e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    }
            ).toArray(AuthorityEntity[]::new);

            authAuthorityDaoSpring.create(authorityEntities);
            return UdUserJson.fromEntity(
                    udUserDaoSpring.createUser(UdUserEntity.fromJson(user))
            );
        });
    }

    public UdUserJson createUserSpringJdbcTx(UdUserJson user) {
        return txTemplate.execute(status -> {
            AuthUserEntity authUser = new AuthUserEntity();
            authUser.setUsername(user.username());
            authUser.setPassword(pe.encode("12345"));
            authUser.setEnabled(true);
            authUser.setAccountNonExpired(true);
            authUser.setAccountNonLocked(true);
            authUser.setCredentialsNonExpired(true);

            AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

            AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values())
                    .map(e -> {
                        AuthorityEntity ae = new AuthorityEntity();
                        ae.setUserId(createdAuthUser.getId());
                        ae.setAuthority(e);
                        return ae;
                    })
                    .toArray(AuthorityEntity[]::new);

            authAuthorityDaoSpring.create(authorityEntities);

            return UdUserJson.fromEntity(udUserDaoSpring.createUser(UdUserEntity.fromJson(user)));
        });
    }

    public UdUserJson createUserSpringJdbc(UdUserJson user) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(user.username());
        authUser.setPassword(pe.encode("12345"));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        AuthUserEntity createdAuthUser = authUserDaoSpring.create(authUser);

        AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                e -> {
                    AuthorityEntity ae = new AuthorityEntity();
                    ae.setUserId(createdAuthUser.getId());
                    ae.setAuthority(e);
                    return ae;
                }
        ).toArray(AuthorityEntity[]::new);
        authAuthorityDaoSpring.create(authorityEntities);
        return UdUserJson.fromEntity(udUserDaoSpring.createUser(UdUserEntity.fromJson(user)));
    }
}
