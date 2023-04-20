package org.rangiffler.page;

import org.junit.jupiter.api.Assertions;

public class MainPage extends BasePage<MainPage> {

    public static final String URL = CFG.frontUrl();


    @Override
    public MainPage waitForPageLoaded() {
        return this;
    }
}
