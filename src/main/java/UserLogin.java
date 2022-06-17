import lombok.Data;

@Data
public class UserLogin {
    private boolean success;
    private String accessToken;
    private String refreshToken;
    private User user;

    public UserLogin (boolean success, String accessToken, String refreshToken, User user) {
        this.success = success;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.user = user;
    }

    @Override
    public String toString() {
        return "{ \"success\": \"" + success + "\", \"accessToken\": \"" + accessToken + "\", \"refreshToken\": \"" + refreshToken + "\", \"user\": "+user+" }";
    }
}