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

        List<Ingredient> burger = Arrays.asList(burgerBun, patty, lettuce, tomato);
        List<Ingredient> chickenBurger = Arrays.asList(burgerBun, chickenPatty, lettuce, tomato);
        List<Ingredient> vegetarian = Arrays.asList(burgerBun, veg, lettuce, tomato);
        List<Ingredient> fries = Arrays.asList(potato, salt);
        List<Ingredient> hot = Arrays.asList(hotdog, hotdogBun);

        menu[0] = new MenuItemImpl("Hamburger", 5, burger);
        menu[1] = new MenuItemImpl("Chicken Burger", 4.5, chickenBurger);
        menu[2] = new MenuItemImpl("Veggie Burger", 4, vegetarian);
        menu[3] = new MenuItemImpl("Fries", 1.50, fries);
        menu[4] = new MenuItemImpl("Hotdog", 3, hot);

    }
    public void printMenu(){
        for(MenuItem item : menu){
            System.out.println(item.getName() + "\nIngredients:" + item.printIngredients() +"\n");
        }
    }


}
