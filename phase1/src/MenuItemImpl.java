import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private final String name;
    private final int quantity;
    private final List<Ingredient> extraIngredients = new ArrayList<>();
    private final List<Ingredient> removedIngredients = new ArrayList<>();
    private final List<Ingredient> ingredients;
    private double price;
    private String comment;


    MenuItemImpl(MenuItem item, int quantity) {
        this(item.getName(), item.getPrice(), item.getIngredients(), quantity);
        if (!item.getExtraIngredients().isEmpty()) {
            for (Ingredient i : item.getExtraIngredients()) {
                addExtraIngredient(i);
            }
        }
        if (!item.getRemovedIngredients().isEmpty()) {
            for (Ingredient i : item.getRemovedIngredients()) {
                if (item.getIngredients().contains(i)) {
                    removeIngredient(i);
                }
            }
        }
    }

    MenuItemImpl(String name, int quantity) {
        this(name, 0.0, Collections.emptyList(), quantity);
    }

    MenuItemImpl(String name, double price, List<Ingredient> ingredients) {
        this(name, price, ingredients, 1);
    }

    private MenuItemImpl(String name, double price, List<Ingredient> ingredients, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
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
    public boolean equals(MenuItem item) {
        return name.equals(item.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getExtraIngredients() {
        return new ArrayList<>(extraIngredients);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getRemovedIngredients() {
        return new ArrayList<>(removedIngredients);
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
}
