import java.util.Collections;
import java.util.List;
import java.util.Arrays;
public class BurgerMenu implements Menu {
    private MenuItem[] menu = new MenuItemImpl[10];

    BurgerMenu() {
        Ingredient burgerBun = new IngredientImpl("Burger Bun");
        Ingredient patty = new IngredientImpl("Patty");
        Ingredient chickenPatty = new IngredientImpl("Chicken Patty");
        Ingredient veg = new IngredientImpl("Vegetarian Patty");
        Ingredient lettuce = new IngredientImpl("Lettuce");
        Ingredient tomato = new IngredientImpl("Tomato");
        Ingredient cucumber = new IngredientImpl("cucumber");
        Ingredient blackOlives = new IngredientImpl("black olives");
        Ingredient greenOlives = new IngredientImpl("green olives");
        Ingredient potato = new IngredientImpl("Potato");
        Ingredient salt = new IngredientImpl("Salt");
        Ingredient hotdog = new IngredientImpl("Hotdog");
        Ingredient hotdogBun = new IngredientImpl("Hotdog Bun");
        Ingredient chicken = new IngredientImpl("Chicken");
        Ingredient breadcrumbs = new IngredientImpl("Breadcrumbs");
        Ingredient onion = new IngredientImpl("onion");
        Ingredient cheeseCurds = new IngredientImpl("cheese");
        Ingredient gravy = new IngredientImpl("gravy");

        List<Ingredient> noIngredients = Collections.emptyList();
        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato);
        List<Ingredient> chickenBurger = Arrays.asList(burgerBun, chickenPatty, lettuce, tomato);
        List<Ingredient> vegetarian = Arrays.asList(burgerBun, veg, lettuce, tomato);
        List<Ingredient> fries = Arrays.asList(potato, salt);
        List<Ingredient> hot = Arrays.asList(hotdog, hotdogBun);
        List<Ingredient> chickenFingers = Arrays.asList(chicken, breadcrumbs);
        List<Ingredient> onionRings = Arrays.asList(onion, breadcrumbs);
        List<Ingredient> poutine = Arrays.asList(potato, salt, cheeseCurds, gravy);
        List<Ingredient> salad = Arrays.asList(lettuce, tomato, onion, cucumber, greenOlives, blackOlives, cheeseCurds );

        menu[0] = new MenuItemImpl("Hamburger", 7.99, hamburger);
        menu[1] = new MenuItemImpl("Chicken Burger", 5.39, chickenBurger);
        menu[2] = new MenuItemImpl("Veggie Burger", 5.99, vegetarian);
        menu[3] = new MenuItemImpl("Fries", 1.50, fries);
        menu[4] = new MenuItemImpl("Hotdog", 4.99, hot);
        menu[5] = new MenuItemImpl("Coke", 1.25, noIngredients);
        menu[6] = new MenuItemImpl("Chicken Fingers", 7.29, chickenFingers);
        menu[7] = new MenuItemImpl("Onion Rings", 2.99, onionRings);
        menu[8] = new MenuItemImpl("Poutine", 6.99, poutine);
        menu[9] = new MenuItemImpl("Salad", 5.99, salad);

        // Addon items
        Ingredient ketchup = new IngredientImpl("Ketchup");
        Ingredient mustard = new IngredientImpl("Mustard");
        Ingredient cheese = new IngredientImpl("Cheese");
        Ingredient bacon = new IngredientImpl("Bacon");

        FoodMod addTomato = new FoodModImpl(tomato, 0.25);
        FoodMod addLettuce = new FoodModImpl(lettuce, 0.25);
        FoodMod addSalt = new FoodModImpl(salt, 0.00);
        FoodMod addKetchup = new FoodModImpl(ketchup, 0.00);
        FoodMod addMustard = new FoodModImpl(mustard, 0.00);
        FoodMod addCheese = new FoodModImpl(cheese, 1.00);
        FoodMod addBacon = new FoodModImpl(bacon, 2.00);
    }

    public MenuItem[] getMenu(){
        return menu;
    }

    public MenuItem getMenuItem(MenuItem query) {
        for(MenuItem searchItem : menu){
            if (searchItem.equals(query)) return searchItem;
        }
        throw new IllegalArgumentException("Please order off of the menu.");
    }

    private void printMenu(){ //TODO: delete this method if it's not used anywhere.
        for(MenuItem item : menu){
            System.out.println(item.getName() + "\nIngredients:" + item.printIngredients() +"\n");
        }
    }
}
