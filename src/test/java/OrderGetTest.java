import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class OrderGetTest {

    private UserClient userClient;

    String accessToken;

    @Before
    public void setUp() {
        userClient = new UserClient();
        UserData jsonUser = UserData.getRandom();
        Response response = userClient.sentPostToCreateUser(jsonUser);
        accessToken = userClient.compareResponseCodeAndBodyAboutCreation(response).getAccessToken().substring(7);
    }

    @Test
    public void checkGetOrdersWithOrderWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response responseOrderCreate = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);
        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(responseOrderCreate);


        Response response = ordersClient.sentGetToGetUsersOrders(accessToken);
        ordersClient.compareResponseCodeAndBody200TotalNotNull(response);

        ordersClient.isResponseBodyHaveOrdersList(response,1);
    }

    @Test
    public void checkGetOrdersWith2OrdersWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();

        List<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa71");
        OrderCreate jsonOrder2 = new OrderCreate(ingredients);

        OrdersClient ordersClient = new OrdersClient();

        Response responseOrderCreate = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);
        Response responseOrderCreate2 = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder2);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(responseOrderCreate);
        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(responseOrderCreate2);

        Response response = ordersClient.sentGetToGetUsersOrders(accessToken);

        ordersClient.compareResponseCodeAndBody200TotalNotNull(response);
        ordersClient.isResponseBodyHaveOrdersList(response,2);
    }

    @Test
    public void checkGetOrdersWithoutOrdersWithAuthReturn200True() {
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sentGetToGetUsersOrders(accessToken);
        ordersClient.compareResponseCodeAndBody200TotalNotNull(response);
        System.out.println(response.asString());
    }

    @Test
    public void checkGetOrdersWithoutAuthReturn401False() {
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sentGetToGetUsersOrdersWithoutAuth();
        ordersClient.compareCodeAndSuccessStatusAndMessageOrders(response, SC_UNAUTHORIZED, false, "You should be authorised");
    }

    @After
    public void tearDown() {
        if (accessToken.length() != 0) {
            Response response = userClient.sentDeleteToRemoveUser(accessToken);
            userClient.compareResponseCodeAndBodyAboutRemove(response);
        }
    }
}
