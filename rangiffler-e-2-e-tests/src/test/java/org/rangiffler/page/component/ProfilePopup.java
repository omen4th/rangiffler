package org.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.rangiffler.condition.PhotoCondition.photo;

public class ProfilePopup extends BaseComponent<ProfilePopup> {

    public ProfilePopup() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final SelenideElement username = self.$(byText("Username"));
    private final SelenideElement firstnameInput = self.$("input[name='firstName']");
    private final SelenideElement lastnameInput = self.$("input[name='lastName']");
    private final SelenideElement saveButton = self.$(byText("Save"));
    private final SelenideElement addPhotoIcon = self.$("#file");
    private final SelenideElement avatarImg = self.$("img[src*='data']");

    @Step("Set firstname: {0}")
    public ProfilePopup setFirstname(String firstname) {
        firstnameInput.setValue(firstname);
        return this;
    }

    @Step("Set lastname: {0}")
    public ProfilePopup setLastname(String lastname) {
        lastnameInput.setValue(lastname);
        return this;
    }

    @Step("Save the profile")
    public void saveProfile() {
        saveButton.click();
    }

    @Step("Check firstname: {0}")
    public ProfilePopup checkFirstname(String firstname) {
        firstnameInput.shouldHave(value(firstname));
        return this;
    }

    @Step("Check lastname: {0}")
    public ProfilePopup checkLastname(String lastname) {
        lastnameInput.shouldHave(value(lastname));
        return this;
    }

    @Step("Set avatar img: {avatarPath}")
    public ProfilePopup updateAvatar(String avatarPath) {
        addPhotoIcon.uploadFromClasspath(avatarPath);
        return this;
    }

    @Step("Update avatar with img: {avatarPath}")
    public void checkAvatar(String avatarPath) {
        avatarImg.shouldHave(photo(avatarPath));
    }
}
