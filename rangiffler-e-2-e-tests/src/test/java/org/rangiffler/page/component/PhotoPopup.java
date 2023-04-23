package org.rangiffler.page.component;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import org.rangiffler.model.Country;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.rangiffler.condition.PhotoCondition.photo;

public class PhotoPopup extends BaseComponent<PhotoPopup> {
    public PhotoPopup() {
        super($(".MuiPaper-root.MuiCard-root"));
    }

    private final SelenideElement addPhotoIcon = self.$("#file");
    private final SelenideElement photoImg = self.$("img[src*='data']");
    private final SelenideElement countrySelect = self.$("div[class*='MuiSelect-root']");
    private final ElementsCollection countriesList = $$("ul.MuiMenu-list li");
    private final SelenideElement descriptionInput = self.$("textarea.MuiInputBase-input[aria-invalid='false']");
    private final SelenideElement saveButton = self.$(byText("Save"));

    @Step("Set country: {0}")
    public PhotoPopup setCountry(Country country) {
        countrySelect.click();
        countriesList.shouldHave(size(Country.values().length))
                .filter(text(country.name()))
                .first()
                .scrollIntoView(true)
                .shouldBe(visible)
                .hover()
                .click();
        return this;
    }

    @Step("Set country: {0}")
    public PhotoPopup checkCountry(String country) {
        countrySelect.shouldHave(value(country));
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
