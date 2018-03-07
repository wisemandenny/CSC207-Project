import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Event {
    private static final int QUANTITY_ADDRESS = 0;
    private static final int NAME_ADDRESS = 1;
    private final EventType type;
    private final int tableId;
    private Order order;
    private List<MenuItem> deduction;
    private String addOn;

    Event(EventType type, int tableId) {
        this.type = type;
        this.tableId = tableId;
    }

    Event(EventType type, int tableId, String order) {
        this.type = type;
        this.tableId = tableId;
        if (type.equals(EventType.ORDER)) {
            orderConstructorHelper(order);
        } else {
            if (type.equals(EventType.ADDON)) {
                addOn = order;
            }
        }
    }

    Event(EventType type, int tableId, String itemList, String commentList){
        this.type = type;
        this.tableId = tableId;
        itemBuilder(itemList);
        commentSetter(commentList);
    }


    private void commentSetter(String commentList){
        int count = 0;
        // make comments list
        String[] comments = new String[deduction.size()];
        // add every comment to the comment list
        for(String itemComment: commentList.split(",\\s")){
            comments[count++] = itemComment;
        }
        // assign each comment to each returned menu item
        count = 0;
        for(MenuItem item: deduction){
            item.setComment(comments[count++]);
        }
    }

    private void itemBuilder(String strings){
        this.deduction = new ArrayList<>();
        if(strings.length() < 2) throw new IllegalArgumentException("Invalid strings length in Event.itemBuilder");
        for(String item: strings.split(",\\s")){
           addItemToList(item, deduction);
        }
    }

    private void orderConstructorHelper(String strings){
        List<MenuItem> orderItems = new ArrayList<>();
        for (String item : strings.split(",\\s")) { //split the order into [1-9] <item name> substrings
            addItemToList(item, orderItems);
        }
        order = new OrderImpl(orderItems);
    }

    private void addItemToList(String item, List<MenuItem> list){
        String[] itemSplitString = item.split("\\s", 2);
        int quantity = Integer.parseInt(itemSplitString[QUANTITY_ADDRESS]);
        String itemName = itemSplitString[NAME_ADDRESS];
        // make a menu item and add it to the deduction list
        list.add(new MenuItemImpl(itemName, quantity));
    }

    EventType getType() {
        return EventType.fromString(type.toString());
    }

    String getAddOn() {
        return addOn;
    }

    Order getOrder() {
        return new OrderImpl(order.getItems());
    }

    int getTableId() {
        return tableId;
    }

    List<MenuItem> getDeductions() { return new ArrayList<>(deduction); }
}
