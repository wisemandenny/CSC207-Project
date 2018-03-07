import java.util.ArrayList;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private final String name;
    private double price;
    private int quantity;
    private List<Ingredient> ingredients;
    private List<FoodMod> mods;
    private double modPrice = 0.0;
    private String comment;

    MenuItemImpl(final String name, final int quantity) {
        this.name = name;
        this.quantity = quantity;
        //TODO: get the ingredients from the menu
        //TODO: get the prices from the menu
    }

    MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients) {
        this(name, price, ingredients, 1);
    }

    private MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients, final int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
        mods = new ArrayList<>();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getQuantity() {
        return quantity;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public double getModPrice() {
        return modPrice;
    }

    @Override
    public List<FoodMod> getMods() {
        return mods;
    }

    @Override
    public void addMod(FoodMod mod) {
        if(!mods.contains(mod) && !ingredients.contains(mod) && mods.size() < 5){
            mods.add(mod);
            increaseModPrice(mod);
        }
    }

    @Override
    public void removeMod(FoodMod mod){
        if(mods.contains(mod)){
            mods.remove(mod);
            decreaseModPrice(mod);
        }
    }

    @Override
    public void increaseModPrice(final FoodMod mod) {
        modPrice += mod.getPrice();
    }

    @Override
    public void decreaseModPrice(final FoodMod mod) {
        modPrice -= mod.getPrice();
    }

    @Override
    public void setComment(String comment) { this.comment = comment; }

    @Override
    public String getComment() { return comment; }

    @Override
    public void setPrice(double price) { this.price = price; }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    @Override
    public String printIngredients() {
        final StringBuilder result = new StringBuilder("");
        for (final Ingredient ingredient : ingredients) { result.append("| ").append(ingredient.getName()).append(" "); }
        return result.toString();
    }

    @Override
    public boolean equals(final MenuItem item) {
        return name.equals(item.getName());
    }

    static MenuItem fromRestaurantMenu(final MenuItem restaurantItem, final int quantity) {
        return new MenuItemImpl(restaurantItem.getName(), restaurantItem.getPrice(), restaurantItem.getIngredients(), quantity);
    }

    void setIngredients(final List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
