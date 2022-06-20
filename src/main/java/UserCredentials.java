import lombok.Data;
import lombok.NonNull;

@Data
public class UserCredentials {
    private String email;
    private String password;

    public UserCredentials (@NonNull String email, String password) {
        this.email = email;
        this.password = password;
    }

    public UserCredentials (UserData user) {
        this.email = user.getEmail();
        this.password = user.getPassword();
    }

    public static UserCredentials from(UserData user) {
        return new UserCredentials(user);
    }
}
