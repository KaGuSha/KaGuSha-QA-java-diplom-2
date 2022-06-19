import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class OrderCreateTest {


    protected final String URL = "https://stellarburgers.nomoreparties.site/api/";

    public final String ORDER = "orders";
    private UserClient userClient;
    private UserCreation json;
    String tokenBear1;


    @Before

    public void setUp() {
        userClient = new UserClient();
        json = UserCreation.getRandom();
        Response response = userClient.sentPostToCreateUser(json);
        tokenBear1 = userClient.compareResponseCodeAndBodyAboutCreation(response).getAccessToken();
    }

    @Test
    public void checkGetOrdersWithAuthReturn200Success() {

        String json1 = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa7a\"]}";
        Response response = given().baseUri(URL).log().all().header("Authorization", tokenBear1).and().header("Content-type", "application/json").and().body(json1)
                .when().post(ORDER);
        response.then().assertThat().statusCode(200).and().body("success", is(true)).and().body("order.number", greaterThan(0));

        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithoutAuthReturn200Success() {

        String json1 = "{\"ingredients\": [\"61c0c5a71d1f82001bdaaa6d\",\"61c0c5a71d1f82001bdaaa73\",\"61c0c5a71d1f82001bdaaa7a\"]}";
        Response response = given().baseUri(URL).log().all().and().header("Content-type", "application/json").and().body(json1)
                .when().post(ORDER);
        response.then().assertThat().statusCode(200).and().body("success", is(true)).and().body("order.number", greaterThan(0));

        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithoutAuthWithoutIngredientsReturn400False() {

        String json1 = "{\"ingredients\": []}";
        Response response = given().baseUri(URL).log().all().and().header("Content-type", "application/json").and().body(json1)
                .when().post(ORDER);
        response.then().assertThat().statusCode(400).and().body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));

        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithAuthWithoutIngredientsReturn400False() {

        String json1 = "{\"ingredients\": []}";
        Response response = given().baseUri(URL).log().all().header("Authorization", tokenBear1).and().header("Content-type", "application/json").and().body(json1)
                .when().post(ORDER);
        response.then().assertThat().statusCode(400).and().body("success", is(false)).and().body("message", is("Ingredient ids must be provided"));

        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithAuthWrongIngredientsHeshReturn500False() {

        String json1 = "{\"ingredients\": [\"fdgfggdgfgdgfdg11111\",\"fdgfggdgfgdgfdg1111fdfdfff1\"]}";
        Response response = given().baseUri(URL).log().all().header("Authorization", tokenBear1).and().header("Content-type", "application/json").and().body(json1)
                .when().post(ORDER);
        response.then().assertThat().statusCode(500);

        System.out.println(response.asString());
    }


    @After
    public void tearDown() {
        if (tokenBear1.length()!=0) {
            Response response = userClient.sentDeleteToRemoveUser(tokenBear1);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }

}
