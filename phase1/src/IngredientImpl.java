public class IngredientImpl implements Ingredient {
    private final String name;
    private final double price;

    IngredientImpl(String name) {
        this(name, 0.0);
    }

    IngredientImpl(String name, double price) {
        this.name = name;
        this.price = price;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public boolean equalTo(Ingredient ing) {
        return name.equalsIgnoreCase(ing.getName());
    }
}
