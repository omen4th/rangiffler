package org.rangiffler.service;

import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.grpc.rangiffler.grpc.users.*;
import org.rangiffler.model.UserJson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

import static org.rangiffler.model.UserJson.toGrpcMessage;

@Component
public class GrpcUsersClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcGeoClient.class);

    @GrpcClient("grpcUsersClient")
    private RangifflerUserServiceGrpc.RangifflerUserServiceBlockingStub rangifflerUserServiceStub;

    public @Nonnull List<UserJson> getAllUsers(String username) {
        try {
            return rangifflerUserServiceStub
                    .getAllUsers(UsernameRequest.newBuilder().setUsername(username).build())
                    .getUsersList()
                    .stream().map(UserJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull UserJson getCurrentUser(String username) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .getCurrentUser(UsernameRequest.newBuilder().setUsername(username).build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public @Nonnull UserJson updateCurrentUser(UserJson user) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .updateCurrentUser(UserRequest.newBuilder().setUser(toGrpcMessage(user)).build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getFriends(String username) {
        try {
            return rangifflerUserServiceStub
                    .getFriends(UsernameRequest.newBuilder().setUsername(username).build())
                    .getUsersList()
                    .stream().map(UserJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public List<UserJson> getInvitations(String username) {
        try {
            return rangifflerUserServiceStub
                    .getInvitations(UsernameRequest.newBuilder().setUsername(username).build())
                    .getUsersList()
                    .stream().map(UserJson::fromGrpcMessage)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson sendInvitation(String username, UserJson friend) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .sendInvitation(InvitationRequest.newBuilder()
                            .setUsername(username)
                            .setFriend(toGrpcMessage(friend))
                            .build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson removeUserFromFriends(String username, UserJson friend) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .removeUserFromFriends(InvitationRequest.newBuilder()
                            .setUsername(username)
                            .setFriend(toGrpcMessage(friend))
                            .build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }


    public UserJson acceptInvitation(String username, UserJson friend) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .acceptInvitation(InvitationRequest.newBuilder()
                            .setUsername(username)
                            .setFriend(toGrpcMessage(friend))
                            .build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

    public UserJson declineInvitation(String username, UserJson friend) {
        try {
            return UserJson.fromGrpcMessage(rangifflerUserServiceStub
                    .declineInvitation(InvitationRequest.newBuilder()
                            .setUsername(username)
                            .setFriend(toGrpcMessage(friend))
                            .build())
                    .getUser()
            );
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

}
