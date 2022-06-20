import io.restassured.response.Response;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrdersClient extends RestAssuredClient{

    private final String ORDERS = "orders";

    public Response sendPostToCreateOrder (String accessToken,OrderCreate json){
        return reqSpec.auth().oauth2(accessToken).and().body(json).when().post(ORDERS);
    }

    public Response sendPostToCreateOrderWithoutAuth (OrderCreate json){
        return reqSpec.and().body(json).when().post(ORDERS);
    }

    public Response sentGetToGetUsersOrders (String accessToken) {
        return  reqSpecGet.auth().oauth2(accessToken).when().get(ORDERS);
    }

    public Response sentGetToGetUsersOrdersWithoutAuth () {
        return  reqSpecGet.when().get(ORDERS);
    }

    public void compareResponseCodeAndBodyAboutOrderCreation (Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("success", is(true)).and().body("order.number", greaterThan(0));
    }

    public void compareCodeAndSuccessStatusAndMessageOrders(Response response, int expectedHttp, boolean expectedSuccess, String expectedMessage) {
        response.then().assertThat().statusCode(expectedHttp).and().body("success", is(expectedSuccess)).and().body("message", is(expectedMessage));
    }

    public void compareResponseCode500 (Response response) {
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    public void compareResponseCodeAndBody200TotalNotNull (Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("total", notNullValue());
    }

    public void isResponseBodyHaveOrdersList(Response response,int exceptionSize) {
        response.then().assertThat().body("orders.number",notNullValue());
        List<String> orderNumbers = response.then().extract().path("orders.number");
        assertThat(orderNumbers.size(), allOf(equalTo(exceptionSize), lessThanOrEqualTo(50)));
        }
}
