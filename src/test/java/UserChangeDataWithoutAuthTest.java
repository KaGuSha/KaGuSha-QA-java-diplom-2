import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class UserChangeDataWithoutAuthTest {

    private UserClient userClient;
    private String accessToken;
    private UserData jsonUserCreate;

    @Before

    public void setUp() {
        userClient = new UserClient();
        jsonUserCreate = UserData.getRandom();
        Response response = userClient.sentPostToCreateUser(jsonUserCreate);
        userClient.compareResponseCodeAndBodyReturn200True(response);
        accessToken = response.then().extract().path("accessToken").toString().substring(7);
    }

    @Test
    public void checkChangeNameWithoutAuth() {
        UserCredentials json2 = UserCredentials.from(jsonUserCreate);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLoginResponse userLoginResponse = response.body().as(UserLoginResponse.class);
        String tokenBear = userLoginResponse.getAccessToken().substring(7);
        if (tokenBear!= accessToken) {
            accessToken = tokenBear;
        }

        String json3 = "{\"name\": \"change125\"}";
        Response response1 = userClient.sentPatchToChangeWithoutAuthUserData(json3);
        System.out.println(response1.asString());
        response1.then().assertThat().statusCode(401).and().body("success",is(false)).and().body("message",is("You should be authorised"));


    }

    @After
    public void tearDown() {
        if (accessToken.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
