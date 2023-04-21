package org.rangiffler.jupiter.extension;

import io.qameta.allure.AllureId;
import io.qameta.allure.Step;
import org.junit.jupiter.api.extension.*;
import org.rangiffler.api.RangifflerAuthClient;
import org.rangiffler.api.RangifflerUsersGrpcClient;
import org.rangiffler.config.Config;
import org.rangiffler.jupiter.annotation.*;
import org.rangiffler.model.UserGrpc;
import retrofit2.Response;

import java.util.*;

import static org.rangiffler.utils.DataUtils.generateRandomPassword;
import static org.rangiffler.utils.DataUtils.generateRandomUsername;

public class CreateUserExtension implements BeforeEachCallback, ParameterResolver {

    private final RangifflerAuthClient authClient = new RangifflerAuthClient();
    private final RangifflerUsersGrpcClient userdataClient = new RangifflerUsersGrpcClient();
    protected static final Config CFG = Config.getConfig();

    public static final ExtensionContext.Namespace
            API_LOGIN_USERS_NAMESPACE = ExtensionContext.Namespace.create(CreateUserExtension.class);


    @Step("Create user for test")
    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        final String testId = getTestId(context);

        List<GenerateUser> userAnnotations = extractGenerateUserAnnotations(context);
        UserGrpc[] resultCollector = new UserGrpc[userAnnotations.size()];

        for (int i = 0; i < userAnnotations.size(); i++) {
            GenerateUser generateUser = userAnnotations.get(i);
            String username = generateUser.username();
            String password = generateUser.password();
            if ("".equals(username)) {
                username = generateRandomUsername();
            }
            if ("".equals(password)) {
                password = generateRandomPassword();
            }
            UserGrpc createdUser = apiRegister(username, password);

            createFriendsIfPresent(generateUser, createdUser);
            createIncomeInvitationsIfPresent(generateUser, createdUser);
            createOutcomeInvitationsIfPresent(generateUser, createdUser);
            resultCollector[i] = createdUser;
        }

        Object storedResult = resultCollector.length == 1 ? resultCollector[0] : resultCollector;
        context.getStore(API_LOGIN_USERS_NAMESPACE).put(testId, storedResult);
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
        return extensionContext.getStore(API_LOGIN_USERS_NAMESPACE).get(testId);
    }

    private String getTestId(ExtensionContext context) {
        return Objects.requireNonNull(
                context.getRequiredTestMethod().getAnnotation(AllureId.class)
        ).value();
    }

    private List<GenerateUser> extractGenerateUserAnnotations(ExtensionContext context) {
        List<GenerateUser> annotationsOnTest = new ArrayList<>();
        GenerateUser singleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUser.class);
        GenerateUsers multipleUserAnnotation = context.getRequiredTestMethod().getAnnotation(GenerateUsers.class);
        if (singleUserAnnotation != null && singleUserAnnotation.handleAnnotation()) {
            annotationsOnTest.add(singleUserAnnotation);
        } else if (multipleUserAnnotation != null) {
            annotationsOnTest.addAll(Arrays.asList(multipleUserAnnotation.value()));
        }
        ApiLogin apiLoginAnnotation = context.getRequiredTestMethod().getAnnotation(ApiLogin.class);
        if (apiLoginAnnotation != null && apiLoginAnnotation.rangifflerUser().handleAnnotation()) {
            annotationsOnTest.add(apiLoginAnnotation.rangifflerUser());
        }
        return annotationsOnTest;
    }

    private UserGrpc apiRegister(String username, String password) throws Exception {
        authClient.authorize();
        Response<Void> res = authClient.register(username, password);
        if (res.code() != 201) {
            throw new RuntimeException("User is not registered");
        }
        UserGrpc currentUser = userdataClient.getCurrentUser(username);
        currentUser.setPassword(password);
        return currentUser;
    }

    private void createIncomeInvitationsIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        IncomeInvitations invitations = generateUser.incomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserGrpc invitation = apiRegister(generateRandomUsername(), generateRandomPassword());
                UserGrpc addFriend = new UserGrpc();
                addFriend.setUsername(createdUser.getUsername());
                userdataClient.sendInvitation(invitation.getUsername(), addFriend);
                createdUser.getInvitationsJsons().add(invitation);
            }
        }
    }

    private void createOutcomeInvitationsIfPresent(GenerateUser generateUser, UserGrpc createdUser) throws Exception {
        OutcomeInvitations invitations = generateUser.outcomeInvitations();
        if (invitations.handleAnnotation() && invitations.count() > 0) {
            for (int i = 0; i < invitations.count(); i++) {
                UserGrpc friend = apiRegister(generateRandomUsername(), generateRandomPassword());
                userdataClient.sendInvitation(createdUser.getUsername(), friend);
                createdUser.getInvitationsJsons().add(friend);
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
                createdUser.getFriendsJsons().add(friend);
            }
        }
    }
}
