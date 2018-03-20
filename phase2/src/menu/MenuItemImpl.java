package menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class MenuItemImpl implements MenuItem {
    private final String name;
    private final List<Ingredient> ingredients;
    private final List<Ingredient> extraIngredients;
    private final List<Ingredient> removedIngredients;
    private int quantity;
    private double price;
    private String comment;

    MenuItemImpl(String name, double price, List<Ingredient> ingredients, List<Ingredient> extraIngredients, List<Ingredient> removedIngredients, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
        this.extraIngredients = extraIngredients;
        this.removedIngredients = removedIngredients;
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
    public int getQuantity() {
        return quantity;
    }

    @Override
    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
    public double getTotal() {
        return price * quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getExtraIngredientPrice() {
        double ret = 0.0;
        for (Ingredient i : extraIngredients) {
            ret += i.getPrice();
        }
        return ret * quantity;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addExtraIngredient(Ingredient addOn) {
        extraIngredients.add(addOn);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeIngredient(Ingredient ingredient) {
        removedIngredients.add(ingredient);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getExtraIngredients() {
        return extraIngredients;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getRemovedIngredients() {
        return removedIngredients;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getRemovedIngredientsPrice() {
        double ret = 0.0;
        for (Ingredient i : removedIngredients) {
            ret += i.getPrice();
        }
        return ret * quantity;
    }

    @Override
    public List<Ingredient> getAllIngredients() {
        List<Ingredient> allIngredients = new ArrayList<>();
        allIngredients.addAll(getIngredients());
        allIngredients.addAll(getExtraIngredients());
        allIngredients.removeAll(getRemovedIngredients());
        return allIngredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (!(o instanceof MenuItem)) {
            return false;
        }
        MenuItem m = (MenuItem) o;
        return name.equalsIgnoreCase(m.getName());
    }

    // TODO: change the logic here.
    @Override
    public boolean equalsWithExtras(MenuItem item) {
        return item.hashCode() == hashCode();
    }

    @Override
    public int hashCode() {
        return Objects.hash(concatenate());
    }

    private String concatenate() {
        StringBuilder mods = new StringBuilder("");
        for (Ingredient i : extraIngredients) {
            mods.append(i.getName());
        }
        for (Ingredient i : removedIngredients) {
            mods.append(i.getName());
        }
        return name + mods.toString();
    }
}
