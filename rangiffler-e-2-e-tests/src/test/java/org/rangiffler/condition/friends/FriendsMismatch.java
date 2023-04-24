package org.rangiffler.condition.friends;

import com.codeborne.selenide.ex.UIAssertionError;
import com.codeborne.selenide.impl.CollectionSource;
import org.rangiffler.model.UserGrpc;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.System.lineSeparator;

@ParametersAreNonnullByDefault
public class FriendsMismatch extends UIAssertionError {
    public FriendsMismatch(CollectionSource collection,
                           List<UserGrpc> expectedFriends, List<UserGrpc> actualFriends,
                           @Nullable String explanation, long timeoutMs) {
        super(
                collection.driver(),
                "Friends mismatch" +
                        lineSeparator() + "Actual: " + actualFriends.stream().map(UserGrpc::getUsername).toList() +
                        lineSeparator() + "Expected: " + expectedFriends.stream().map(UserGrpc::getUsername).toList() +
                        (explanation == null ? "" : lineSeparator() + "Because: " + explanation) +
                        lineSeparator() + "Collection: " + collection.description(),
                expectedFriends, actualFriends,
                timeoutMs);
    }
}
