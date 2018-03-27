package restaurant;

import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

public class InventoryImpl implements Inventory {
    private final Map<Ingredient, Integer> inventory = new HashMap<>();

    InventoryImpl(Menu menu) {
        Set<Ingredient> ingredientSet = new HashSet<>();
        List<MenuItem> burgerMenu = menu.getMenu();
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
    public void addToInventory(Map<Ingredient, Integer> shipment) {
        for (Map.Entry<Ingredient, Integer> entry: shipment.entrySet()) {
            inventory.put(entry.getKey(), (inventory.get(entry.getKey()) + entry.getValue()));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromInventory(Order o) {
        for (Ingredient i : o.getIngredients()) {
            removeFromInventory(i);
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromInventory(Ingredient i) {
        if(inventory.get(i) > 0) {
            inventory.put(i, inventory.get(i) - 1);
        }
    }

    void orderIngredient(Ingredient ingredient) { //TODO: remove duplication of inventory orders in output file

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Map<Ingredient, Integer> getContents() {
        return inventory;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void makeRestockRequest(Ingredient i) {
        try (FileWriter fw = new FileWriter("requests.txt", true)) {
            fw.write("We need 10 more " + i.getName() + ".\n");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void printContents() {
        for(Map.Entry<Ingredient, Integer> entry : inventory.entrySet()){

        }
    }
}
