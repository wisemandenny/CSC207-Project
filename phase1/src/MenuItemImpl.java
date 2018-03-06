import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private String name;
    private double price;
    private List<Ingredient> ingredients;
    private int quantity = 1;
    private List<FoodMod> mods;
    private double modPrice = 0.00;

    public MenuItemImpl(String name, int quantity){

        this.name = name;
        this.quantity = quantity;
        //TODO: get the ingredients from the menu
        //TODO: get the prices from the menu
    }

    public MenuItemImpl(String name, double price, List<Ingredient> ingredients) {
        this(name, price, ingredients, 1);
    }

    public MenuItemImpl(MenuItem item, int quantity) {
        this(item.getName(), item.getPrice(), item.getIngredients(), quantity);
    }

    public MenuItemImpl(String name, double price){
        this(name, price, Collections.emptyList(), 1);
    }

    public MenuItemImpl(String name, double price, List<Ingredient> ingredients, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.ingredients = ingredients;
        this.mods = new ArrayList<>();
    }

    public String getName(){return name;}

    public int getQuantity() {
        return quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getModPrice() {
        return modPrice;
    }

    public List<FoodMod> getMods() {
        return mods;
    }

    public void increaseModPrice(FoodMod mod) {
        this.modPrice += mod.getPrice();
    }

    public void decreaseModPrice(FoodMod mod) {
        this.modPrice -= mod.getPrice();
    }

    @Override
    public List<Ingredient> getIngredients() {
        return ingredients;
    }
    @Override
    public String printIngredients(){
        // print the ingredients of this MenuItem
        StringBuilder result = new StringBuilder("");
        for (Ingredient ingredient : ingredients){
            result.append("| " + ingredient.getName() + " ");
        }
        return result.toString();
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(MenuItem item){
        return name.equals(item.getName());
    }
}
