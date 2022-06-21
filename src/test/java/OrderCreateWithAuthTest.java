import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import orders.OrderCreate;
import orders.OrdersClient;
import user.UserClient;
import user.UserData;
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

    @DisplayName("Создание заказа с авторизацией и корректными хешами ингредиетов")
    @Description("Заказ создается, если в теле запроса передаются корректные хеш коды ингредиентов.")
    @Test
    public void checkCreateOrderWithIngredientsCorrectHashWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response);
    }

    @DisplayName("Создание двух одинаковых корректных заказов на одного пользователя")
    @Description("Заказ создается, если в теле запроса передаются корректные хеш коды ингредиентов.")
    @Test
    public void checkCreateOrder2TimesWithIngredientsCorrectHashWithAuthReturn200True() {
        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);
        Response response2 = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response);
        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response2);
    }

    @DisplayName("Создание заказа с авторизацией, но без ингредиентов в теле запроса")
    @Description("Заказ создается, если в теле запроса передаются корректные хеш коды ингредиентов.")
    @Test
    public void checkCreateOrderWithoutIngredientsWithAuthReturn400False() {
        List<String> ingredient = new ArrayList<>();
        OrderCreate jsonOrder = new OrderCreate(ingredient);
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrder(accessToken, jsonOrder);

        ordersClient.compareCodeAndSuccessStatusAndMessageOrders(response, SC_BAD_REQUEST, false, "Ingredient ids must be provided");
    }

    @DisplayName("Создание заказа с авторизацией, но с неверными хеш кодами ингредиентов в теле запроса")
    @Description("Заказ создается, если в теле запроса передаются корректные хеш коды ингредиентов.")
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
