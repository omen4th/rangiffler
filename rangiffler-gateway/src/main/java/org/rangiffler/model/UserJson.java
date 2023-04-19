package org.rangiffler.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;
import org.grpc.rangiffler.grpc.users.User;

import java.util.UUID;

@Data
@Builder
public class UserJson {

    @JsonProperty("id")
    private UUID id;

    @JsonProperty("username")
    private String username;

    @JsonProperty("firstName")
    private String firstname;

    @JsonProperty("lastName")
    private String lastname;

    @JsonProperty("avatar")
    private String avatar;

    @JsonProperty("friendStatus")
    private FriendStatus friendStatus;

    public static UserJson fromGrpcMessage(User userMessage) {
        return UserJson.builder()
                .id(UUID.fromString((userMessage.getId())))
                .username(userMessage.getUsername())
                .firstname(userMessage.getFirstname())
                .lastname(userMessage.getLastname())
                .avatar(userMessage.getAvatar())
                .friendStatus(FriendStatus.valueOf(userMessage.getFriendStatus().name()))
                .build();
    }

    public static User toGrpcMessage(UserJson userJson) {
        return User.newBuilder()
                .setId(userJson.getId().toString())
                .setUsername(userJson.getUsername())
                .setFirstname(userJson.getFirstname())
                .setLastname(userJson.getLastname())
                .setAvatar(userJson.getAvatar())
                .setFriendStatus(org.grpc.rangiffler.grpc.users.FriendStatus.valueOf(userJson.getFriendStatus().name()))
                .build();
    }
}

