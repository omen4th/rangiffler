package org.rangiffler.service;

import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.grpc.rangiffler.grpc.users.*;
import org.grpc.rangiffler.grpc.username.UsernameRequest;
import org.rangiffler.data.FriendsEntity;
import org.rangiffler.data.UserEntity;
import org.rangiffler.data.repository.UserRepository;
import org.rangiffler.model.FriendStatus;
import org.rangiffler.model.UserGrpcMessage;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static org.rangiffler.model.UserGrpcMessage.*;

@GrpcService
public class GrpcUsersService extends RangifflerUserServiceGrpc.RangifflerUserServiceImplBase {
    private final UserRepository userRepository;

    @Autowired
    public GrpcUsersService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void getAllUsers(UsernameRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        String username = request.getUsername();

        Map<UUID, UserGrpcMessage> result = new HashMap<>();
        for (UserEntity user : userRepository.findByUsernameNot(username)) {
            List<FriendsEntity> sendInvites = user.getFriends();
            List<FriendsEntity> receivedInvites = user.getInvites();

            if (!sendInvites.isEmpty() || !receivedInvites.isEmpty()) {
                Optional<FriendsEntity> inviteToMe = sendInvites.stream().filter(i -> i.getFriend().getUsername().equals(username)).findFirst();

                Optional<FriendsEntity> inviteFromMe = receivedInvites.stream().filter(i -> i.getUser().getUsername().equals(username)).findFirst();

                if (inviteToMe.isPresent()) {
                    FriendsEntity invite = inviteToMe.get();
                    result.put(user.getId(), fromEntity(user, invite.isPending() ? FriendStatus.INVITATION_RECEIVED : FriendStatus.FRIEND));
                }
                if (inviteFromMe.isPresent()) {
                    FriendsEntity invite = inviteFromMe.get();
                    result.put(user.getId(), fromEntity(user, invite.isPending() ? FriendStatus.INVITATION_SENT : FriendStatus.FRIEND));
                }
            }
            if (!result.containsKey(user.getId())) {
                result.put(user.getId(), fromEntity(user));
            }
        }
        List<UserGrpcMessage> all = new ArrayList<>(result.values());

        AllUsersResponse response = AllUsersResponse.newBuilder().addAllUsers(all.stream().map(UserGrpcMessage::toGrpcMessage).toList()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getCurrentUser(UsernameRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserEntity userDataEntity = userRepository.findByUsername(username);
        UserResponse response;

        if (userDataEntity == null) {
            userDataEntity = new UserEntity();
            userDataEntity.setUsername(username);
            response = UserResponse.newBuilder().setUser(toGrpcMessage(userRepository.save(userDataEntity))).build();
        } else {
            response = UserResponse.newBuilder().setUser(toGrpcMessage(userDataEntity)).build();
        }

        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void updateCurrentUser(UserRequest request, StreamObserver<UserResponse> responseObserver) {
        UserGrpcMessage user = fromGrpcMessage(request.getUser());
        UserEntity userDataEntity = userRepository.findByUsername(user.getUsername());

        userDataEntity.setFirstname(user.getFirstname());
        userDataEntity.setLastname(user.getLastname());
        userDataEntity.setAvatar(user.getAvatar());
        UserEntity saved = userRepository.save(userDataEntity);

        UserGrpcMessage.fromEntity(saved);
        UserResponse response = UserResponse.newBuilder().setUser(toGrpcMessage(userDataEntity)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getFriends(UsernameRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        String username = request.getUsername();

        List<UserGrpcMessage> friends = userRepository.findByUsername(username).getFriends().stream().filter(fe -> !fe.isPending()).map(fe -> fromEntity(fe.getFriend(), FriendStatus.FRIEND)).toList();

        AllUsersResponse response = AllUsersResponse.newBuilder().addAllUsers(friends.stream().map(UserGrpcMessage::toGrpcMessage).toList()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void getInvitations(UsernameRequest request, StreamObserver<AllUsersResponse> responseObserver) {
        String username = request.getUsername();

        List<UserGrpcMessage> invitations = userRepository.findByUsername(username).getInvites().stream().filter(FriendsEntity::isPending).map(fe -> UserGrpcMessage.fromEntity(fe.getUser(), FriendStatus.INVITATION_RECEIVED)).toList();

        AllUsersResponse response = AllUsersResponse.newBuilder().addAllUsers(invitations.stream().map(UserGrpcMessage::toGrpcMessage).toList()).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void sendInvitation(InvitationRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserGrpcMessage friend = fromGrpcMessage(request.getFriend());
        UserEntity currentUserEntity = userRepository.findByUsername(username);
        UserEntity friendEntity = userRepository.findByUsername(friend.getUsername());

        currentUserEntity.addFriends(true, friendEntity);
        userRepository.save(currentUserEntity);
        friend = UserGrpcMessage.fromEntity(friendEntity, FriendStatus.INVITATION_SENT);

        UserResponse response = UserResponse.newBuilder().setUser(toGrpcMessage(friend)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void removeUserFromFriends(InvitationRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserGrpcMessage friendToRemove = fromGrpcMessage(request.getFriend());
        UserEntity currentUser = userRepository.findByUsername(username);
        UserEntity friendToRemoveEntity = userRepository.findByUsername(friendToRemove.getUsername());

        currentUser.removeFriends(friendToRemoveEntity);
        currentUser.removeInvites(friendToRemoveEntity);
        userRepository.save(currentUser);
        friendToRemoveEntity.removeFriends(currentUser);
        friendToRemoveEntity.removeInvites(currentUser);
        userRepository.save(friendToRemoveEntity);
        friendToRemove = UserGrpcMessage.fromEntity(friendToRemoveEntity, FriendStatus.NOT_FRIEND);

        UserResponse response = UserResponse.newBuilder().setUser(toGrpcMessage(friendToRemove)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void acceptInvitation(InvitationRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserGrpcMessage inviteUser = fromGrpcMessage(request.getFriend());
        UserEntity currentUserEntity = userRepository.findByUsername(username);
        UserEntity inviteUserEntity = userRepository.findByUsername(inviteUser.getUsername());

        FriendsEntity invite = currentUserEntity.getInvites().stream().filter(fe -> fe.getUser().getUsername().equals(inviteUserEntity.getUsername())).findFirst().orElseThrow();

        invite.setPending(false);
        currentUserEntity.addFriends(false, inviteUserEntity);
        userRepository.save(currentUserEntity);
        inviteUser = UserGrpcMessage.fromEntity(inviteUserEntity, FriendStatus.FRIEND);

        UserResponse response = UserResponse.newBuilder().setUser(toGrpcMessage(inviteUser)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }

    @Override
    public void declineInvitation(InvitationRequest request, StreamObserver<UserResponse> responseObserver) {
        String username = request.getUsername();
        UserGrpcMessage friendToDecline = fromGrpcMessage(request.getFriend());
        UserEntity currentUserEntity = userRepository.findByUsername(username);
        UserEntity friendToDeclineEntity = userRepository.findByUsername(friendToDecline.getUsername());

        friendToDeclineEntity.removeFriends(currentUserEntity);
        currentUserEntity.removeFriends(friendToDeclineEntity);
        userRepository.save(currentUserEntity);
        userRepository.save(friendToDeclineEntity);
        friendToDecline = UserGrpcMessage.fromEntity(friendToDeclineEntity, FriendStatus.NOT_FRIEND);

        UserResponse response = UserResponse.newBuilder().setUser(toGrpcMessage(friendToDecline)).build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
}
