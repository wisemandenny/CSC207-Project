public class OrderImpl implements Order {
    @Override
    public MenuItemImpl[] getItems() {
        return new MenuItemImpl[0];
    }

    @Override
    public double getTotalPrice() {
        return 0;
    }

    @Override
    public Ingredient[] getIngredients() {
        return new Ingredient[0];
    }
}
