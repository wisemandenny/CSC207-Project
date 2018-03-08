import java.util.*;

public class BurgerMenu implements Menu {
    private final MenuItem[] menu = new MenuItemImpl[10];

    BurgerMenu() {
        Ingredient burgerBun = new IngredientImpl("Burger Bun", 0.50);
        Ingredient patty = new IngredientImpl("Patty", 2.00);
        Ingredient chickenPatty = new IngredientImpl("Chicken Patty", 2.50);
        Ingredient veg = new IngredientImpl("Vegetarian Patty", 2.00);
        Ingredient lettuce = new IngredientImpl("Lettuce", 0.50);
        Ingredient tomato = new IngredientImpl("Tomato", 0.50);
        Ingredient cucumber = new IngredientImpl("Cucumber", 0.50);
        Ingredient blackOlives = new IngredientImpl("Black olives", 1.00);
        Ingredient greenOlives = new IngredientImpl("Green olives", 1.00);
        Ingredient potato = new IngredientImpl("Potato");
        Ingredient salt = new IngredientImpl("Salt");
        Ingredient hotdog = new IngredientImpl("Hotdog", 1.50);
        Ingredient hotdogBun = new IngredientImpl("Hotdog Bun", 0.50);
        Ingredient chicken = new IngredientImpl("Chicken", 3.00);
        Ingredient breadcrumbs = new IngredientImpl("Breadcrumbs", 0.50);
        Ingredient onion = new IngredientImpl("Onion", 0.50);
        Ingredient cheese = new IngredientImpl("Cheese", 1.00);
        Ingredient gravy = new IngredientImpl("Gravy", 1.00);
        Ingredient ketchup = new IngredientImpl("Ketchup", 0.10);
        Ingredient mustard = new IngredientImpl("Mustard", 0.10);
        Ingredient bacon = new IngredientImpl("Bacon", 3.00);
        Ingredient coke = new IngredientImpl("Coke", 1.25);


        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        List<Ingredient> chickenBurger = Arrays.asList(burgerBun, chickenPatty, lettuce, tomato);
        List<Ingredient> vegetarian = Arrays.asList(burgerBun, veg, lettuce, tomato);
        List<Ingredient> fries = Arrays.asList(potato, salt);
        List<Ingredient> hot = Arrays.asList(hotdog, hotdogBun);
        List<Ingredient> chickenFingers = Arrays.asList(chicken, breadcrumbs);
        List<Ingredient> onionRings = Arrays.asList(onion, breadcrumbs);
        List<Ingredient> poutine = Arrays.asList(potato, salt, cheese, gravy);
        List<Ingredient> salad = Arrays.asList(lettuce, tomato, onion, cucumber, greenOlives, blackOlives, cheese);

		menu[0] = new MenuItemImpl("Hamburger", 7.99, hamburger);
		menu[1] = new MenuItemImpl("Chicken Burger", 5.39, chickenBurger);
		menu[2] = new MenuItemImpl("Veggie Burger", 5.99, vegetarian);
		menu[3] = new MenuItemImpl("Fries", 1.50, fries);
		menu[4] = new MenuItemImpl("Hotdog", 4.99, hot);
		menu[5] = new MenuItemImpl("Coke", 1.25, Arrays.asList(coke));
		menu[6] = new MenuItemImpl("Chicken Fingers", 7.29, chickenFingers);
		menu[7] = new MenuItemImpl("Onion Rings", 2.99, onionRings);
		menu[8] = new MenuItemImpl("Poutine", 6.99, poutine);
		menu[9] = new MenuItemImpl("Salad", 5.99, salad);

    }

    @Override
    public MenuItem[] getMenu() {
        return menu.clone();
    }

    @Override
    public MenuItem getMenuItem(MenuItem query) {
        for (MenuItem searchItem : menu) {
            if (searchItem.equals(query)) {
                return searchItem;
            }
        }
        throw new IllegalArgumentException("Please order off of the menu.");
    }
}
