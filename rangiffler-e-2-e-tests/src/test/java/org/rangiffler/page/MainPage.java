package org.rangiffler.page;

import io.qameta.allure.Step;
import org.rangiffler.page.component.Header;
import org.rangiffler.page.component.NavigationPanel;
import org.rangiffler.page.component.TravelsPanel;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final NavigationPanel navigationPanel = new NavigationPanel();
    protected final Header header = new Header();
    protected final TravelsPanel travelsPanel = new TravelsPanel($(".MuiTabPanel-root[id*='main']"));

    public NavigationPanel getNavigationPanel() {
        return navigationPanel;
    }

    public Header getHeader() {
        return header;
    }

    public TravelsPanel getTravelsPanel() {
        return travelsPanel;
    }

    @Step("Check that the page is loaded")
    @Override
    public MainPage waitForPageLoaded() {
        header.getSelf().shouldBe(visible).shouldHave(text("Rangiffler"));
        navigationPanel.getSelf().shouldBe(visible);
        travelsPanel.getSelf().shouldBe(visible);
        return this;
    }
}
