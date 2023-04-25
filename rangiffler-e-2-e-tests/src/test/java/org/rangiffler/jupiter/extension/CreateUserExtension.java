package org.rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.rangiffler.api.RangifflerPhotoGrpcClient;
import org.rangiffler.api.RangifflerUsersGrpcClient;
import org.rangiffler.api.auth.RangifflerAuthClient;
import org.rangiffler.condition.PhotoCondition;
import org.rangiffler.config.Config;
import org.rangiffler.jupiter.annotation.*;
import org.rangiffler.model.*;
import retrofit2.Response;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

import static org.rangiffler.utils.DataUtils.*;
import static org.rangiffler.utils.PhotoUtils.getPhotoByteFromClasspath;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final RangifflerAuthClient authClient = new RangifflerAuthClient();
    private final RangifflerUsersGrpcClient userdataClient = new RangifflerUsersGrpcClient();
    private final RangifflerPhotoGrpcClient photoClient = new RangifflerPhotoGrpcClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, USE.LOGIN),
            ON_METHOD_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class, USE.METHOD);


    @Step("Create user for test")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);
        Map<USE, List<GenerateUser>> userAnnotations = extractGenerateUserAnnotations(context);
        for (Map.Entry<USE, List<GenerateUser>> entry : userAnnotations.entrySet()) {
            UserGrpc[] resultCollector = new UserGrpc[entry.getValue().size()];
            List<GenerateUser> value = entry.getValue();
            for (int i = 0; i < value.size(); i++) {
                GenerateUser generateUser = value.get(i);
                String username = generateUser.username();
                String password = generateUser.password();
                if ("".equals(username)) {
                    username = generateRandomUsername();
                }
                if ("".equals(password)) {
                    password = generateRandomPassword();
                }
                UserGrpc createdUser = apiRegister(username, password);

                updateUserInfoIfPresent(generateUser, createdUser);
                createFriendsIfPresent(generateUser, createdUser);
                createIncomeInvitationsIfPresent(generateUser, createdUser);
                createOutcomeInvitationsIfPresent(generateUser, createdUser);
                addPhotosIfPresent(generateUser, createdUser);
                addFriendsPhotosIfPresent(generateUser, createdUser);
                resultCollector[i] = createdUser;
            }

            Object storedResult = resultCollector.length == 1 ? resultCollector[0] : resultCollector;
            context.getStore(entry.getKey().getNamespace()).put(testId, storedResult);
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        Class<?> parameterType = parameterContext.getParameter().getType();
        return (parameterType.isAssignableFrom(UserGrpc.class) || parameterType.isAssignableFrom(UserGrpc[].class))
                && parameterContext.getParameter().isAnnotationPresent(User.class);
    }

    @Override
    public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        final String testId = getTestId(extensionContext);
        User annotation = parameterContext.getParameter().getAnnotation(User.class);
        return extensionContext.getStore(annotation.use().getNamespace()).get(testId);
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    public enum USE {
        METHOD, LOGIN;

        public ExtensionContext.Namespace getNamespace() {
            switch (this) {
                case METHOD -> {
                    return ON_METHOD_USERS_NAMESPACE;
                }
                case LOGIN -> {
                    return API_LOGIN_USERS_NAMESPACE;
                }
                default -> {
                    throw new IllegalStateException();
                }
            }
        }
    }

    private Map<USE, List<GenerateUser>> extractGenerateUserAnnotations(ExtensionContext context) {
        Map<USE, List<GenerateUser>> annotationsOnTest = new HashMap<>();
        GenerateUser singleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        GenerateUsers multipleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUsers.class);
        if (singleUserAnnotation != null && singleUserAnnotation.handleAnnotation()) {
            annotationsOnTest.put(USE.METHOD, List.of(singleUserAnnotation));
        } else if (multipleUserAnnotation != null) {
            annotationsOnTest.put(USE.METHOD, Arrays.asList(multipleUserAnnotation.value()));
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.rangifflerUser().handleAnnotation()) {
            annotationsOnTest.put(USE.LOGIN, List.of(apiLoginAnnotation.rangifflerUser()));
        }
        return annotationsOnTest;
    }

    private UserGrpc apiRegister(String username, String password) throws Exception {
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 200) {
            throw new RuntimeException("User is not registered");
        }
        UserGrpc currentUser = userdataClient.getCurrentUser(username);
        currentUser.setPassword(password);
        return currentUser;
    }

    private void updateUserInfoIfPresent(GenerateUser generateUser, UserGrpc createdUser) {
        String firstname = generateUser.firstname();
        String lastname = generateUser.lastname();
        String avatarPath = generateUser.avatarPath();

        createdUser.setFirstname(firstname);
        createdUser.setLastname(lastname);
        if (!avatarPath.isEmpty()) {
            createdUser.setAvatar(getPhotoByteFromClasspath(avatarPath));
        }

        if ((!firstname.isEmpty() || (!lastname.isEmpty())) || (!avatarPath.isEmpty())) {
            userdataClient.updateCurrentUser(createdUser);
        }
    }

    private void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        IncomeInvitations invitations = generateUser.incomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserGrpc invitation = apiRegister(generateRandomUsername(), generateRandomPassword());
                UserGrpc addFriend = new UserGrpc();
                addFriend.setUsername(createdUser.getUsername());
                userdataClient.sendInvitation(invitation.getUsername(), addFriend);
                invitation.setFriendStatus(FriendStatus.INVITATION_SENT);
                createdUser.getInvitationsGrpcList().add(invitation);
            }
        }
    }

    private void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        OutcomeInvitations invitations = generateUser.outcomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserGrpc friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                userdataClient.sendInvitation(createdUser.getUsername(), friend);
                friend.setFriendStatus(FriendStatus.INVITATION_RECEIVED);
                createdUser.getInvitationsGrpcList().add(friend);
            }
        }
    }

    private void createFriendsIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        Friends friends = generateUser.friends();
        if (friends.handleAnnotation() && friends.count() > 0) {
            for (int i = 0; i < friends.count(); i++) {
                UserGrpc friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                UserGrpc invitation = new UserGrpc();
                invitation.setUsername(createdUser.getUsername());
                userdataClient.sendInvitation(createdUser.getUsername(), friend);
                userdataClient.acceptInvitation(friend.getUsername(), invitation);
                friend.setFriendStatus(FriendStatus.FRIEND);
                createdUser.getFriendsGrpcList().add(friend);
            }
        }
    }

    private void addPhotosIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        GeneratePhoto[] photos = generateUser.photos();
        if (photos != null) {
            for (GeneratePhoto photo : photos) {
                PhotoGrpc photoGrpc = new PhotoGrpc();
                photoGrpc.setPhoto(getPhotoByteFromClasspath(photo.photoPath()));
                photoGrpc.setDescription(photo.description());
                photoGrpc.setCountry(CountryGrpc.builder().name(photo.country().toString()).code(photo.country().getCode()).build());
                photoGrpc.setUsername(createdUser.getUsername());
                photoGrpc.setPhotoPath(photo.photoPath());
                PhotoGrpc created = photoClient.addUserPhoto(photoGrpc);
                photoGrpc.setId(created.getId());

                createdUser.getPhotosGrpcList().add(photoGrpc);
            }
        }
    }

    private void addFriendsPhotosIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        Friends friends = generateUser.friends();
        if (friends.handleAnnotation() && friends.count() > 0 && friends.withPhotos()) {
            for (int i = 0; i < friends.count(); i++) {
                UserGrpc friend = createdUser.getFriendsGrpcList().get(i);

                for (int j = 0; j < 2; j++) {
                    String photoPath = getRandomPhotoPath();
                    Country country = getRandomCountry();
                    PhotoGrpc photoGrpc = new PhotoGrpc();
                    photoGrpc.setPhoto(getPhotoByteFromClasspath(photoPath));
                    photoGrpc.setDescription(generateRandomDescription());
                    photoGrpc.setCountry(CountryGrpc.builder().code(country.getCode()).name(country.toString()).build());
                    photoGrpc.setUsername(friend.getUsername());
                    photoGrpc.setPhotoPath(photoPath);
                    PhotoGrpc created = photoClient.addUserPhoto(photoGrpc);
                    photoGrpc.setId(created.getId());

                    friend.getPhotosGrpcList().add(photoGrpc);
                }
            }
        }
    }

}
