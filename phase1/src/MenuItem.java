import java.util.List;

public interface MenuItem {
    String getName();

    double getPrice();

    void setPrice(double price);

    double getExtraIngredientPrice();

    int getQuantity();

    String getComment();

    void setComment(String comment);

    List<Ingredient> getIngredients();

    void addExtraIngredient(Ingredient ingredient);

    List<Ingredient> getExtraIngredients();

    List<Ingredient> getRemovedIngredients();

    double getRemovedIngredientsPrice();

    void removeIngredient(Ingredient ingredient);

    boolean equals(MenuItem item);
}
