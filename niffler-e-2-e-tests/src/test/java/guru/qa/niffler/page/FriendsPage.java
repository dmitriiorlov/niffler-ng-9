package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {

    private final SelenideElement friendsHeader = $x("//h2[text()='Friends']");
    private final ElementsCollection friendsList = $$x("//*[@id='friends']/tr");
    private final SelenideElement acceptFriendButton = $x("//button[text()='Accept']");
    private final SelenideElement declineFriendButton = $x("//button[text()='Decline']");
    private final SelenideElement waitingApproveLabel = $x("//span[text()='Waiting...']");
    private final SelenideElement allPeopleLink = $x("//a[@href='/people/all']");

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