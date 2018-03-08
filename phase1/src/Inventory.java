import java.util.Map;
import java.util.List;

public interface Inventory {
    void addToInventory(Ingredient i, int amount);
    void removeFromInventory(MenuItem item, Table t);

    Map<Ingredient, Integer> getContents();
}
