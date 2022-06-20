import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;


import static org.apache.http.HttpStatus.*;

public class UserChangeDataWithoutAuthTest {
    private UserClient userClient;

    @Before
    public void setUp() {
        userClient = new UserClient();
    }

    @Test
    public void checkChangeNameWithoutAuthReturn401False() {
        String json = "{\"name\": \"change125\"}";
        Response response = userClient.sentPatchToChangeWithoutAuthUserData(json);
        System.out.println(response.asString());

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response,SC_UNAUTHORIZED,false,"You should be authorised");
    }

    @Test
    public void checkChangeEmailWithoutAuthReturn401False() {
        String json = "{\"email\": \"change20062022@test.test\"}";
        Response response = userClient.sentPatchToChangeWithoutAuthUserData(json);
        System.out.println(response.asString());

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response,SC_UNAUTHORIZED,false,"You should be authorised");
    }

    @Test
    public void checkChangePasswordWithoutAuthReturn401False() {
        String json = "{\"password\": \"change20062022\"}";
        Response response = userClient.sentPatchToChangeWithoutAuthUserData(json);
        System.out.println(response.asString());

        userClient.compareResponseCodeAndSuccessStatusAndMessageUsers(response,SC_UNAUTHORIZED,false,"You should be authorised");
    }
}
