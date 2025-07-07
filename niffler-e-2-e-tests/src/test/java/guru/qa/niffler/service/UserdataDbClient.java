package guru.qa.niffler.service;

import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.UserJson;

import java.util.Optional;
import java.util.UUID;

public class UserdataDbClient {
    private final UserdataUserDao userdataUserDao = new UserdataUserDaoJdbc();

    public UserEntity createUser(UserJson userJson) {
        UserEntity userEntity = UserEntity.fromJson(userJson);
        return userdataUserDao.createUser(userEntity);
    }

    public Optional<UserEntity> findById(UUID id) {
        return userdataUserDao.findById(id);
    }

    public Optional<UserEntity> findByUserName(String username) {
        return userdataUserDao.findByUserName(username);
    }

    public void delete(UserJson user) {
        UserEntity userEntity = UserEntity.fromJson(user);
        userdataUserDao.delete(userEntity);
    }
}
