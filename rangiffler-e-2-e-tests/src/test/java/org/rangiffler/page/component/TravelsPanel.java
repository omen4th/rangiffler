package org.rangiffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static org.rangiffler.condition.PhotoCondition.photo;

public class TravelsPanel extends BaseComponent<TravelsPanel> {
    public TravelsPanel(SelenideElement self) {
        super(self);
    }

    private final ElementsCollection photosList = self.$$("ul.MuiImageList-root img");

    @Step("Open photo popup for img {photoPath}")
    public PhotoPopup openPhotoPopup(String photoPath) {
        photosList.find(photo(photoPath)).click();
        return new PhotoPopup();
    }


}
