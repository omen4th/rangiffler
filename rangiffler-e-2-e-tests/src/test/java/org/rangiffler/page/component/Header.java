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
    private final SelenideElement photosIcon = self.$("div[aria-label='Your photos']");
    private final SelenideElement friendsIcon = self.$("div[aria-label='Your friends']");
    private final SelenideElement logoutIcon = self.$("div[aria-label='Logout']");

    public ProfilePopup openProfilePopup() {
        profileIcon.click();
        return new ProfilePopup();
    }

    public FriendsPopup openFriendsPopup() {
        friendsIcon.click();
        return new FriendsPopup();
    }

    public PhotoPopup openPopupToAddPhoto() {
        addPhotoButton.click();
        return new PhotoPopup();
    }

    @Step("Check that friends count is equal to {expectedCount}")
    public Header checkFriendsCountInHeader(int expectedCount) {
        friendsIcon.shouldHave(text(String.valueOf(expectedCount)));
        return this;
    }

    @Step("Check that photos count is equal to {expectedCount}")
    public Header checkPhotosCountInHeader(int expectedCount) {
        photosIcon.shouldHave(text(String.valueOf(expectedCount)));
        return this;
    }

    @Step("Check that visited countries count is equal to {expectedCount}")
    public Header checkCountriesCountInHeader(int expectedCount) {
        visitedCountriesIcon.shouldHave(text(String.valueOf(expectedCount)));
        return this;
    }

}
