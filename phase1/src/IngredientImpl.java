public class IngredientImpl implements Ingredient {
    private final String name;
    private double price;

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
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public boolean sameAs(Ingredient i) {
        return name.equalsIgnoreCase(i.getName());
    }
}
