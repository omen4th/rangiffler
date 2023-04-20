package org.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.rangiffler.page.FriendsTravelsPage;
import org.rangiffler.page.MainPage;
import org.rangiffler.page.PeopleAroundPage;

public class NavigationPanel extends BaseComponent<NavigationPanel> {
    public NavigationPanel(SelenideElement self) {
        super(self);
    }

    private final SelenideElement yourTravelsTab = self.$("button[id*='-main']");
    private final SelenideElement friendsTravelsTab = self.$("button[id*='-friends']");
    private final SelenideElement peopleAroundTab = self.$("button[id*='-all']");

    @Step("Select the Your travels tab")
    public MainPage toYourTravelsPage() {
        yourTravelsTab.click();
        return new MainPage();
    }

    @Step("Select the Friends travels tab")
    public FriendsTravelsPage toFriendsTravelsPage() {
        friendsTravelsTab.click();
        return new FriendsTravelsPage();
    }

    @Step("Select the People around page")
    public PeopleAroundPage toPeopleAroundPage() {
        peopleAroundTab.click();
        return new PeopleAroundPage();
    }
}
