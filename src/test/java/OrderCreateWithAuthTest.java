import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;


public class OrderCreateWithAuthTest {

    private UserClient userClient;
    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        UserData jsonUser = UserData.getRandom();
        Response response = userClient.sentPostToCreateUser(jsonUser);
        accessToken = response.then().extract().path("accessToken").toString().substring(7);
    }

    @Test
    public void checkCreateOrderWithIngredientsCorrectHashWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response);
    }

    @Test
    public void checkCreateOrder2TimesWithIngredientsCorrectHashWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);
        Response response2 = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response);
        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response2);

    }

    @Test
    public void checkCreateOrderWithoutIngredientsWithAuthReturn400False() {
        List<String> ingredient = new ArrayList<>();
        OrderCreate jsonOrder = new OrderCreate(ingredient);
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareCodeAndSuccessStatusAndMessageOrders(response, SC_BAD_REQUEST, false, "Ingredient ids must be provided");
    }

    @Test
    public void checkCreateOrderWithAuthWrongIngredientsHashReturn500() {
        List<String> ingredient = new ArrayList<>();
        ingredient.add("111111111111111111");
        ingredient.add("ERdgdgf1T1");
        OrderCreate jsonOrder = new OrderCreate(ingredient);
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareResponseCode500(response);
    }

    @After
    public void tearDown() {
        if (accessToken.length() != 0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
