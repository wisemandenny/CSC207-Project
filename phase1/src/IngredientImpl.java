public class IngredientImpl implements Ingredient {
    private String name;
    private int threshold = 10; //TODO: In the assignment handout the only threshold mentioned is 10. Is this a default value?
    private int reorderAmount = 20;

    public IngredientImpl(String name){
        this.name = name;
    }

    public IngredientImpl(String name, int threshold){
        this.name = name;
        this.threshold = threshold;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public int getThreshold() {
        return threshold;
    }

    @Override
    public int getReorderAmount() {
        return reorderAmount;
    }

    @Override
    public void setReorderAmount(int amount) {
        reorderAmount = amount;
    }
}
