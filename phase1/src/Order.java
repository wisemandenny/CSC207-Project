public interface Order {
    MenuItemImpl[] getItems();
    double getTotalPrice();
    Ingredient[] getIngredients();
}
