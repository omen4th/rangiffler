package org.rangiffler.model;


import lombok.*;
import org.grpc.rangiffler.grpc.users.User;
import org.rangiffler.data.UserEntity;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Data
@Builder(toBuilder = true)
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class UserGrpcMessage {

    private UUID id;
    private String username;
    private String firstname;
    private String lastname;
    private byte[] avatar;
    private FriendStatus friendStatus;

    public static User toGrpcMessage(UserGrpcMessage userGrpcMessage) {
        User.Builder userBuilder = User.newBuilder()
                .setId(String.valueOf(userGrpcMessage.getId()))
                .setUsername(userGrpcMessage.getUsername());

        if (userGrpcMessage.getFirstname() != null) {
            userBuilder.setFirstname(userGrpcMessage.getFirstname());
        }
        if (userGrpcMessage.getLastname() != null) {
            userBuilder.setLastname(userGrpcMessage.getLastname());
        }
        if (userGrpcMessage.getAvatar() != null && userGrpcMessage.getAvatar().length > 0) {
            userBuilder.setAvatar(new String(userGrpcMessage.getAvatar(), StandardCharsets.UTF_8));
        }
        if (userGrpcMessage.friendStatus != null) {
            userBuilder.setFriendStatus(org.grpc.rangiffler.grpc.users.FriendStatus.valueOf(userGrpcMessage.friendStatus.name()));
        }

        return userBuilder.build();
    }

    public static User toGrpcMessage(UserEntity entity) {
        return toGrpcMessage(fromEntity(entity));
    }

    public static UserGrpcMessage fromEntity(UserEntity entity) {
        return UserGrpcMessage.builder()
                .id(entity.getId())
                .username(entity.getUsername())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .avatar(entity.getAvatar())
                .build();
    }

    public static UserGrpcMessage fromEntity(UserEntity entity, FriendStatus friendStatus) {
        return fromEntity(entity).toBuilder().friendStatus(friendStatus).build();
    }

    public static UserGrpcMessage fromGrpcMessage(User userMessage) {
        return UserGrpcMessage.builder()
                .username(userMessage.getUsername())
                .firstname(userMessage.getFirstname())
                .lastname(userMessage.getLastname())
                .avatar(userMessage.getAvatar().getBytes())
                .friendStatus(FriendStatus.valueOf(userMessage.getFriendStatus().name()))
                .build();
    }
}
