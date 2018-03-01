import java.util.HashMap;
import java.util.Map;

public class InventoryImpl implements Inventory {
    private Map<Ingredient, Integer> inventory = new HashMap<>();

    @Override
    public void addToInventory(Ingredient i, int amount) {
        inventory.put(i, inventory.get(i) + amount);
    }

    @Override
    public void removeFromInventory(Ingredient i, int amount) {
        inventory.put(i, inventory.get(i) - amount);
    }

    @Override
    public Map<Ingredient, Integer> getContents() {
        return inventory;
    }
}
