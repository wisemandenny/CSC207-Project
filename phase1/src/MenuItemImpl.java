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
        //add extras if they exist and are valid to this.extraIngredients TODO:check if there are less than 5 mods
        if (!item.getRemovedIngredients().isEmpty()) {
            for (Ingredient i : item.getRemovedIngredients()) {
                if (item.getIngredients().contains(i)) {
                    removeIngredient(i);
                }
            }
        }
        //remove subtractions if they exist and they are present in the ingredients
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
    public int getQuantity() {
        return quantity;
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
     * @param price  the new price of this MenuItem
     */
    @Override
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    public double getTotal() {
        return price * quantity;
    }

    /**
     * {@inheritDoc}
     *
     * @return
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
     *
     * @return
     */
    @Override
    public String getComment() {
        return comment;
    }

    /**
     * {@inheritDoc}
     *
     * @param comment  the comment that is to be associated with this MenuItem
     */
    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * {@inheritDoc}
     *
     * @param addOn
     */
    @Override
    public void addExtraIngredient(Ingredient addOn) {
        extraIngredients.add(addOn);
    }

    /**
     * {@inheritDoc}
     *
     * @param ingredient  The ingredient that is to be removed from this MenuItem's list of Ingredients
     */
    @Override
    public void removeIngredient(Ingredient ingredient) {
        removedIngredients.add(ingredient);
    }

    /**
     * {@inheritDoc}
     *
     * @param item  the MenuItem being compared to this MenuItem
     * @return
     */
    @Override
    public boolean equals(MenuItem item) {
        return name.equals(item.getName());
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Ingredient> getExtraIngredients() {
        return new ArrayList<>(extraIngredients);
    }

    /**
     * {@inheritDoc}
     *
     * @return
     */
    @Override
    public List<Ingredient> getRemovedIngredients() {
        return new ArrayList<>(removedIngredients);
    }

    /**
     * {@inheritDoc}
     *
     * @return
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
