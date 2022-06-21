import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import user.UserClient;
import user.UserCredentials;
import user.UserData;
import io.restassured.response.Response;
import org.junit.After;

import org.junit.Before;
import org.junit.Test;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;


public class UserCreateTest {
    private String accessToken1;
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @DisplayName("Создание уникального пользователя")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserWithAllFieldSuccessCreation() {
        UserData json = UserData.getRandom();

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndBodyReturn200True(response);
        userClient.checkAccessTokenAndRefreshTokenInResponseBodyNotNull(json, response);
        userClient.compareEmailAndNameInResponseBodyWithUserData(json,response);

        UserCredentials jsonToLogin = UserCredentials.from(json);
        Response responseToLogin = userClient.sentPostToLogin(jsonToLogin);
        userClient.compareResponseCodeAndBodyReturn200True(responseToLogin);

        accessToken1 = responseToLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @DisplayName("Создание пользователя, который уже зарегистрирован")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserAsExistingUserNotCreated403False() {
        UserData json = UserData.getRandom();

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        accessToken1 = response.then().extract().path("accessToken").toString().substring(7);

        Response responseToCreateAsExistingUser = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(responseToCreateAsExistingUser, SC_FORBIDDEN, false, "User already exists");
    }

    @DisplayName("Создание пользователя с незаполненными полями")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserEmptyFieldsNotCreated403False() {
        UserData json = new UserData("", "", "");

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");
        accessToken1 = "";
    }

    @DisplayName("Создание пользователя с незаполненным полем email")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserEmptyEmailNotCreated403() {
        String email = "";
        String password = "f1gh5gh6h46";
        String name = "gulnara";
        UserData json = new UserData(email, password, name);

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");
        accessToken1 = "";
    }

    @DisplayName("Создание пользователя с незаполненным полем password")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserEmptyPasswordNotCreated403() {
        String email = "fdjflsjlkjgsegh@test.test";
        String password = "";
        String name = "gulnara";

        UserData json = new UserData(email, password, name);

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");

        accessToken1 = "";
    }

    @DisplayName("Создание пользователя с незаполненным полем name")
    @Description("Для регистрации пользователя поля email,password и name обязательные. Значение поля email должно быть уникальным.")
    @Test
    public void createNewUserEmptyNameNotCreated403False() {
        String email = "fdjflsjlkjgsegh@test.test";
        String password = "fdj11g5ljk167";
        String name = "";

        UserData json = new UserData(email, password, name);

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");

        accessToken1 = "";
    }

    @After
    public void tearDown() {
        if (accessToken1.length() != 0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken1);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
