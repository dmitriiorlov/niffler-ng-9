package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.util.XpathUtil;

import static com.codeborne.selenide.Condition.exist;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class RegisterPage {
    private final SelenideElement usernameInput = $("#username");
    private final SelenideElement passwordInput = $("#password");
    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");
    private final SelenideElement signUpButton = $("#register-button");
    private final SelenideElement signInButton = $("a.form_sign-in");
    private final SelenideElement passwordNotEqualSpan = $x("//span[text()='Passwords should be equal']");


    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);
        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);
        return this;
    }

    public RegisterPage setPasswordSubmit(String password) {
        passwordSubmitInput.setValue(password);
        return this;
    }

    public RegisterPage submitRegistration() {
        signUpButton.click();
        return this;
    }

    public RegisterPage checkSuccessRegistration() {
        signInButton.should(visible);
        return this;
    }

    public RegisterPage checkPasswordNotEqualMessage() {
        passwordNotEqualSpan.should(visible);
        return this;
    }

    public RegisterPage checkUsernameAlreadyExistMessage(String username) {
        XpathUtil.findByXpathTemplate("//span[text()='Username `%s` already exists']", username).should(exist);
        return this;
    }

}