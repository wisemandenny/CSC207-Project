package menu;

import java.util.Objects;

public class IngredientImpl implements Ingredient {
    private final String name;
    private double price;

    IngredientImpl(Ingredient i) {
        this(i.getName(), i.getPrice());
    }

    IngredientImpl(String name, double price) {
        this.name = name;
        this.price = price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPrice() {
        return price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof Ingredient)) {
            return false;
        }
        Ingredient i = (Ingredient) o;
        return name.equalsIgnoreCase(i.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
