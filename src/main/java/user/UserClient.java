package user;

import all.RestAssuredClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;

import static org.hamcrest.Matchers.*;


public class UserClient extends RestAssuredClient {

    private final String USER_REGISTRATION = "auth/register";
    private final String USER_AUTH = "/auth/user";
    private final String USER_LOGIN = "auth/login";

    @Step("Отправить запрос POST с json в теле для регистрации аккаунта пользователя на /api/auth/register")
    public Response sentPostToCreateUser(UserData userData) {
        return reqSpec.and().body(userData).when().post(USER_REGISTRATION);
    }

    @Step("Отправить запрос POST с json в теле для авторизации пользователя  на /api/auth/user")
    public Response sentPostToLogin(UserCredentials userCredentials) {
        return reqSpec.and().body(userCredentials).when().post(USER_LOGIN);
    }

    @Step("Отправить запрос DELETE с авторизацией по токену для удаления аккаунта пользователя на /api/auth/user")
    public Response sentDeleteToRemoveUser(String accessToken) {
        return reqSpecGet.auth().oauth2(accessToken).when().delete(USER_AUTH);
    }

    @Step("Отправить запрос PATCH с авторизацией по токену и с json в теле для изменения данных пользователя на /api/auth/user")
    public Response sentPatchToChangeUserData(String accessToken, String json) {
        return reqSpec.auth().oauth2(accessToken).and().body(json).when().patch(USER_AUTH);
    }

    @Step("Отправить запрос PATCH без авторизации по токену и с json в теле для изменения данных пользователя на /api/auth/user")
    public Response sentPatchToChangeWithoutAuthUserData(String json) {
        return reqSpec.and().body(json).when().patch(USER_AUTH);
    }

    @Step("Проверить, что поля accessToken и refreshToken не пустые")
    public void checkAccessTokenAndRefreshTokenInResponseBodyNotNull(UserData json, Response response) {
        assertThat("Фактическое значение accessToken пустое", response.then().extract().path("accessToken"), notNullValue());
        assertThat("Фактическое значение refreshToken пустое", response.then().extract().path("refreshToken"), notNullValue());
    }

    @Step("Проверить, что поля name и email в теле ответа совпадают с ожидаемым значением")
    public void compareEmailAndNameInResponseBodyWithUserData(UserData json, Response response) {
        assertThat("Фактическое имя почты отличается от ожидаемой", response.then().extract().path("user.email"), is(json.getEmail().toLowerCase()));
        assertThat("Фактическое имя пользователя отличается от ожидаемого", response.then().extract().path("user.name"), is(json.getName()));
    }

    @Step("Проверить, что код ответа 200 и значение атрибута success - true в теле ответа.")
    public void compareResponseCodeAndBodyReturn200True(Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("success", is(true));
    }

    @Step("Проверить, что код ответа, значение атрибута success, значение атрибута message в теле ответа совпадает с ожидаемым.")
    public void compareResponseCodeAndSuccessStatusAndMessageUsers(Response response, int expectedHttp, boolean expectedSuccess, String expectedMessage) {
        response.then().assertThat().statusCode(expectedHttp).and().body("success", is(expectedSuccess)).and().body("message", is(expectedMessage));
    }

    @Step("Проверить, что код ответа 200, значение атрибута success - true, и значение атрибута message совпадает с \"User successfully removed\".")
    public void compareResponseCodeAndBodyAboutRemove(Response response) {
        response.then().assertThat().statusCode(SC_ACCEPTED).and().body("success", is(true)).and().body("message", is("User successfully removed"));
    }
}
