import io.restassured.response.Response;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.apache.http.HttpStatus.*;

public class OrderCreateWithoutAuthTest {


    @Test
    public void checkCreateOrderWithIngredientsCorrectHashWithoutAuthReturn200True() {

        OrderCreate jsonOrder = OrderCreate.getOrder();
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrderWithoutAuth(jsonOrder);

        ordersClient.compareResponseCodeAndBodyAboutOrderCreation(response);
    }

    @Test
    public void checkCreateOrderWithoutIngredientsWithoutAuthReturn400False() {

        List<String> ingredient = new ArrayList<>();
        OrderCreate jsonOrder = new OrderCreate(ingredient);
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrderWithoutAuth(jsonOrder);
        ordersClient.compareCodeAndSuccessStatusAndMessageOrders(response, SC_BAD_REQUEST, false, "Ingredient ids must be provided");
    }

    @Test
    public void checkCreateOrderWrongIngredientsHashWithoutAuthReturn500() {
        List<String> ingredient = new ArrayList<>();
        ingredient.add("fdgfggdgfgdgfdg11111");
        ingredient.add("fdgfggdgfgdgfdg1111fdfdfff1");
        OrderCreate jsonOrder = new OrderCreate(ingredient);
        OrdersClient ordersClient = new OrdersClient();

        Response response = ordersClient.sendPostToCreateOrderWithoutAuth(jsonOrder);

        ordersClient.compareResponseCode500(response);
    }
}
