package org.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.rangiffler.model.UserGrpc;

import java.util.List;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.rangiffler.condition.friends.FriendCondition.friends;

public class FriendsPopup extends BaseComponent<FriendsPopup> {

    public FriendsPopup() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final ElementsCollection friendsTable = self.$$("tbody tr");
    private final String removeFriendIcon = "button[aria-label='Remove friend']";

    private final SelenideElement deleteButton = $(byText("Delete"));

    @Step("Check that friends list contains data {0}")
    public FriendsPopup checkTableContainsFriends(List<UserGrpc> expectedFriends) {
        friendsTable.shouldHave(friends(expectedFriends));
        return this;
    }

    @Step("Check the message if there are no friends")
    public FriendsPopup checkTableNotContainsFriends() {
        self.shouldHave(text("No friends yet"));
        return this;
    }

    @Step("Remove friend {username}")
    public FriendsPopup removeFriend(String username) {
        friendsTable.filter(text(username)).first().$(removeFriendIcon).click();
        deleteButton.click();
        return this;
    }

}
