import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.is;

public class UserLoginTest {
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
    public void checkLoginWithExistingUserLoginSuccessful() {
        UserCredentials json2 = UserCredentials.from(json);
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginSuccess(response);
        UserLogin userLogin = response.body().as(UserLogin.class);

        //Assert.assertEquals(json2.getEmail(),userLogin.getUser().getEmail());
        Assert.assertEquals(json.getName(),userLogin.getUser().getName());
        String tokenBear = userLogin.getAccessToken();
        if (tokenBear!=tokenBear1) {
            tokenBear1 = tokenBear;
        }
    }

    @Test
    public void checkLoginWithWrongPasswordLoginUnsuccessful() {
        UserCredentials json2 = new UserCredentials(json.getEmail(), json.getPassword()+"123456");
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginWrong(response);
    }

    @Test
    public void checkLoginWithWrongEmailLoginUnsuccessful() {
        UserCredentials json2 = new UserCredentials(json.getEmail()+"123456", json.getPassword());
        Response response = userClient.sentPostToLogin(json2);
        userClient.compareResponseCodeAndBodyAboutLoginWrong(response);

    }

    @After
    public void tearDown() {
        if (tokenBear1.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(tokenBear1);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
