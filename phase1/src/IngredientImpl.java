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

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     *
     * @param price  the new price of this Ingredient
     */
    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * {@inheritDoc}
     * @param i  the Ingredient in question
     * @return
     */
    @Override
    public boolean sameAs(Ingredient i) {
        return name.equalsIgnoreCase(i.getName());
    }
}
