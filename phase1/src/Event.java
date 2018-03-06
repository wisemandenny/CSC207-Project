import java.util.ArrayList;
import java.util.List;

public class Event {
    private String type;
    private int tableId;
    private Order order;
    private String addOn;

    Event(final String type, final int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    Event(final String type, final int tableId, final String order) {
        this.type = type;
        this.tableId = tableId;
        if (type.equals("order")) {
            this.order = orderConstructorHelper(order);
        } else {
            if (type.equals("addon")) {
                this.addOn = order;
            }
        }
    }

    private Order orderConstructorHelper(String strings){
        List<MenuItem> orderItems = new ArrayList<>();
        for(String item: strings.split(",\\s")){ //split the order into [1-9] <item name> substrings
            String[] orderItemSplitString = item.split("\\s", 2);
            orderItems.add(new MenuItemImpl(orderItemSplitString[1], Integer.parseInt(orderItemSplitString[0])));
        }
        return new OrderImpl(orderItems);
    }

    String getType() {
        return type;
    }

    String getAddOn() {
        return addOn;
    }

    Order getOrder() {
        return order;
    }

    int getTableId() {
        return tableId;
    }
}
