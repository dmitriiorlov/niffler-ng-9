package guru.qa.niffler.service;

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

public class SpendDbClient {

  private final SpendDao spendDao = new SpendDaoJdbc();
  private final CategoryDao categoryDao = new CategoryDaoJdbc();

  public SpendJson createSpend(SpendJson spend) {
    SpendEntity spendEntity = SpendEntity.fromJson(spend);
    if (spendEntity.getCategory().getId() == null) {
      CategoryEntity categoryEntity = categoryDao.create(spendEntity.getCategory());
      spendEntity.setCategory(categoryEntity);
    }
    return SpendJson.fromEntity(
        spendDao.create(spendEntity)
    );
  }

  public Optional<SpendEntity> findSpendById(UUID id) {
    return spendDao.findSpendById(id);
  }

  public List<SpendEntity> findAllByUsername(String username) {
    return spendDao.findAllByUsername(username);
  }

  public void deleteSpend(SpendEntity spend) {
    spendDao.deleteSpend(spend);
  }

  public CategoryEntity createCategory(CategoryJson categoryJson) {
    CategoryEntity categoryEntity = CategoryEntity.fromJson(categoryJson);
    return categoryDao.create(categoryEntity);
  }

  public List<CategoryEntity> findAllByUserName(String username) {
    return categoryDao.findAllByUsername(username);
  }

  public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
    return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
  }

  public void deleteCategory(CategoryEntity category) {
    categoryDao.deleteCategory(category);
  }
}
