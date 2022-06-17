import lombok.Data;
import lombok.NonNull;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class UserCreation {
    private String email;
    private String password;
    private String name;

    public UserCreation(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserCreation getRandom() {
        String email = RandomStringUtils.randomAlphanumeric(15)+"@test.test";
        String password = RandomStringUtils.randomAlphanumeric(15);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new UserCreation(email,password,name);
    }

    @Override
    public String toString() {
        return "{ \"email\": \"" + email + "\",\"password\": \"" + password + "\", \"firstName\": \"" + name + "\" }";
    }
}
