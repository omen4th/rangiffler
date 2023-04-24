package org.rangiffler.page;

import io.qameta.allure.Step;
import org.rangiffler.page.component.TravelsPanel;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class FriendsTravelsPage extends BasePage<FriendsTravelsPage> {

    protected final TravelsPanel travelsPanel = new TravelsPanel($(".MuiTabPanel-root[id*='friends']"));

    public TravelsPanel getTravelsPanel() {
        return travelsPanel;
    }

    @Step("Check that the page is loaded")
    @Override
    public FriendsTravelsPage waitForPageLoaded() {
        travelsPanel.getSelf().shouldBe(visible);
        return this;
    }
}
