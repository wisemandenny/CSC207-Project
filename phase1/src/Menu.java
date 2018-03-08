public interface Menu {
    /**
     * Returns the entire menu as a list of MenuItems
     *
     * @return      the menu
     */
    MenuItem[] getMenu();

    /**
     * Returns the MenuItem item from the menu if it exists.
     *
     * @param item  the MenuItem that is to be searched for on the menu.
     * @return      the MenuItem from the menu with the same name.
     */
    MenuItem getMenuItem(MenuItem item);

    /**
     * Returns the Ingredient query from the ingredient menu if it exists.
     *
     * @param query  the Ingredient that is to be searched for on the ingredient menu.
     * @return      the Ingredient from the ingredient menu with the same name.
     */
    Ingredient getMenuIngredient(Ingredient query);
}
