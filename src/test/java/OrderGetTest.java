import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.requestSpecification;
import static org.hamcrest.Matchers.*;

public class OrderGetTest {
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
    public void  checkGetOrdersWithAuthReturn200Success() {
        Response response = given().baseUri(URL).log().all().header("Authorization",tokenBear1).when().get(ORDER+"/all");
        response.then().assertThat().statusCode(200).and().body("total", notNullValue());

        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithoutAuthReturn401False() {
        Response response = given().baseUri(URL).log().all().when().get(ORDER);
        response.then().assertThat().statusCode(401).and().body("success", is(false)).and().body("message",is("You should be authorised"));
    }
}
