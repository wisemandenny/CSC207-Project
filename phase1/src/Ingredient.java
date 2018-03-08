public interface Ingredient {
    String getName();

    double getPrice();

    void setPrice(double price);

    boolean sameAs(Ingredient i);
}
