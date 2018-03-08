public interface Ingredient {
    String getName();

    double getPrice();

    boolean sameAs(Ingredient i);
}
