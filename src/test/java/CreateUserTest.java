import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.*;
import static org.apache.http.HttpStatus.SC_OK;

import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;


public class CreateUserTest {
    String URL = "https://stellarburgers.nomoreparties.site/api/";

    private String tokenBear;
    private String tokenBear1;
    private UserClient userClient;
    private UserLogin userLogin;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    public void createNewUserWithAllFieldSuccessCreation () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";
        UserCreation json = UserCreation.getRandom();

        Response response = userClient.sentPostToCreateUser(json);

        //Response response = given().baseUri(URL).log().all().header("Content-type", "application/json").and().body(json).when().post("auth/register");
        //userClient.compareResponseCodeAndBodyAboutCreation(response);
        //tokenBear = response.then().extract().path("accessToken");
        userLogin = userClient.compareResponseCodeAndBodyAboutCreation(response);
        tokenBear = userLogin.getAccessToken();

        Assert.assertNotNull(tokenBear);
        Assert.assertNotNull(userLogin.getRefreshToken());
        //Assert.assertEquals(json.getEmail(),userLogin.getUser().getEmail());
        Assert.assertEquals(json.getName(),userLogin.getUser().getName());

        UserCredentials jsonToLogin = UserCredentials.from(json);

        //String json2 = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\"}";
        Response responseToLogin = userClient.sentPostToLogin(jsonToLogin);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(responseToLogin);

        //given().baseUri(URL).log().all().header("Content-type", "application/json").and().body(json2).when().post("auth/login");
        //response2.then().assertThat().statusCode(SC_OK).and().body("success",is(true));

        tokenBear1 = responseToLogin.then().extract().path("accessToken");
        //System.out.println(tokenBear1);
        Assert.assertNotNull(tokenBear1);
    }

    @Test
    public void createNewUserAsExistingUserNotCreated403 () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";
        UserCreation json = UserCreation.getRandom();

        Response response = userClient.sentPostToCreateUser(json);

        //Response response = given().baseUri(URL).log().all().header("Content-type", "application/json").and().body(json).when().post("auth/register");
        //userClient.compareResponseCodeAndBodyAboutCreation(response);
        //tokenBear = response.then().extract().path("accessToken");
        userLogin = userClient.compareResponseCodeAndBodyAboutCreation(response);
        tokenBear1 = userLogin.getAccessToken();

        Assert.assertNotNull(tokenBear1);
        Assert.assertNotNull(userLogin.getRefreshToken());
        //Assert.assertEquals(json.getEmail(),userLogin.getUser().getEmail());
        Assert.assertEquals(json.getName(),userLogin.getUser().getName());

        UserCredentials jsonToLogin = UserCredentials.from(json);

        //String json2 = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\"}";
        Response responseToLogin = userClient.sentPostToLogin(jsonToLogin);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(responseToLogin);

        Response responseToCreateAsExistingUser = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyAboutNotCreated(responseToCreateAsExistingUser);
    }

    @Test
    public void createNewUserEmptyFieldsNotCreated403 () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";

        UserCreation json = new UserCreation("","","");

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyAboutNotCreatedWithoutRequiredField(response);
        tokenBear1 = "";
    }

    @Test
    public void createNewUserEmptyEmailNotCreated403 () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";

        String email = "";
        String password = "fdjflsjlkjgsegh";
        String name = "sllkselkjc";

        UserCreation json = new UserCreation(email,password,name);

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyAboutNotCreatedWithoutRequiredField(response);
        tokenBear1 = "";
    }

    @Test
    public void createNewUserEmptyPasswordNotCreated403 () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";

        String email = "fdjflsjlkjgsegh@test.test";
        String password = "";
        String name = "sllkselkjc";

        UserCreation json = new UserCreation(email,password,name);

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyAboutNotCreatedWithoutRequiredField(response);
        tokenBear1 = "";
    }

    @Test
    public void createNewUserEmptyNameNotCreated403 () {
        //String json = "{\"email\": \"lkdfsfdsfjfskjgljlddddddfk@test.test\", \"password\": \"012345987d\", \"name\": \"guka\"}";

        String email = "fdjflsjlkjgsegh@test.test";
        String password = "fdjflsjlkjgsegh";
        String name = "";

        UserCreation json = new UserCreation(email,password,name);

        Response response = userClient.sentPostToCreateUser(json);
        userClient.compareResponseCodeAndBodyAboutNotCreatedWithoutRequiredField(response);
        tokenBear1 = "";
    }

    @After
    public void tearDown() {
        if (tokenBear1.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(tokenBear1);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
 }
