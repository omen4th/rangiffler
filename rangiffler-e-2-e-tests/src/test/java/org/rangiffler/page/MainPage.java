package org.rangiffler.page;

import org.rangiffler.page.component.Header;
import org.rangiffler.page.component.NavigationPanel;
import org.rangiffler.page.component.ProfilePopup;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();

    protected final NavigationPanel navigationPanel = new NavigationPanel();
    protected final Header header = new Header();
    protected final ProfilePopup profilePopup = new ProfilePopup();

    public NavigationPanel getNavigationPanel() {
        return navigationPanel;
    }

    public Header getHeader() {
        return header;
    }

    //TODO
    @Override
    public MainPage waitForPageLoaded() {

        return this;
    }
}
