package restaurant;

import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.logging.Level;

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
        StringBuilder s = new StringBuilder();
        for (MenuItem i: o.getItems()){
            s.append("Cooked ");
            s.append(Integer.toString(i.getQuantity()));
            s.append(" ");
            s.append(i.getName());
            s.append(": removed ");
           for (Ingredient in: i.getIngredients()){
               s.append(Integer.toString(i.getQuantity()));
               s.append(" ");
               s.append(in.getName());
               if (i.getIngredients().indexOf(in) == i.getIngredients().size() - 1){
                   s.append(", ");
               }else{ s.append(" from inventory.");}
           }
        }
        RestaurantLogger.log(Level.INFO, "" + s.toString());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromInventory(Ingredient i) {
        if(inventory.get(i) > 0) {
            inventory.put(i, inventory.get(i) - 1);
            if(inventory.get(i) < 5){
                makeRestockRequest();
            }
        }
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
    public void makeRestockRequest() {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<Ingredient, Integer> entry: inventory.entrySet()) {
            if (entry.getValue() < 5) {
                sb.append("We need more " + entry.getKey().getName() + ".\n");
            }
        }
        try (FileWriter fw = new FileWriter("requests.txt")) {
            fw.write(sb.toString());
            fw.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    @Override
    public boolean enoughIngredients(Ingredient i, int amount) {
        return inventory.get(i) < amount;
    }
}
