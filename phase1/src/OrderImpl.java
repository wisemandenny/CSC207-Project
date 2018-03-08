import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements Order {
    private final List<MenuItem> orderItems;
    private boolean isReceivedByCook, isReadyForPickup, isDelivered, isReturned; //TODO: we will need to use these somwhere.

    OrderImpl(List<MenuItem> orderItems) {
        this.orderItems = orderItems;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> getItems() {
        return new ArrayList<>(orderItems);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receivedByCook() {
        isReceivedByCook = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void readyForPickup() {
        isReadyForPickup = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void delivered() {
        isDelivered = true;
    }


    //TODO: Delete if unused.
    //@Override
    public double getTotalPrice() {
        double total = 0.00;
        for (MenuItem item : orderItems) {
            total += item.getPrice();
            total += item.getExtraIngredientPrice();
        }
        return total;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returned() {
        isReturned = true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (MenuItem item : orderItems) {
            ingredientList.addAll(item.getIngredients());
        }
        return ingredientList;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (MenuItem item : orderItems) {
            sb.append(i++).append(". ").append(item.getName()).append("\n");
            for (Ingredient mod : item.getExtraIngredients()) {
                sb.append(mod.getName()).append("\n");
            }
        }
        return sb.toString();
    }
}
