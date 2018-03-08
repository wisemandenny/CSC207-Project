import java.util.List;

public interface MenuItem {
    // MenuItem fromRestaurantMenu(MenuItem restaurantItem, int quantity);
    String getName();

    double getPrice();

    void setPrice(double price);

    double getTotal();

    double getExtraIngredientPrice();

    int getQuantity();

    String getComment();

    void setComment(String comment);

    List<Ingredient> getIngredients();

    void addExtraIngredient(Ingredient ingredient);

    List<Ingredient> getExtraIngredients();

    List<Ingredient> getRemovedIngredients();

    double getRemovedIngredientsPrice();

    List<Ingredient> getAllowedExtraIngredients();

    void removeIngredient(Ingredient ingredient);

    boolean equals(MenuItem item);
}
