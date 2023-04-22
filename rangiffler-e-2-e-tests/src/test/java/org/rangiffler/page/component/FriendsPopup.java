package org.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import io.qameta.allure.Step;
import org.rangiffler.model.UserGrpc;

import java.util.List;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static org.rangiffler.condition.friends.FriendCondition.friends;

public class FriendsPopup extends BaseComponent<FriendsPopup> {

    public FriendsPopup() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final ElementsCollection friendsTable = self.$$("tbody tr");

    @Step("Check that friends list contains data {0}")
    public FriendsPopup checkTableContains(List <UserGrpc> expectedFriends) {
        friendsTable.should(friends(expectedFriends));
        return this;
    }

}
