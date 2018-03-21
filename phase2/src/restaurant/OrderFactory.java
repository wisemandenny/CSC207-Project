package restaurant;

import menu.MenuItem;

import java.util.List;

public class OrderFactory {
    private OrderFactory() {}

    public static Order makeOrder(List<MenuItem> items, int id, int tableId){
        return new OrderImpl(items, id, tableId);
    }
}
