import java.util.ArrayList;
import java.util.List;

class MenuItemImpl implements MenuItem {
    private String name;
    private double price;
    private List<Ingredient> ingredients;
    private int quantity;
    public List<FoodMod> mods;
    public double modPrice = 0.0;

    public MenuItemImpl(String name, int quantity){
        this.name = name;
        this.quantity = quantity;
        //get the ingredients from the menu
        //get the prices from the menu
    }

    public MenuItemImpl(String name, double price){
        this.name = name;
        this.price = price;
    }

    public MenuItemImpl(String name, double price, List<Ingredient> ingredients){
        this.name = name;
        this.price = price;
        this.ingredients = ingredients;
        this.mods = new ArrayList<>();
    }

    public String getName(){return name;}

    public double getPrice(){return price;}

    public double getModPrice(){return modPrice;}

    public List<FoodMod> getMods(){return mods;}

    public void increaseModPrice(FoodMod mod){this.modPrice += mod.getPrice();}

    public void decreaseModPrice(FoodMod mod){this.modPrice -= mod.getPrice();}

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
}
