package org.rangiffler.model;


import lombok.*;
import org.grpc.rangiffler.grpc.users.User;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserGrpc {

    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private byte[] avatar;
    private FriendStatus friendStatus;
    private String password;

    @Builder.Default
    private List<PhotoGrpc> photosGrpcList = new ArrayList<>();
    @Builder.Default
    private List<UserGrpc> friendsGrpcList = new ArrayList<>();
    @Builder.Default
    private List<UserGrpc> invitationsGrpcList = new ArrayList<>();

    public static User toGrpcMessage(UserGrpc userGrpc) {
        User.Builder userBuilder = User.newBuilder()
                .setUsername(userGrpc.getUsername());

        if (userGrpc.getFirstname() != null) {
            userBuilder.setFirstname(userGrpc.getFirstname());
        }
        if (userGrpc.getLastname() != null) {
            userBuilder.setLastname(userGrpc.getLastname());
        }
        if (userGrpc.getAvatar() != null && userGrpc.getAvatar().length > 0) {
            userBuilder.setAvatar(new String(userGrpc.getAvatar(), StandardCharsets.UTF_8));
        }
        if (userGrpc.friendStatus != null) {
            userBuilder.setFriendStatus(org.grpc.rangiffler.grpc.users.FriendStatus.valueOf(userGrpc.friendStatus.name()));
        }

        return userBuilder.build();
    }

    public static UserGrpc fromGrpcMessage(User userMessage) {
        UserGrpc.UserGrpcBuilder builder = UserGrpc.builder()
                .username(userMessage.getUsername())
                .firstname(userMessage.getFirstname())
                .lastname(userMessage.getLastname())
                .avatar(userMessage.getAvatar().getBytes())
                .friendStatus(FriendStatus.valueOf(userMessage.getFriendStatus().name()));

        if (!userMessage.getId().isEmpty()) {
            builder.id(UUID.fromString(userMessage.getId()));
        }

        return builder.build();
    }
}
