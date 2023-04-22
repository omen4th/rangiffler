package org.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public class Header extends BaseComponent<Header> {

    public Header() {
        super($("header"));
    }

    private final SelenideElement addPhotoButton = self.$("button");
    private final SelenideElement profileIcon = self.$(".MuiAvatar-root");
    private final SelenideElement visitedCountriesIcon = self.$("div[aria-label='Your visited countries']");
    private final SelenideElement yourPhotosIcon = self.$("div[aria-label='Your photos']");
    private final SelenideElement yourFriendsIcon = self.$("div[aria-label='Your friends']");
    private final SelenideElement logoutIcon = self.$("div[aria-label='Logout']");

    public ProfilePopup toProfilePage() {
        profileIcon.click();
        return new ProfilePopup();
    }
}
