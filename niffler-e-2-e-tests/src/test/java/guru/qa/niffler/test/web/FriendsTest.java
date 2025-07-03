package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.ProfilePage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;

@ExtendWith({UsersQueueExtension.class, BrowserExtension.class})
public class FriendsTest {
    private static final Config CFG = Config.getInstance();
    private static final ProfilePage profilePage = new ProfilePage();

    @Test
    void friendShouldBePresentInFriendsTable(@UserType(UserType.Type.WITH_FRIEND) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit();
        profilePage.clickProfileButton()
                .clickFriendsLink()
                .checkThatPageLoaded()
                .checkFriendsListCount(1);
    }

    @Test
    void friendsTableShouldBeEmptyForNewUser(@UserType(UserType.Type.EMPTY) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit();
        profilePage.clickProfileButton()
                .clickFriendsLink()
                .checkThatPageLoaded()
                .checkFriendsListCount(0);
    }

    @Test
    void incomeInvitationBePresentInFriendsTable(@UserType(UserType.Type.WITH_INCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit();
        profilePage.clickProfileButton()
                .clickFriendsLink()
                .checkThatPageLoaded()
                .checkAcceptFriendButton()
                .checkDeclineFriendButton();
    }

    @Test
    void outcomeInvitationBePresentInAllPeopleTable(@UserType(UserType.Type.WITH_OUTCOME_REQUEST) StaticUser user) {
        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .fillLoginPage(user.username(), user.password())
                .submit();
        profilePage.clickProfileButton()
                .clickFriendsLink()
                .checkThatPageLoaded()
                .openAllPeopleList()
                .checkWaitingStatus();
    }


}
