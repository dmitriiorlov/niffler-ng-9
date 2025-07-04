package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPage {
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement submitButton = $("button[type='submit']");
    private final SelenideElement registerButton = $("#register-button");
    private final SelenideElement registerForm = $("#register-form");
    private final SelenideElement badCredentialsMessage =  $x("//p[text()='Неверные учетные данные пользователя']");


    public LoginPage fillLoginPage(String username, String password) {
        usernameInput.setValue(username);
        passwordInput.setValue(password);
        return this;
    }

    public MainPage submit() {
        submitButton.click();
        return new MainPage();
    }

    public RegisterPage createAccount() {
        registerButton.click();
        return new RegisterPage();
    }

    public LoginPage checkRegisterForm() {
        registerForm.should(visible);
        return this;
    }

    public LoginPage checkBadCredentialsMessage() {
        badCredentialsMessage.should(visible);
        return this;
    }
}
