package menu;

import java.util.List;

public interface Menu {
    /**
     * Returns the entire Mnu as a list of MenuItems
     *
     * @return the menu.Menu
     */
    List<MenuItem> getMenu();

    /**
     * Returns the menu.Ingredient query from the menu.Ingredient menu if it exists.
     *
     * @param query the String that is to be searched for on the menu.Ingredient menu
     * @return the menu.Ingredient from the menu.Ingredient menu with the same name
     */
    Ingredient getMenuIngredient(String query);

    MenuItem getMenuItem(String itemName);
}
