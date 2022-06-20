import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;


public class UserClient extends RestAssuredClient{

    private final String USER_REGISTRATION =  "auth/register";
    private final String USER_AUTH = "/auth/user";
    private final String USER_LOGIN = "auth/login";

    public Response sentPostToCreateUser(UserData userData) {
        return reqSpec.and().body(userData).when().post(USER_REGISTRATION);
    }

    public Response sentPostToLogin(UserCredentials userCredentials) {
        return reqSpec.and().body(userCredentials).when().post(USER_LOGIN);
    }

     public Response sentDeleteToRemoveUser(String accessToken) {
        return reqSpecGet.auth().oauth2(accessToken).when().delete(USER_AUTH);
     }

    public Response sentPatchToChangeUserData(String accessToken,String json) {
        return reqSpec.auth().oauth2(accessToken).and().body(json).when().patch(USER_AUTH);
    }

    public Response sentPatchToChangeWithoutAuthUserData(String json) {
        return reqSpec.and().body(json).when().patch(USER_AUTH);
    }

    public void checkResponseBodyWithUserData(UserData json, Response response) {
        assertThat("Фактическое значение accessToken пустое",response.then().extract().path("accessToken"),notNullValue());
        assertThat("Фактическое значение refreshToken пустое",response.then().extract().path("refreshToken"),notNullValue());
        assertThat("Фактическое имя пользователя отличается от ожидаемого",response.then().extract().path("user.name"),is(json.getName()));
        assertThat("Фактическое имя почты отличается от ожидаемой",response.then().extract().path("user.email"),is(json.getEmail().toLowerCase()));
    }

    public void checkResponseBodyAfterChangeWithUserData(UserData json, Response response) {
        assertThat("Фактическое значение success false",response.then().extract().path("success"),is(true));
        assertThat("Фактическое имя почты отличается от ожидаемой",response.then().extract().path("user.email"),is(json.getEmail().toLowerCase()));
        assertThat("Фактическое имя пользователя отличается от ожидаемого",response.then().extract().path("user.name"),is(json.getName()));
    }

    public void compareResponseCodeAndBodyReturn200True(Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("success", is(true));
    }

    public void compareResponseCodeAndSuccessStatusAndMessageUsers(Response response, int expectedHttp, boolean expectedSuccess, String expectedMessage) {
        response.then().assertThat().statusCode(expectedHttp).and().body("success", is(expectedSuccess)).and().body("message", is(expectedMessage));
    }

    public void compareResponseCodeAndBodyAboutRemove(Response response) {
        response.then().assertThat().statusCode(SC_ACCEPTED).and().body("success", is(true)).and().body("message", is("User successfully removed"));
    }
}
