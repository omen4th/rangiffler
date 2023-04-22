package org.rangiffler.page;

import io.qameta.allure.Step;

public class FriendsTravelsPage extends BasePage<FriendsTravelsPage> {


    //TODO
    @Step("Check that the page is loaded")
    @Override
    public FriendsTravelsPage waitForPageLoaded() {
        return this;
    }
}
