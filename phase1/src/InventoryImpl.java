import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryImpl implements Inventory {
    private Map<Ingredient, Integer> inventory = new HashMap<>();

    InventoryImpl(Menu menu){
        Set<Ingredient> ingredientSet = new HashSet<>();
        MenuItem[] burgerMenu = menu.getMenu();
        for (MenuItem item : burgerMenu) ingredientSet.addAll(item.getIngredients());
        //get all the ingredients from the menu
        for (Ingredient ingredient : ingredientSet) inventory.put(ingredient, 10);
        //initialize them all to 10
    }

    //this method is called when the amount of a given ingredient is less than 10.
    // For now we assume that the reorder process is effective immediately (see Restaurant case cook ready).
    @Override
    public void addToInventory(Ingredient i, int amount) { inventory.put(i, inventory.get(i) + amount); }

    @Override
    public void removeFromInventory(Ingredient i, int amount) { inventory.put(i, inventory.get(i) - amount ); }

    @Override
    public Map<Ingredient, Integer> getContents() {
        return inventory;
    }
}
