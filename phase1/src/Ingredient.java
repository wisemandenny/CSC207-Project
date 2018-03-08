public interface Ingredient {
    String getName();

    double getPrice();
    boolean equalTo(Ingredient ing);
}
