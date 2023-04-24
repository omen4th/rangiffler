package org.rangiffler.condition.friends;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import org.rangiffler.model.UserGrpc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

import java.util.List;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class FriendsSizeMismatch extends UIAssertionError {
    public FriendsSizeMismatch(CollectionSource collection,
                               List<UserGrpc> expectedFriends, List<UserGrpc> actualFriends,
                               @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Friends size mismatch" +
                        lineSeparator() + "Actual: " + actualFriends.stream().map(UserGrpc::getUsername).toList() + ", List size: " + actualFriends.size() +
                        lineSeparator() + "Expected: " + expectedFriends.stream().map(UserGrpc::getUsername).toList() + ", List size: " + expectedFriends.size() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedFriends, actualFriends,
                timeoutMs
        );
    }
}
