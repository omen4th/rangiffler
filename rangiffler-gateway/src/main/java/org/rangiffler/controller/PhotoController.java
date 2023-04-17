package org.rangiffler.controller;

import org.rangiffler.model.PhotoJson;
import org.rangiffler.service.GrpcPhotoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
public class PhotoController {

    private final GrpcPhotoClient photoClient;

    @Autowired
    public PhotoController(GrpcPhotoClient photoClient) {
        this.photoClient = photoClient;
    }

    @GetMapping("/photos")
    public List<PhotoJson> getPhotosForUser(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return photoClient.getAllUserPhotos(username);
    }

    @GetMapping("/friends/photos")
    public List<PhotoJson> getAllFriendsPhotos(@AuthenticationPrincipal Jwt principal) {
        String username = principal.getClaim("sub");
        return photoClient.getAllFriendsPhotos(username);
    }

    @PostMapping("/photos")
    public PhotoJson addPhoto(@AuthenticationPrincipal Jwt principal,
                              @Validated @RequestBody PhotoJson photoJson) {
        String username = principal.getClaim("sub");
        photoJson.setUsername(username);
        return photoClient.addPhoto(photoJson);
    }

    @PatchMapping("/photos/{id}")
    public PhotoJson editPhoto(@AuthenticationPrincipal Jwt principal,
                               @Validated @RequestBody PhotoJson photoJson) {
        String username = principal.getClaim("sub");
        photoJson.setUsername(username);
        return photoClient.editPhoto(photoJson);
    }

    @DeleteMapping("/photos")
    public void deletePhoto(@AuthenticationPrincipal Jwt principal,
                            @Validated @RequestParam UUID photoId) {
        photoClient.deletePhoto(photoId);
    }

}
