import java.util.List;

class MenuItemImpl implements MenuItem {
    private final String name;
    private final int quantity;
    private final double modPrice = 0.0;
    private double price;
    private List<Ingredient> ingredients;
    private String comment;

    MenuItemImpl(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
        //TODO: get the ingredients from the menu
        //TODO: get the prices from the menu
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

    static MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity) {
        return new MenuItemImpl(restaurantItem.getName(), restaurantItem.getPrice(), restaurantItem.getIngredients(), quantity);
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
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public double getModPrice() {
        return modPrice;
    }

    @Override
    public String getComment() {
        return comment;
    }

    @Override
    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public String printIngredients() {
        StringBuilder result = new StringBuilder("");
        for (Ingredient ingredient : ingredients) {
            result.append("| ").append(ingredient.getName()).append(" ");
        }
        return result.toString();
    }

    @Override
    public boolean equals(MenuItem item) {
        return name.equals(item.getName());
    }
}
