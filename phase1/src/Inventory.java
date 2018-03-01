public interface Inventory {
    void addToInventory(Ingredient i, int amount);
    void removeFromInventory(Ingredient i, int amount);
    Ingredient[] getContents();
}
