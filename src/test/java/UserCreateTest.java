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

    @Test
    public void createNewUserWithAllFieldSuccessCreation() {
        UserData json = UserData.getRandom();

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndBodyReturn200True(response);
        userClient.checkResponseBodyWithUserData(json, response);

        UserCredentials jsonToLogin = UserCredentials.from(json);
        Response responseToLogin = userClient.sentPostToLogin(jsonToLogin);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(responseToLogin);

        accessToken1 = responseToLogin.then().extract().path("accessToken").toString().substring(7);
    }

    @Test
    public void createNewUserAsExistingUserNotCreated403False() {
        UserData json = UserData.getRandom();

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        accessToken1 = response.then().extract().path("accessToken").toString().substring(7);

        Response responseToCreateAsExistingUser = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(responseToCreateAsExistingUser, SC_FORBIDDEN, false, "User already exists");
    }

    @Test
    public void createNewUserEmptyFieldsNotCreated403False() {
        UserData json = new UserData("", "", "");

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");
        accessToken1 = "";
    }

    @Test
    public void createNewUserEmptyEmailNotCreated403() {
        String email = "";
        String password = "fdjflsjl15646";
        String name = "gulnara";
        UserData json = new UserData(email, password, name);

        Response response = userClient.sentPostToCreateUser(json);

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response, SC_FORBIDDEN, false, "Email, password and name are required fields");
        accessToken1 = "";
    }

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

    @Test
    public void createNewUserEmptyNameNotCreated403False() {
        String email = "fdjflsjlkjgsegh@test.test";
        String password = "fdjflsjlk15767";
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
