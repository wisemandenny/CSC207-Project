public class FoodModImpl implements FoodMod {
    private double price;
    private Ingredient ingredient;

    public FoodModImpl(Ingredient ingredient, double price) {
        this.ingredient = ingredient;
        this.price = price;
    }

    @Override
    public String getName() {
        return ingredient.getName();
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public Ingredient getIngredient() {
        return ingredient;
    }

    @Override
    public void addTo(MenuItemImpl item) {
        if (!item.mods.contains(this)
                && item.mods.size() < 5) {
            item.mods.add(this);
            item.increaseModPrice(this);
            //  else Exception?
        }
    }

    @Override
    public void removeFrom(MenuItemImpl item) {
        if (item.mods.contains(this)) {
            item.mods.remove(this);
            item.decreaseModPrice(this);
            // else Exception?
        }
    }
}
