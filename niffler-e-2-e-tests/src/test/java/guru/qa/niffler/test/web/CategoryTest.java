package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(BrowserExtension.class)
public class CategoryTest {
    private static final Logger LOG = LoggerFactory.getLogger(CategoryTest.class);
    private static final Config CONFIG = Config.getInstance();
    private static final String ADMIN_ACCOUNT = "duck";
    private static final String ADMIN_PWD = "12345";
    private final ProfilePage profilePage = new ProfilePage();

    @User(
            username = ADMIN_ACCOUNT,
            categories = @Category(
            )
    )
    @Test
    void activeCategoryShouldBeArchived(CategoryJson category) {
        String categoryName = category.name();
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
        profilePage.clickProfileButton()
                .checkMenuListIsPresent()
                .clickProfileLink()
                .checkProfileHeader()
                .archiveCategory(categoryName)
                .showArchivedCategories()
                .checkArchiveCategory(categoryName);
    }

    @User(
            username = ADMIN_ACCOUNT,
            categories = @Category(
                    archived = true
            )
    )
    @Test
    void archivedCategoryShouldBeUnarchived(CategoryJson category) {
        String categoryName = category.name();
        ProfilePage profilePage = new ProfilePage();
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
        profilePage.clickProfileButton()
                .checkMenuListIsPresent()
                .clickProfileLink()
                .checkProfileHeader()
                .showArchivedCategories()
                .unarchiveCategory(categoryName)
                .checkUnarchiveCategory(categoryName);
    }
}
