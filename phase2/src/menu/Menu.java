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

    /**
     * Returns the MenuItem with the name itemName.
     *
     * @param itemName the MenuItem's name
     * @return  a MenuItem
     */
    MenuItem getMenuItem(String itemName);

    /**
     * Returns a List of all Ingredients.
     *
     * @return  a List of all the Ingredients.
     */
    List<Ingredient> getAllIngredients();
}
