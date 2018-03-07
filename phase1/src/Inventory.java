import java.util.Map;

public interface Inventory {
    void addToInventory(Ingredient i, int amount);

    void removeFromInventory(MenuItem item);

    Map<Ingredient, Integer> getContents();
}
