import java.util.List;

public interface MenuItem {
    // MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity);
    String getName();

    double getPrice();

    void setPrice(double price);

    int getQuantity();

    double getModPrice();

    String getComment();

    void setComment(String comment);

    List<Ingredient> getIngredients();

    String printIngredients();

    boolean equals(MenuItem item);
}
