import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class InventoryImpl implements Inventory {
    private final Map<Ingredient, Integer> inventory = new HashMap<>();

    InventoryImpl(Menu menu) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        MenuItem[] burgerMenu = menu.getMenu();
        for (MenuItem item : burgerMenu) {
            ingredientSet.addAll(item.getIngredients());
        }
        //get all the ingredients from the menu
        for (Ingredient ingredient : ingredientSet) {
            inventory.put(ingredient, 10);
        }//initialize them all to 10

        //clear requests.txt
        try (FileWriter fw = new FileWriter("requests.txt", false)) {
            fw.write("");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addToInventory(Ingredient i, int amount) {
        inventory.put(i, inventory.get(i) + amount);
    }

    private void removeFromInventory(Ingredient i, int amount) {
        inventory.put(i, inventory.get(i) - amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromInventory(MenuItem item, Table t) {
        int uncookableItemNumber = 0;
        for (Ingredient ingredient : item.getIngredients()) {
            int quantity = item.getQuantity();
            int cookableItemNumber = inventory.get(ingredient);
            uncookableItemNumber = quantity - cookableItemNumber;

            if (uncookableItemNumber > 0) {
                removeFromInventory(ingredient, cookableItemNumber);
                //TODO: remove the uncooked items from the bill
                orderIngredient(ingredient);
            } else {
                //inventory >= request
                removeFromInventory(ingredient, quantity);
            }
        }
        if (uncookableItemNumber > 0) { //TODO: this is horrible.
            rejectItem(item, uncookableItemNumber);
            MenuItem i = new MenuItemImpl(item, uncookableItemNumber);
            t.addUncookedMenuitems(i);
        }
    }

    void rejectItem(MenuItem item, int quantity) {
        System.out.println("Sorry, but due to inventory shortages we are unable to cook " + quantity + " " + item.getName() + "(s).\n");
        //TODO: this also needs to remove the uncookable items from the bill
    }

    void orderIngredient(Ingredient ingredient) { //TODO: remove duplication of inventory orders in output file
        try (FileWriter fw = new FileWriter("requests.txt", true)) {
            fw.write("We need 10 more " + ingredient.getName() + ".\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Ingredient, Integer> getContents() {
        return inventory;
    }
}
