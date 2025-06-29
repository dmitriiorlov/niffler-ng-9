package guru.qa.niffler.test;

import com.codeborne.selenide.Selenide;
import com.github.javafaker.Faker;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.util.DataUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ExtendWith(BrowserExtension.class)
public class RegisterTest {
    private static final Logger LOG = LoggerFactory.getLogger(RegisterTest.class);
    private static final Config CONFIG = Config.getInstance();
    private static final Faker FAKER = new Faker();
    private static final String ADMIN_ACCOUNT = "duck";
    private static final String ADMIN_PWD = "12345";
    private static final String PWD_TEMPLATE = "?????";
    private LoginPage loginPage = new LoginPage();

    @Test
    void shouldRegisterNewUser() {
        String password = FAKER.letterify(PWD_TEMPLATE);
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(DataUtil.generateUniqueName())
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkSuccessRegistration();
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        StringBuilder password = new StringBuilder(FAKER.letterify(PWD_TEMPLATE));
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(ADMIN_ACCOUNT)
                .setPassword(password.toString())
                .setPasswordSubmit(password.toString())
                .submitRegistration()
                .checkUsernameAlreadyExistMessage(ADMIN_ACCOUNT);
    }

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordAreNotEqual() {
        StringBuilder password = new StringBuilder(FAKER.letterify(PWD_TEMPLATE));
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .createAccount()
                .setUsername(DataUtil.generateUniqueName())
                .setPassword(password.toString())
                .setPasswordSubmit(password.reverse().toString())
                .submitRegistration()
                .checkPasswordNotEqualMessage();
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_ACCOUNT, ADMIN_PWD)
                .submit()
                .checkThatPageLoaded();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        Selenide.open(CONFIG.frontUrl(), LoginPage.class)
                .fillLoginPage(ADMIN_PWD, ADMIN_ACCOUNT)
                .submit();
        loginPage.checkBadCredentialsMessage();
    }
}