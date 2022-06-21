import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import user.UserClient;
import user.UserCredentials;
import user.UserData;
import io.restassured.response.Response;
import org.junit.*;

import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;

public class UserLoginResponseTest {
    private static UserClient userClient;
    private static String accessToken;
    private static UserData jsonUserCreate;

    @BeforeClass
    public static void setUp() {
        userClient = new UserClient();
        jsonUserCreate = UserData.getRandom();
        Response response = userClient.sentPostToCreateUser(jsonUserCreate);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        accessToken = response.then().extract().path("accessToken").toString().substring(7);
    }

    @DisplayName("Логин под существующим пользователем")
    @Description("Для логина необходио передать верную пару email-password в теле запроса.")
    @Test
    public void checkLoginWithExistingUserLoginSuccessful() {
        UserCredentials json = UserCredentials.from(jsonUserCreate);
        Response response = userClient.sentPostToLogin(json);

        userClient.compareResponseCodeAndBodyReturn200True(response);

        userClient.checkAccessTokenAndRefreshTokenInResponseBodyNotNull(jsonUserCreate, response);
        userClient.compareEmailAndNameInResponseBodyWithUserData(jsonUserCreate,response);
        /*
        String accessToken1 = response.then().extract().path("accessToken").toString().substring(7);
        if (accessToken1 != accessToken) {
            accessToken = accessToken1;
        }*/
    }

    @DisplayName("Логин с неверным паролем")
    @Description("Для логина необходио передать верную пару email-password в теле запроса.")
    @Test
    public void checkLoginWithWrongPassword401False() {
        UserCredentials json = new UserCredentials(jsonUserCreate.getEmail(), jsonUserCreate.getPassword() + "123456");

        Response response = userClient.sentPostToLogin(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_UNAUTHORIZED, false, "email or password are incorrect");
    }

    @DisplayName("Логин под несуществующим пользователем")
    @Description("Для логина необходио передать верную пару email-password в теле запроса.")
    @Test
    public void checkLoginWithWrongEmail401False() {
        UserCredentials json = new UserCredentials(jsonUserCreate.getEmail() + "123456", jsonUserCreate.getPassword());

        Response response = userClient.sentPostToLogin(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_UNAUTHORIZED, false, "email or password are incorrect");
    }

    @DisplayName("Логин под несуществующим пользователем с неверным паролем")
    @Description("Для логина необходио передать верную пару email-password в теле запроса.")
    @Test
    public void checkLoginWithWrongEmailWrongPassword401False() {
        UserCredentials json = new UserCredentials( jsonUserCreate.getEmail()+"123456", jsonUserCreate.getPassword()+"15d4fs648");

        Response response = userClient.sentPostToLogin(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_UNAUTHORIZED, false, "email or password are incorrect");
    }

    @AfterClass
    public static void tearDown() {
        if (accessToken.length() != 0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
