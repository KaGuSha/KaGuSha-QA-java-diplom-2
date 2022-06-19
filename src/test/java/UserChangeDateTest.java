import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class UserChangeDateTest {
    private UserClient userClient;
    private String tokenBear1;
    private UserCreation json;

    @Before

    public void setUp() {
        userClient = new UserClient();
        json = UserCreation.getRandom();
        Response response = userClient.sentPostToCreateUser(json);
        tokenBear1 = userClient.compareResponseCodeAndBodyAboutCreation(response).getAccessToken();
    }

    @Test
    public void checkChangeNameWithAuth() {
        UserCredentials json2 = UserCredentials.from(json);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLogin userLogin = response.body().as(UserLogin.class);
        String tokenBear = userLogin.getAccessToken();
        if (tokenBear!=tokenBear1) {
            tokenBear1 = tokenBear;
        }

        String json3 = "{\"name\": \"change125\"}";
        Response response1 = userClient.sentPatchToChangeUserData(tokenBear1,json3);
        System.out.println(response1);
        response1.then().assertThat().statusCode(200).and().body("success",is(true));

        System.out.println(response1.asString());
        String actual = response1.then().extract().path("user.name");
        System.out.println(actual);

        Assert.assertEquals("change125",actual);
    }

    @Test
    public void checkChangeEmailWithAuth() {
        UserCredentials json2 = UserCredentials.from(json);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLogin userLogin = response.body().as(UserLogin.class);
        String tokenBear = userLogin.getAccessToken();
        if (tokenBear!=tokenBear1) {
            tokenBear1 = tokenBear;
        }

        String json3 = "{\"email\": \"Change125@test.test\"}";
        Response response1 = userClient.sentPatchToChangeUserData(tokenBear1,json3);
        System.out.println(response1);
        response1.then().assertThat().statusCode(200).and().body("success",is(true));

        System.out.println(response1.asString());
        String actual = response1.then().extract().path("user.email");
        System.out.println(actual);

        Assert.assertEquals("change125@test.test",actual);
    }

    @Test
    public void checkChangePasswordWithAuth() {
        UserCredentials json2 = UserCredentials.from(json);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLogin userLogin = response.body().as(UserLogin.class);
        String tokenBear = userLogin.getAccessToken();
        if (tokenBear!=tokenBear1) {
            tokenBear1 = tokenBear;
        }

        String json3 = "{\"password\": \"Change125\"}";
        Response response1 = userClient.sentPatchToChangeUserData(tokenBear1,json3);
        System.out.println(response1);
        response1.then().assertThat().statusCode(200).and().body("success",is(true));

        //System.out.println(response1.asString());
        //String actual = response1.then().extract().path("user.pass");
        //System.out.println(actual);

        //Assert.assertEquals("change125@test.test",actual);

        UserCredentials json4 = new UserCredentials(json2.getEmail(),"Change125");
        Response response3 = userClient.sentPostToLogin(json4);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response3);
    }

    @Test
    public void checkChangeNameWithoutAuth() {
        UserCredentials json2 = UserCredentials.from(json);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLogin userLogin = response.body().as(UserLogin.class);
        String tokenBear = userLogin.getAccessToken();
        if (tokenBear!=tokenBear1) {
            tokenBear1 = tokenBear;
        }

        String json3 = "{\"name\": \"change125\"}";
        Response response1 = userClient.sentPatchToChangeWithoutAuthUserData(json3);
        System.out.println(response1.asString());
        response1.then().assertThat().statusCode(401).and().body("success",is(false)).and().body("message",is("You should be authorised"));


    }

    @After
    public void tearDown() {
        if (tokenBear1.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(tokenBear1);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
