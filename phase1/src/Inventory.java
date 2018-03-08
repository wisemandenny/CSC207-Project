import java.util.Map;
import java.util.List;

public interface Inventory {
    void addToInventory(Ingredient i, int amount);
    List<MenuItem> getUncookedMenuItems();
    void removeFromInventory(MenuItem item);

    Map<Ingredient, Integer> getContents();
}
