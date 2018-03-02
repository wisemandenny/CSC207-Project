import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class OrderImpl extends Observable implements Order {
    private List<MenuItem> orderItems;
    private boolean isReceivedByCook, isReadyForPickup, isDelivered;

    public OrderImpl(List<MenuItem> orderItems){
        //this.orderItems = orderItems; maybe, or
        this.orderItems = orderItems;
    }

    @Override
    public List<MenuItem> getItems() {
        return orderItems;
    }

    @Override
    public boolean isReceivedByCook() {
        return isReceivedByCook;
    }

    @Override
    public void receivedByCook() {
        isReceivedByCook = true;
    }

    @Override
    public boolean isReadyForPickup() {
        return isReadyForPickup;
    }
    @Override
    public void readyForPickup() {
        isReadyForPickup = true;
    }

    @Override
    public boolean isDelivered() {
        return isDelivered;
    }

    @Override
    public void delivered() {
        isDelivered = true;
    }

    @Override
    public double getTotalPrice() {
        //return orderItems.stream().mapToDouble(MenuItem::getPrice).sum(); too confusing but kind of cool
        double total = 0.0;
        for (MenuItem item : orderItems){
            total += item.getPrice();
            total += item.getModPrice();
        }
        return total;
    }

    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientList= new ArrayList<>();
        for (MenuItem item : orderItems){
            for (Ingredient ingredient : item.getIngredients()){
                ingredientList.add(ingredient);
            }
        }
        return ingredientList;
    }

    @Override
    public String toString(){
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for(MenuItem item: orderItems){
            sb.append(i++ + ". " + item.getName() +"\n");
        }
        return sb.toString();
    }
}
