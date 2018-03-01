class MenuItemImpl implements MenuItem {
    private String name;
    private double price;

    public MenuItemImpl(String name, double price){
        this.name = name;
        this.price = price;
    }

    public String getName(){return this.name;}

    public double getPrice(){return this.price;}

    @Override
    public Ingredient[] getIngredients() {
        return new Ingredient[0];
    }

}
