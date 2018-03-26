package restaurant;

import menu.Ingredient;
import menu.MenuItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class OrderImpl implements Order {

    private List<MenuItem> orderItems;
    private int tableId;
    private int id;
    private int seatId;

    OrderImpl() {
        orderItems = new ArrayList<>();
    }

    OrderImpl(List<MenuItem> items, int id, int tableId, int seatId) {
        orderItems = new ArrayList<>(items);
        this.id = id;
        this.tableId = tableId;
        this.seatId = seatId;

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<MenuItem> getItems() {
        return new ArrayList<>(orderItems);
    }

    @Override
    public void add(Order o) {
        orderItems.addAll(o.getItems());
    }

    @Override
    public void remove(Order o) {
        List<Integer> delete = new ArrayList<>();
        List<Integer> amount = new ArrayList<>();
        for (MenuItem item : o.getItems()) {
            int count = 0;
            for (MenuItem orderItem : orderItems) {
                if (item.equalsWithExtras(orderItem)) {
                    if (item.getQuantity() == orderItem.getQuantity()) {
                        delete.add(count);
                        amount.add(0);
                    } else if(item.getQuantity() > orderItem.getQuantity()){
                        //TODO: change this exception into a logger event
                        throw new IllegalArgumentException("You cannot remove this many! Tried to remove " + item.getQuantity() + " when there were only " + orderItem.getQuantity() + "." );
                    } else {
                        amount.add(orderItem.getQuantity() - item.getQuantity());
                    }
                } else {
                    amount.add(orderItem.getQuantity() - item.getQuantity());
                }
                count ++;
            }
        }
        // First do quantity decreases
        for (int i = 0; i < orderItems.size(); i++){
            orderItems.get(i).setQuantity(amount.get(i));
        }

        // Next do total item removals
        Collections.sort(delete, Collections.reverseOrder());
        for (int j : delete){
            orderItems.remove(j);
        }
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

    @Override
    public void setSeatId(int seatId) { this.seatId = seatId; }

    @Override
    public int getSeatId(){
        return seatId;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void returned(MenuItem returnedItem) {
        //go through the "order" (bill) and set matching items price to 0, and set their comment to item.comment
        for (MenuItem item : orderItems) {
            if (item.equalsWithExtras(returnedItem)) {
                item.setPrice(0.0);
                item.setComment(returnedItem.getComment());
                for (Ingredient i : item.getExtraIngredients()) {
                    i.setPrice(0.0);
                }
                for (Ingredient i : item.getRemovedIngredients()) {
                    i.setPrice(0.0);
                }
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Ingredient> getIngredients() {
        List<Ingredient> ingredientList = new ArrayList<>();
        for (MenuItem item : orderItems) {
            for (int i = 0; i < item.getQuantity(); i++) { //quantity multiplier
                ingredientList.addAll(item.getIngredients());
                ingredientList.addAll(item.getExtraIngredients());
                ingredientList.removeAll(item.getRemovedIngredients());
            }
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
