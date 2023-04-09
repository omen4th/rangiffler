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

    public User toGrpcMessage() {
        return User.newBuilder()
                .setId(id.toString())
                .setUsername(username)
                .setFirstname(firstname)
                .setLastname(lastname)
                .setAvatar(avatar)
                .setFriendStatus(org.grpc.rangiffler.grpc.users.FriendStatus.valueOf(friendStatus.name()))
                .build();
    }
}

