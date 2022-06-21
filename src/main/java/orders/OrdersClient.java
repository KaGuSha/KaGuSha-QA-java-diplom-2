package orders;

import all.RestAssuredClient;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import java.util.List;

import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrdersClient extends RestAssuredClient {

    private final String ORDERS = "orders";

    @Step("Отправить запрос POST с авторизацией по токену и с json в теле для создания заказа аккаунта пользователя на /api/orders")
    public Response sendPostToCreateOrder (String accessToken, OrderCreate json){
        return reqSpec.auth().oauth2(accessToken).and().body(json).when().post(ORDERS);
    }

    @Step("Отправить запрос POST без авторизации по токену и с json в теле для создания заказа аккаунта пользователя на /api/orders")
    public Response sendPostToCreateOrderWithoutAuth (OrderCreate json){
        return reqSpec.and().body(json).when().post(ORDERS);
    }

    @Step("Отправить запрос GET с авторизации по токену для получения списка со всеми заказами пользователя на /api/orders")
    public Response sentGetToGetUsersOrders (String accessToken) {
        return  reqSpecGet.auth().oauth2(accessToken).when().get(ORDERS);
    }

    @Step("Отправить запрос GET без авторизации по токену для получения списка со всеми заказами пользователя на /api/orders")
    public Response sentGetToGetUsersOrdersWithoutAuth () {
        return  reqSpecGet.when().get(ORDERS);
    }

    @Step("Проверить, что код ответа 200 и значение атрибута success - true в теле ответа.")
    public void compareResponseCodeAndBodyAboutOrderCreation (Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("success", is(true)).and().body("order.number", greaterThan(0));
    }

    @Step("Проверить, что код ответа, значение атрибута success, значение атрибута message в теле ответа совпадает с ожидаемым.")
    public void compareCodeAndSuccessStatusAndMessageOrders(Response response, int expectedHttp, boolean expectedSuccess, String expectedMessage) {
        response.then().assertThat().statusCode(expectedHttp).and().body("success", is(expectedSuccess)).and().body("message", is(expectedMessage));
    }

    @Step("Проверить, что код ответа 500")
    public void compareResponseCode500 (Response response) {
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Step("Проверить, что код ответа 200 и значение атрибута total не пустое.")
    public void compareResponseCodeAndBody200TotalNotNull (Response response) {
        response.then().assertThat().statusCode(SC_OK).and().body("total", notNullValue());
    }

    @Step("Проверить, что в ответе в списке отображаются ожидаемое количество заказов на не больше 50.")
    public void isResponseBodyHaveOrdersList(Response response,int exceptionSize) {
        response.then().assertThat().body("orders.number",notNullValue());
        List<String> orderNumbers = response.then().extract().path("orders.number");
        assertThat(orderNumbers.size(), allOf(equalTo(exceptionSize), lessThanOrEqualTo(50)));
        }
}
