package menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BurgerMenu implements Menu {
    private final List<MenuItem> menu = new ArrayList<>();
    private final List<Ingredient> ingredientMenu = new ArrayList<>();

    public BurgerMenu() {
        Ingredient burgerBun = new IngredientImpl("BurgerBun", 0.50);
        Ingredient patty = new IngredientImpl("Patty", 2.00);
        Ingredient chickenPatty = new IngredientImpl("ChickenPatty", 2.50);
        Ingredient veg = new IngredientImpl("VegetarianPatty", 2.00);
        Ingredient lettuce = new IngredientImpl("Lettuce", 0.50);
        Ingredient tomato = new IngredientImpl("Tomato", 0.50);
        Ingredient cucumber = new IngredientImpl("Cucumber", 0.50);
        Ingredient blackOlives = new IngredientImpl("BlackOlives", 1.00);
        Ingredient greenOlives = new IngredientImpl("GreenOlives", 1.00);
        Ingredient potato = new IngredientImpl("Potato", 0.6);
        Ingredient salt = new IngredientImpl("Salt", 0.01);
        Ingredient hotdog = new IngredientImpl("Hotdog", 1.50);
        Ingredient hotdogBun = new IngredientImpl("HotdogBun", 0.50);
        Ingredient chicken = new IngredientImpl("Chicken", 3.00);
        Ingredient breadcrumbs = new IngredientImpl("Breadcrumbs", 0.50);
        Ingredient onion = new IngredientImpl("Onion", 0.50);
        Ingredient cheese = new IngredientImpl("Cheese", 1.00);
        Ingredient gravy = new IngredientImpl("Gravy", 1.00);
        Ingredient ketchup = new IngredientImpl("Ketchup", 0.10);
        Ingredient mustard = new IngredientImpl("Mustard", 0.10);
        Ingredient bacon = new IngredientImpl("Bacon", 3.00);
        Ingredient coke = new IngredientImpl("Coke", 1.25);

        ingredientMenu.addAll(Arrays.asList(burgerBun, patty, chickenPatty, veg, lettuce, tomato, cucumber, blackOlives, greenOlives, potato, salt, hotdog, hotdogBun, chicken, breadcrumbs, onion, cheese, gravy, ketchup, mustard, bacon, coke));

        List<Ingredient> hamburger = Arrays.asList(burgerBun, patty, lettuce, tomato, ketchup, mustard, bacon);
        List<Ingredient> chickenBurger = Arrays.asList(burgerBun, chickenPatty, lettuce, tomato);
        List<Ingredient> vegetarian = Arrays.asList(burgerBun, veg, lettuce, tomato);
        List<Ingredient> fries = Arrays.asList(potato, salt);
        List<Ingredient> hotdogExtras = Arrays.asList(hotdog, hotdogBun);
        List<Ingredient> chickenFingers = Arrays.asList(chicken, breadcrumbs);
        List<Ingredient> onionRings = Arrays.asList(onion, breadcrumbs);
        List<Ingredient> poutine = Arrays.asList(potato, salt, cheese, gravy);
        List<Ingredient> salad = Arrays.asList(lettuce, tomato, onion, cucumber, greenOlives, blackOlives, cheese);

        menu.add(MenuItemFactory.makeMenuItem("Hamburger", 7.99, hamburger));
        menu.add(MenuItemFactory.makeMenuItem("Chicken Burger", 5.39, chickenBurger));
        menu.add(MenuItemFactory.makeMenuItem("Veggie Burger", 5.99, vegetarian));
        menu.add(MenuItemFactory.makeMenuItem("Fries", 1.50, fries));
        menu.add(MenuItemFactory.makeMenuItem("Hotdog", 4.99, hotdogExtras));
        menu.add(MenuItemFactory.makeMenuItem("Coke", 1.25, Arrays.asList(coke)));
        menu.add(MenuItemFactory.makeMenuItem("Chicken Fingers", 7.29, chickenFingers));
        menu.add(MenuItemFactory.makeMenuItem("Onion Rings", 2.99, onionRings));
        menu.add(MenuItemFactory.makeMenuItem("Poutine", 6.99, poutine));
        menu.add(MenuItemFactory.makeMenuItem("Salad", 5.99, salad));

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> getMenu() {
        return new ArrayList<>(menu);
    }

    @Override
    public MenuItem getMenuItem(String query) {
        for (MenuItem searchItem : menu) {
            if (searchItem.getName().equals(query)) {
                return MenuItemFactory.makeMenuItem(searchItem);
            }
        }
        throw new IllegalArgumentException("Please order off the menu.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Ingredient getMenuIngredient(String query) {
        for (Ingredient searchItem : ingredientMenu) {
            if (searchItem.getName().equalsIgnoreCase(query)) {
                return searchItem;
            }
        }
        throw new IllegalArgumentException(query + " is not a valid ingredient.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getAllIngredients() {
        return ingredientMenu;
    }
}
