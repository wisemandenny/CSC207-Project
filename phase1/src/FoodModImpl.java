public class FoodModImpl implements FoodMod {
    private double price;
    private Ingredient ingredient;
    private String type;

    FoodModImpl (Ingredient ingredient, String type) {
        this.ingredient = ingredient;
        this.type = type;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) { //TODO: delet this
        this.type = type;
    }

//    @Override
    public boolean equals(final FoodMod mod) {
        return getName().equals(mod.getName());
    }

//    @Override
//    public void addTo(MenuItem item) {
//        if (!item.getMods().contains(this)
//                && !item.getIngredients().contains(this.ingredient)
//                && item.getMods().size() < 5) {
//            item.getMods().add(this);
//            item.increaseModPrice(this);
//        }
//    }
//
//    @Override
//    public void removeFrom(MenuItem item) {
//        if (item.getMods().contains(this)) {
//            item.getMods().remove(this);
//            item.decreaseModPrice(this);
//        }
//    }
}
