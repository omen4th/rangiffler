package org.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("header.MuiAppBar-root"));
    }

    private final SelenideElement addPhotoButton = self.$("button");
    private final SelenideElement profileIcon = self.$(".MuiAvatar-root");
    private final SelenideElement visitedCountriesIcon = self.$("div[aria-label='Your visited countries']");
    private final SelenideElement yourPhotosIcon = self.$("div[aria-label='Your photos']");
    private final SelenideElement yourFriendsIcon = self.$("div[aria-label='Your friends']");
    private final SelenideElement logoutIcon = self.$("div[aria-label='Logout']");

    public ProfilePopup openProfilePopup() {
        profileIcon.click();
        return new ProfilePopup();
    }

    public FriendsPopup openFriendsPopup() {
        yourFriendsIcon.click();
        return new FriendsPopup();
    }

    @Step("Check that friends count is equal to {expectedCount}")
    public Header checkFriendsCountInHeader(int expectedCount) {
        yourFriendsIcon.shouldHave(text(String.valueOf(expectedCount)));
        return this;
    }

}
