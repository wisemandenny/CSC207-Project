public class FoodModImpl implements FoodMod {
    private double price;
    private Ingredient ingredient;

    FoodModImpl (Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    FoodModImpl(Ingredient ingredient, double price) {
        this.ingredient = ingredient;
        this.price = price;
    }

    @Override
    public String getName(){return ingredient.getName();}

    @Override
    public double getPrice(){return price;}

    @Override
    public Ingredient getIngredient(){return ingredient;}

    @Override
    public void addTo(MenuItem item) {
        if (!item.getMods().contains(this)
                && !item.getIngredients().contains(this.ingredient)
                && item.getMods().size() < 5) {
            item.getMods().add(this);
            item.increaseModPrice(this);
        }
        throw new IllegalArgumentException("Can't add that modifier to this item.");
    }

    @Override
    public void removeFrom(MenuItem item) {
        if (item.getMods().contains(this)) {
            item.getMods().remove(this);
            item.decreaseModPrice(this);
        }
        throw new IllegalArgumentException("Can't remove that modifier to this item.");
    }
}
