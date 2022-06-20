import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

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

    @Test
    public void checkChangeNameWithAuth() {

        String json = "{\"name\": \"change125\"}";
        Response response = userClient.sentPatchToChangeUserData(accessToken,json);
        System.out.println(response);

        userClient.compareResponseCodeAndBodyReturn200True(response);

        userClient.checkResponseBodyWithUserData(jsonUserCreate,response);

        String actual = response.then().extract().path("user.name");
        Assert.assertEquals("change125",actual);

        //userClient.sentPostToLogin(json)
    }

    @Test
    public void checkChangeEmailWithAuth() {


        String json3 = "{\"email\": \"Change125@test.test\"}";
        Response response1 = userClient.sentPatchToChangeUserData(accessToken,json3);
        System.out.println(response1);
        response1.then().assertThat().statusCode(200).and().body("success",is(true));

        System.out.println(response1.asString());
        String actual = response1.then().extract().path("user.email");
        System.out.println(actual);

        Assert.assertEquals("change125@test.test",actual);
    }

    @Test
    public void checkChangePasswordWithAuth() {
        UserCredentials json2 = UserCredentials.from(jsonUserCreate);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLoginResponse userLoginResponse = response.body().as(UserLoginResponse.class);
        String tokenBear = userLoginResponse.getAccessToken().substring(7);
        if (tokenBear!= accessToken) {
            accessToken = tokenBear;
        }

        String json3 = "{\"password\": \"Change125\"}";
        Response response1 = userClient.sentPatchToChangeUserData(accessToken,json3);
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



    @After
    public void tearDown() {
        if (accessToken.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
