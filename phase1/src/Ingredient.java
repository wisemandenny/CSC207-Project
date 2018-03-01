public interface Ingredient {
    String getName();
    int getThreshold();
    int getReorderAmount();
    void setReorderAmount(int amount);
}
