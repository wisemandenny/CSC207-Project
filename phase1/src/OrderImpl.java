import java.util.ArrayList;
import java.util.List;

public class OrderImpl implements Order {
    private List<MenuItem> orderItems;

    public OrderImpl(List<MenuItem> orderItems){
        //this.orderItems = orderItems; maybe, or
        this.orderItems = orderItems;
    }

    @Override
    public List<MenuItem> getItems() {
        return orderItems;
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
}
