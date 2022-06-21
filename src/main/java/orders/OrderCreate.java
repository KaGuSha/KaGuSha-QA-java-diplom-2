package orders;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OrderCreate {
    private List<String> ingredients;

    public OrderCreate(List<String> ingredients) {
        this.ingredients = ingredients;
    }

    public static OrderCreate getOrder () {
        List <String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        ingredients.add("61c0c5a71d1f82001bdaaa73");
        ingredients.add("61c0c5a71d1f82001bdaaa7a");

        return new OrderCreate(ingredients);
    }

    @Override
    public String toString() {
        return "{\"ingredients\": [\"" + ingredients + "\"]}";
    }
}
