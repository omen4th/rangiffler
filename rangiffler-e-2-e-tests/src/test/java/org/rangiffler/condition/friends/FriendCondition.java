package org.rangiffler.condition.friends;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.ex.ElementNotFound;
import com.codeborne.selenide.impl.CollectionSource;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.rangiffler.model.UserGrpc;

import javax.annotation.Nullable;
import java.util.List;
import java.util.stream.Collectors;

public class FriendCondition {

    public static CollectionCondition friends(List<UserGrpc> expectedFriends) {
        return new CollectionCondition() {
            @Override
            public void fail(CollectionSource collection, @Nullable List<WebElement> elements, @Nullable Exception lastError, long timeoutMs) {
                if (elements == null || elements.isEmpty()) {
                    ElementNotFound elementNotFound = new ElementNotFound(collection, List.of("Can`t find elements"), lastError);
                    throw elementNotFound;
                } else if (elements.size() != expectedFriends.size()) {
                    throw new FriendsSizeMismatch(collection, expectedFriends, bindElementsToFriend(elements), explanation, timeoutMs);
                } else {
                    throw new FriendsMismatch(collection, expectedFriends, bindElementsToFriend(elements), explanation, timeoutMs);
                }
            }

            @Override
            public boolean missingElementSatisfiesCondition() {
                return false;
            }

            @Override
            public boolean test(List<WebElement> elements) {
                if (elements.size() != expectedFriends.size()) {
                    return false;
                }
                for (int i = 0; i < expectedFriends.size(); i++) {
                    WebElement row = elements.get(i);
                    UserGrpc expectedFriend = expectedFriends.get(i);
                    List<WebElement> cells = row.findElements(By.cssSelector("td"));

                    if (!cells.get(1).getText().equals(expectedFriend.getUsername())) {
                        return false;
                    }
                }
                return true;
            }

            private List<UserGrpc> bindElementsToFriend(List<WebElement> elements) {
                return elements.stream()
                        .map(e -> {
                            List<WebElement> cells = e.findElements(By.cssSelector("td"));
                            UserGrpc actual = new UserGrpc();
                            actual.setUsername(cells.get(1).getText());
                            return actual;
                        })
                        .collect(Collectors.toList());
            }
        };
    }
}
