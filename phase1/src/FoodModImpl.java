public class FoodModImpl implements FoodMod {
    private double price;
    private Ingredient ingredient;

    FoodModImpl(Ingredient ingredient, double price) {
        this.ingredient = ingredient;
        this.price = price;
    }

    @Override
    public String getName(){return ingredient.getName();}

    @Override
    public double getPrice(){return price;}

    @Override
    public Ingredient getIngredient(){return ingredient;}
}
