package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.By;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class FriendsPage {

    private final SelenideElement friendsHeader = $(By.xpath("//h2[text()='Friends']"));
    private final ElementsCollection friendsList = $$(By.xpath("//*[@id='friends']/tr"));
    private final SelenideElement acceptFriendButton = $(By.xpath("//button[text()='Accept']"));
    private final SelenideElement declineFriendButton = $(By.xpath("//button[text()='Decline']"));
    private final SelenideElement waitingApproveLabel = $(By.xpath("//span[text()='Waiting...']"));
    private final SelenideElement allPeopleLink = $(By.xpath("//a[@href='/people/all']"));

    public FriendsPage checkThatPageLoaded() {
        friendsHeader.shouldBe(visible);
        return this;
    }

    public FriendsPage openAllPeopleList() {
        allPeopleLink.click();
        return this;
    }

    public FriendsPage checkAcceptFriendButton() {
        acceptFriendButton.shouldBe(visible);
        return this;
    }

    public FriendsPage checkDeclineFriendButton() {
        declineFriendButton.shouldBe(visible);
        return this;
    }

    public FriendsPage checkWaitingStatus() {
        waitingApproveLabel.shouldBe(visible);
        return this;
    }

    public FriendsPage checkFriendsListCount(int expectedCount) {
        friendsList.shouldHave(size(expectedCount));
        return this;
    }
}