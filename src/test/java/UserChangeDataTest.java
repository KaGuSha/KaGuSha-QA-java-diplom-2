import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import user.UserClient;
import user.UserCredentials;
import user.UserData;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserChangeDataTest {
    private UserClient userClient;
    private String accessToken;
    private UserData jsonUserCreate;

    @Before
    public void setUp() {
        userClient = new UserClient();
        jsonUserCreate = UserData.getRandom();
        Response responseUserCreate = userClient.sentPostToCreateUser(jsonUserCreate);
        userClient.compareResponseCodeAndBodyReturn200True(responseUserCreate);

        UserCredentials jsonToLogin = UserCredentials.from(jsonUserCreate);
        Response responseUserLogin = userClient.sentPostToLogin(jsonToLogin);
        userClient.compareResponseCodeAndBodyReturn200True(responseUserLogin);
        accessToken = responseUserLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @DisplayName("Изменение имени пользователя для авторизованного пользователя")
    @Description("Авторизованный пользователй может изменить имя, адрес почты и пароль. Для неавторизованного пользователя вернется ошибка")
    @Test
    public void checkChangeNameWithAuth() {
        String email = jsonUserCreate.getEmail();
        String password = jsonUserCreate.getPassword();
        String name = "Change";

        String json = "{\"name\": \"" + name + "\"}";

        Response response = userClient.sentPatchToChangeUserData(accessToken, json);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        UserData userDataRefresh = new UserData(email, password, name);
        userClient.compareEmailAndNameInResponseBodyWithUserData(userDataRefresh, response);

        UserCredentials jsonToLoginAfterChange = UserCredentials.from(userDataRefresh);
        Response responseUserLogin = userClient.sentPostToLogin(jsonToLoginAfterChange);
        userClient.compareResponseCodeAndBodyReturn200True(responseUserLogin);
        accessToken = responseUserLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @DisplayName("Изменение адреса почты пользователя для авторизованного пользователя")
    @Description("Авторизованный пользователй может изменить имя, адрес почты и пароль. Для неавторизованного пользователя вернется ошибка")
    @Test
    public void checkChangeEmailWithAuth() {
        String email = "Change125@test.test";
        String password = jsonUserCreate.getPassword();
        String name = jsonUserCreate.getName();

        String json = "{\"email\": \"" + email + "\"}";

        Response response = userClient.sentPatchToChangeUserData(accessToken, json);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        UserData userDataRefresh = new UserData(email, password, name);
        System.out.println(response.asString());
        userClient.compareEmailAndNameInResponseBodyWithUserData(userDataRefresh, response);

        UserCredentials jsonToLoginAfterChange = UserCredentials.from(userDataRefresh);
        Response responseUserLogin = userClient.sentPostToLogin(jsonToLoginAfterChange);
        userClient.compareResponseCodeAndBodyReturn200True(responseUserLogin);
        accessToken = responseUserLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @DisplayName("Изменение пароля пользователя для авторизованного пользователя")
    @Description("Авторизованный пользователй может изменить имя, адрес почты и пароль. Для неавторизованного пользователя вернется ошибка")
    @Test
    public void checkChangePasswordWithAuth() {
        String email = jsonUserCreate.getEmail();
        String password = "chanGe20062022";
        String name = jsonUserCreate.getName();

        String json = "{\"password\": \"" + password + "\"}";

        Response response = userClient.sentPatchToChangeUserData(accessToken, json);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        UserData userDataRefresh = new UserData(email, password, name);
        userClient.compareEmailAndNameInResponseBodyWithUserData(userDataRefresh, response);

        UserCredentials jsonToLoginAfterChange = UserCredentials.from(userDataRefresh);
        Response responseUserLogin = userClient.sentPostToLogin(jsonToLoginAfterChange);
        userClient.compareResponseCodeAndBodyReturn200True(responseUserLogin);
        accessToken = responseUserLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @After
    public void tearDown() {
        if (accessToken.length() != 0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
