package org.rangiffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.value;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static org.rangiffler.condition.PhotoCondition.photo;

public class PhotoPopup extends BaseComponent<PhotoPopup> {
    public PhotoPopup() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final SelenideElement addPhotoIcon = self.$("#file");
    private final SelenideElement photoImg = self.$("img[src*='data']");
    private final SelenideElement countryInput = self.$("MuiSelect-nativeInput");
    private final SelenideElement descriptionInput = self.$("textarea#\\:rh\\:");
    private final SelenideElement saveButton = self.$(byText("Save"));

    @Step("Set country: {0}")
    public PhotoPopup setCountry(String country) {
        countryInput.setValue(country);
        return this;
    }

    @Step("Set country: {0}")
    public PhotoPopup checkCountry(String country) {
        countryInput.shouldHave(value(country));
        return this;
    }

    @Step("Set description: {0}")
    public PhotoPopup setDescription(String description) {
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Set description: {0}")
    public PhotoPopup checkDescription(String description) {
        descriptionInput.shouldHave(value(description));
        return this;
    }

    @Step("Add photo img: {photoPath}")
    public PhotoPopup addPhoto(String photoPath) {
        addPhotoIcon.uploadFromClasspath(photoPath);
        return this;
    }

    @Step("Add photo img: {photoPath}")
    public PhotoPopup checkPhotoAdded(String photoPath) {
        photoImg.shouldHave(photo(photoPath));
        return this;
    }

    @Step("Save photo")
    public void savePhoto() {
        saveButton.click();
    }
}
