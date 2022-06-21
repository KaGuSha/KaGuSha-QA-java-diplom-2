package user;

import lombok.Data;
import lombok.NonNull;

@Data
public class User {

    private String email;
    private String name;

    public User(@NonNull String email, String name) {
        this.email = email;
        this.name = name;
    }

    @Override
    public String toString() {
        return "{ \"email\": \"" + email + "\", \"name\": \"" + name + "\" }";
    }
}

