package org.rangiffler.service;

import io.grpc.StatusRuntimeException;
import jakarta.annotation.Nonnull;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.grpc.rangiffler.grpc.users.RangifflerUserServiceGrpc;
import org.grpc.rangiffler.grpc.users.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Component
public class GrpcUsersClient {

    private static final Logger LOG = LoggerFactory.getLogger(GrpcUsersClient.class);

    @GrpcClient("grpcUsersClient")
    private RangifflerUserServiceGrpc.RangifflerUserServiceBlockingStub rangifflerUserServiceStub;

    public @Nonnull List<String> getFriends(String username) {
        try {
            return rangifflerUserServiceStub
                    .getFriends(UsernameRequest.newBuilder().setUsername(username).build())
                    .getUsersList()
                    .stream()
                    .map(User::getUsername)
                    .toList();
        } catch (StatusRuntimeException e) {
            LOG.error("### Error while calling gRPC server ", e);
            throw new ResponseStatusException(HttpStatus.SERVICE_UNAVAILABLE, "The gRPC operation was cancelled", e);
        }
    }

}
