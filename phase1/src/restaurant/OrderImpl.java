package restaurant;

import menu.Ingredient;
import menu.MenuItem;

import java.util.ArrayList;
import java.util.List;


public class OrderImpl implements Order {
    private static int idCounter = 1;

    private final List<MenuItem> orderItems;
    private int tableId;
    private int id;

    OrderImpl() {
        orderItems = new ArrayList<>();
    }

    public OrderImpl(List<MenuItem> orderItems) {
        this.orderItems = orderItems;
        id = OrderImpl.idCounter++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> getItems() {
        return new ArrayList<>(orderItems);
    }

    @Override
    public void remove(MenuItem item) {
        orderItems.remove(item);
    }

    @Override
    public void add(Order o) {
        orderItems.addAll(o.getItems());
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public int getTableId() {
        return tableId;
    }

    @Override
    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returned() {
        //do something
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
