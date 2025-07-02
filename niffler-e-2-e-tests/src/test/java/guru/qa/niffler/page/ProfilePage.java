package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.util.XpathUtil;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement profileButton = $x("//*[@aria-label='Menu']");
    private final SelenideElement menuList = $x("//ul[@role='menu']");
    private final SelenideElement profileLink = $x("//a[@href='/profile']");
    private final SelenideElement friendsLink = $x("//a[@href='/people/friends']");
    private final SelenideElement showArchiveToggle = $x("//span[contains(text(), 'Show archived')]");
    private final SelenideElement archiveButton = $x("//button[text()= 'Archive']");
    private final SelenideElement unarchiveButton = $x("//button[text()= 'Unarchive']");
    private final SelenideElement profileHeader = $x("//h2[text()='Profile']");


    public ProfilePage clickProfileButton() {
        profileButton.click();
        return this;
    }

    public ProfilePage clickProfileLink() {
        profileLink.click();
        return this;
    }

    public ProfilePage showArchivedCategories() {
        showArchiveToggle.click();
        return this;
    }

    public ProfilePage archiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Archive category']", category).click();
        archiveButton.should(visible).click();
        return this;
    }

    public ProfilePage unarchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Unarchive category']", category).click();
        unarchiveButton.should(visible).click();
        return this;
    }

    public FriendsPage clickFriendsLink() {
        friendsLink.click();
        return new FriendsPage();
    }

    public ProfilePage checkArchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Unarchive category']", category).should(visible);
        return this;
    }

    public ProfilePage checkUnarchiveCategory(String category) {
        XpathUtil.findByXpathTemplate("//*[text()='%s']/../..//button[@aria-label = 'Archive category']", category).should(visible);
        return this;
    }

    public ProfilePage checkProfileHeader() {
        profileHeader.should(visible);
        return this;
    }

    public ProfilePage checkMenuListIsPresent() {
        menuList.should(visible);
        return this;
    }
}
