import java.util.List;
import java.util.Arrays;
public class Menu {
    private MenuItem[] menu = new MenuItemImpl[5];

    Menu() {

        Ingredient burgerBun = new IngredientImpl("Burger Bun");
        Ingredient patty = new IngredientImpl("Patty");
        Ingredient chickenPatty = new IngredientImpl("Chicken Patty");
        Ingredient veg = new IngredientImpl("Vegetarian Patty");
        Ingredient lettuce = new IngredientImpl("Lettuce");
        Ingredient tomato = new IngredientImpl("Tomato");
        Ingredient potato = new IngredientImpl("Potato");
        Ingredient salt = new IngredientImpl("Salt");
        Ingredient hotdog = new IngredientImpl("Hotdog");
        Ingredient hotdogBun = new IngredientImpl("Hotdog Bun");

        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato);
        List<Ingredient> chickenBurger = Arrays.asList(burgerBun, chickenPatty, lettuce, tomato);
        List<Ingredient> vegetarian = Arrays.asList(burgerBun, veg, lettuce, tomato);
        List<Ingredient> fries = Arrays.asList(potato, salt);
        List<Ingredient> hot = Arrays.asList(hotdog, hotdogBun);

        menu[0] = new MenuItemImpl("Hamburger", 5, hamburger);
        menu[1] = new MenuItemImpl("Chicken Burger", 4.5, chickenBurger);
        menu[2] = new MenuItemImpl("Veggie Burger", 4, vegetarian);
        menu[3] = new MenuItemImpl("Fries", 1.50, fries);
        menu[4] = new MenuItemImpl("Hotdog", 3, hot);

        // Addon items
        Ingredient ketchup = new IngredientImpl("Ketchup");
        Ingredient mustard = new IngredientImpl("Mustard");
        Ingredient sentience = new IngredientImpl("Sentience");
        Ingredient cheese = new IngredientImpl("Cheese");
        Ingredient bacon = new IngredientImpl("Bacon");
        FoodMod addTomato = new FoodModImpl(tomato, 0.25);
        FoodMod addLettuce = new FoodModImpl(lettuce, 0.25);
        FoodMod addSalt = new FoodModImpl(salt, 0.00);
        FoodMod addketchup = new FoodModImpl(ketchup, 0.00);
        FoodMod addMustard = new FoodModImpl(mustard, 0.00);
        // Should only be added to items ordered by rude customers in need of a good scare.
        FoodMod addSentience = new FoodModImpl(sentience, 6.66);
        FoodMod addCheese = new FoodModImpl(cheese, 1.00);
        FoodMod addBacon = new FoodModImpl(bacon, 2.00);
    }

    public void printMenu(){
        for(MenuItem item : menu){
            System.out.println(item.getName() + "\nIngredients:" + item.printIngredients() +"\n");
        }
    }


}
