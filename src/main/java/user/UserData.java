package user;

import lombok.Data;
import org.apache.commons.lang3.RandomStringUtils;

@Data
public class UserData {
    private String email;
    private String password;
    private String name;

    public UserData(String email, String password, String name) {
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public static UserData getRandom() {
        String email = RandomStringUtils.randomAlphanumeric(15).toLowerCase()+"@test.test";
        String password = RandomStringUtils.randomAlphanumeric(15);
        String name = RandomStringUtils.randomAlphabetic(10);

        return new UserData(email,password,name);
    }

    @Override
    public String toString() {
        return "{ \"email\": \"" + email + "\",\"password\": \"" + password + "\", \"name\": \"" + name + "\" }";
    }
}
