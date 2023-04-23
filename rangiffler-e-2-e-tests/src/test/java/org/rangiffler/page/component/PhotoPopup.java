package org.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

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

    private final SelenideElement editIcon = self.$("button [data-testid='EditIcon']");
    private final SelenideElement deleteIcon = self.$("button [data-testid='DeleteOutlineIcon']");
    private final SelenideElement closePopupIcon = self.$("button [data-testid='CloseIcon']");
    private final SelenideElement photoCountry = self.$("p.MuiTypography-h6");
    private final SelenideElement photoDescription = self.$("p.MuiTypography-body2");

    @Step("Set country: {0}")
    public PhotoPopup setCountry(String country) {
        countrySelect.click();
        countriesList.find(exactText(country)).click();
        return this;
    }

    @Step("Set description: {0}")
    public PhotoPopup setDescription(String description) {
        descriptionInput.clear();
        descriptionInput.setValue(description);
        return this;
    }

    @Step("Add photo img: {photoPath}")
    public PhotoPopup addPhoto(String photoPath) {
        addPhotoIcon.uploadFromClasspath(photoPath);
        return this;
    }

    @Step("Check added photo img: {photoPath}")
    public PhotoPopup checkPhotoAdded(String photoPath) {
        photoImg.shouldHave(photo(photoPath));
        return this;
    }

    @Step("Check the selected country: {0}")
    public PhotoPopup checkCountry(String country) {
        photoCountry.shouldHave(text(country));
        return this;
    }

    @Step("Check the description: {0}")
    public PhotoPopup checkDescription(String description) {
        photoDescription.shouldHave(text(description));
        return this;
    }

    @Step("Check the description: {0}")
    public PhotoPopup checkEmptyDescription() {
        photoDescription.shouldBe(empty);
        return this;
    }

    @Step("Change popup to the edit mode")
    public PhotoPopup changePopupToEditMode() {
        editIcon.click();
        return this;
    }

    @Step("Save photo")
    public void savePhoto() {
        saveButton.click();
    }

}
