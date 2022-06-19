import io.restassured.response.Response;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;


public class UserClient extends RestAssuredClient{

    private final String USER_REGISTRATION =  "auth/register";
    private final String USER_AUTH = "/auth/user";
    private final String USER_LOGIN = "auth/login";

    public Response sentPostToCreateUser(UserCreation userCreation) {
        Response response = reqSpec.and().body(userCreation).when().post(USER_REGISTRATION);
        return response;
    }

    public UserLogin compareResponseCodeAndBodyAboutCreation(Response response) {
        response.then().assertThat().statusCode(200).and().body("success", is(true));
        return response.body().as(UserLogin.class);
    }

    public void compareResponseCodeAndBodyAboutNotCreated(Response response) {
        response.then().assertThat().statusCode(403).and().body("success", is( false)).and().body("message",is("User already exists"));
    }

    public void compareResponseCodeAndBodyAboutNotCreatedWithoutRequiredField(Response response) {
        response.then().assertThat().statusCode(403).and().body("success", is( false)).and().body("message",is("Email, password and name are required fields"));
    }


    public Response sentPostToLogin(UserCredentials userCredentials) {
        Response response = reqSpec.and().body(userCredentials).when().post("auth/login");
        return response;
    }

    public void compareResponseCodeAndBodyAboutLoginSuccess(Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("success",is(true));
    }

    public void compareResponseCodeAndBodyAboutLoginWrong(Response response) {
        response.then().assertThat().statusCode(401).and().body("success", is(false)).and().body("message", is("email or password are incorrect"));
    }
    public Response sentDeleteToRemoveUser(String accessToken) {
        Response response = reqSpecGet.header("Authorization", accessToken).when().delete(USER_AUTH);
        response.then().assertThat().statusCode(SC_ACCEPTED);
        return response;
    }

    public Response sentPatchToChangeUserData(String accessToken,String json) {
        Response response = reqSpec.header("Authorization", accessToken).and().body(json).when().patch(USER_AUTH);
        return response;
    }

    public Response sentPatchToChangeWithoutAuthUserData(String json) {
        Response response = reqSpec.and().body(json).when().patch(USER_AUTH);
        return response;
    }

    public void compareResponseCodeAndBodyAboutRemove(Response response) {
        response.then().assertThat().statusCode(SC_ACCEPTED).and().body("success", is(true)).and().body("message", is("User successfully removed"));
    }


}
