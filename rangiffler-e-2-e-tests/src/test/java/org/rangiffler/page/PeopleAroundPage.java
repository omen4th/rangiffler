package org.rangiffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;

public class PeopleAroundPage extends BasePage<PeopleAroundPage> {

    private final SelenideElement usersTable = $("tbody");
    private final ElementsCollection userTableRows = usersTable.$$("tr");
    private final String acceptInvitationIcon = "button[aria-label='Accept invitation']";
    private final String declineInvitationIcon = "button[aria-label='Decline invitation']";
    private final String removeFriendIcon = "button[aria-label='Remove friend']";
    private final String addFriendIcon = "button[aria-label='Add friend']";
    private final SelenideElement declineInvitationButton = $(byText("Decline"));
    private final SelenideElement deleteFriendButton = $(byText("Delete"));
    private final SelenideElement messageAlert = $("div[role='alert']");

    @Step("Accept invitation from user {username}")
    public PeopleAroundPage acceptInvitationFromUser(String username) {
        userTableRows.find(text(username)).scrollTo().$(acceptInvitationIcon).click();
        return this;
    }

    @Step("Check that icon is changed after the invitation is accepted")
    public PeopleAroundPage checkIconChangedToRemoveFriend(String username) {
        userTableRows.find(text(username)).scrollTo().$(removeFriendIcon).shouldBe(visible);
        return this;
    }

    @Step("Decline invitation from user {username}")
    public PeopleAroundPage declineInvitationFromUser(String username) {
        userTableRows.find(text(username)).scrollTo().$(declineInvitationIcon).click();
        declineInvitationButton.click();
        return this;
    }

    @Step("Decline invitation from user {username}")
    public PeopleAroundPage removeUserFromFriends(String username) {
        userTableRows.find(text(username)).scrollTo().$(removeFriendIcon).click();
        deleteFriendButton.click();
        return this;
    }

    @Step("Send invitation to user {username}")
    public PeopleAroundPage sendInvitationUser(String username) {
        userTableRows.find(text(username)).$(addFriendIcon).click();
        return this;
    }

    @Step("Check that icon is changed after the invitation is accepted")
    public PeopleAroundPage checkIconChangedToAddFriend(String username) {
        userTableRows.find(text(username)).scrollTo().$(addFriendIcon).shouldBe(visible);
        return this;
    }

    @Step("Check that icon is changed after the invitation is send")
    public PeopleAroundPage checkIconChangedToInvitationSentTest(String username) {
        userTableRows.find(text(username))
                .scrollTo()
                .$$("td")
                .get(3)
                .shouldHave(text("Invitation sent"));
        return this;
    }

    @Step("Check that susses message is displayed: {message}")
    public PeopleAroundPage checkSussesMessage(String sussesMessage) {
        messageAlert.shouldHave(text(sussesMessage));
        return this;
    }

    @Step("Check that the page is loaded")
    @Override
    public PeopleAroundPage waitForPageLoaded() {
        usersTable.shouldBe(visible);
        return this;
    }
}
