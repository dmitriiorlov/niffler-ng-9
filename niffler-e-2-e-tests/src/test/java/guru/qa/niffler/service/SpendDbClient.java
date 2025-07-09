package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    public SpendJson createSpend(SpendJson spend) {
        return transaction(connection -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = new CategoryDaoJdbc(connection)
                                .create(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            new SpendDaoJdbc(connection).create(spendEntity)
                    );
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return transaction(connection -> {
                    SpendDao spendDao = new SpendDaoJdbc(connection);
                    return spendDao.findSpendById(id);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public List<SpendEntity> findAllByUsername(String username) {
        return transaction(connection -> {
                    SpendDao spendDao = new SpendDaoJdbc(connection);
                    return spendDao.findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public void deleteSpend(SpendEntity spend) {
        transaction(connection -> {
                    SpendDao spendDao = new SpendDaoJdbc(connection);
                    spendDao.deleteSpend(spend);
                },
                CFG.spendJdbcUrl(), TRANSACTION_REPEATABLE_READ);
    }

    public CategoryEntity createCategory(CategoryJson categoryJson) {
        return transaction(connection -> {
                    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
                    CategoryDao categoryDao = new CategoryDaoJdbc(connection);
                    return categoryDao.create(categoryEntity);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public List<CategoryEntity> findAllCategoriesByUserName(String username) {
        return transaction(connection -> {
                    CategoryDao categoryDao = new CategoryDaoJdbc(connection);
                    return categoryDao.findAllByUsername(username);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return transaction(connection -> {
                    CategoryDao categoryDao = new CategoryDaoJdbc(connection);
                    return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }

    public void deleteCategory(CategoryEntity category) {
        transaction(connection -> {
                    CategoryDao categoryDao = new CategoryDaoJdbc(connection);
                    categoryDao.deleteCategory(category);
                },
                CFG.spendJdbcUrl(),
                TRANSACTION_REPEATABLE_READ);
    }
}
