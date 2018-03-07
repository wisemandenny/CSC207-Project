import java.util.ArrayList;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private final String name;
    private double price;
    private int quantity;
    private List<Ingredient> ingredients;
    private double modPrice = 0.0;
    private String comment;
    private List<FoodMod> allowedMods;
    private List<FoodMod> orderedMods = new ArrayList<>();
    private List<FoodMod> appliedMods = new ArrayList<>();

    MenuItemImpl(final String name, final int quantity) {
        this.name = name;
        this.quantity = quantity;
        //TODO: get the ingredients from the menu
        //TODO: get the prices from the menu
        //TODO: get allowedMods from the menu
    }

    MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
    }

    MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients, List<FoodMod> mods) {
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.allowedMods = mods;
    }

    private MenuItemImpl(final String name, final double price, final List<Ingredient> ingredients, final int quantity, List<FoodMod> mods) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
        this.allowedMods = mods;
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
        return appliedMods;
    }

    public  List<FoodMod> getOrderedMods() {
        return orderedMods;
    }

    public void setOrderedMods(List<FoodMod> mods) {
        this.orderedMods = mods;
    }

    public void orderMod(FoodMod mod) {
        orderedMods.add(mod);
    }

    public void applyMods() {
        for (FoodMod modToAdd : orderedMods) {
            if (modToAdd.getType().equals("add")) {
                if (allowedMods.contains(modToAdd) && appliedMods.size() < 5) {
                    appliedMods.add(modToAdd);
                    increaseModPrice(modToAdd);
                }
            } else if (modToAdd.getType().equals("remove")) {
                if (ingredients.contains(modToAdd.getIngredient()) && appliedMods.size() < 5) {
                    ingredients.remove(modToAdd.getIngredient());
                    appliedMods.add(modToAdd);
                    decreaseModPrice(modToAdd);
                }
            }
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

    public List<FoodMod> getAllowedMods() {
        return allowedMods;
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
        return new MenuItemImpl(restaurantItem.getName(), restaurantItem.getPrice(), restaurantItem.getIngredients(), quantity, restaurantItem.getAllowedMods());
    }

    void setIngredients(final List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }
}
