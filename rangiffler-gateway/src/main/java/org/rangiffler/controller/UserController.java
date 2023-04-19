package org.rangiffler.controller;

import org.rangiffler.model.UserJson;
import org.rangiffler.service.GrpcUsersClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class UserController {

    private final GrpcUsersClient usersClient;

    @Autowired
    public UserController(GrpcUsersClient usersClient) {
        this.usersClient = usersClient;
    }

    @GetMapping("/users")
    public List<UserJson> getAllUsers(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return usersClient.getAllUsers(username);
    }

    @GetMapping("/currentUser")
    public UserJson getCurrentUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return usersClient.getCurrentUser(username);
    }

    @PatchMapping("/currentUser")
    public UserJson updateCurrentUser(@AuthenticationPrincipal Jwt principal,
                                      @Validated @RequestBody UserJson user) {
        String username = principal.getClaim("sub");
        user.setUsername(username);
        return usersClient.updateCurrentUser(user);
    }

    @GetMapping("/friends")
    public List<UserJson> getFriendsByUserId(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return usersClient.getFriends(username);
    }

    @GetMapping("invitations")
    public List<UserJson> getInvitations(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return usersClient.getInvitations(username);
    }

    @PostMapping("users/invite/")
    public UserJson sendInvitation(@AuthenticationPrincipal Jwt principal,
                                   @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return usersClient.sendInvitation(username, friend);
    }

    @PostMapping("friends/remove")
    public UserJson removeFriendFromUser(@AuthenticationPrincipal Jwt principal,
                                         @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return usersClient.removeUserFromFriends(username, friend);
    }

    @PostMapping("friends/submit")
    public UserJson submitFriend(@AuthenticationPrincipal Jwt principal,
                                 @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return usersClient.acceptInvitation(username, friend);
    }

    @PostMapping("friends/decline")
    public UserJson declineFriend(@AuthenticationPrincipal Jwt principal,
                                  @Validated @RequestBody UserJson friend) {
        String username = principal.getClaim("sub");
        return usersClient.declineInvitation(username, friend);
    }
}
