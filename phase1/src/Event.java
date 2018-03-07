import java.util.ArrayList;
import java.util.List;

class Event {
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

    private void itemBuilder(String strings){
        this.deduction = new ArrayList<>();
        for(String item: strings.split(",\\s")){
            String[] itemSplitString = item.split("\\s", 2);
            int quantity = Integer.parseInt(itemSplitString[0]);
            String itemName = itemSplitString[1];
            // make a menu item and add it to the deduction list
            deduction.add(new MenuItemImpl(itemName, quantity));
        }
    }

    private void commentSetter(String commentList){
        int count = 0;
        // make comments list
        String[] comments = new String[deduction.size()];
        // add every comment to the comment list
        for(String itemComment: commentList.split(",\\s")){
            comments[count] = itemComment;
            count ++;
        }
        // assign each comment to each returned menu item
        count = 0;
        for(MenuItem item: deduction){
            item.setComment(comments[count]);
            count++;
        }
    }

    private void orderConstructorHelper(String strings){
        List<MenuItem> orderItems = new ArrayList<>();
        for (String item : strings.split(",\\s")) { //split the order into [1-9] <item name> substrings
            String[] orderItemSplitString = item.split("\\s", 2);
            int quantity = Integer.parseInt(orderItemSplitString[0]);
            String name = orderItemSplitString[1];
            orderItems.add(new MenuItemImpl(name, quantity));
        }
        order = new OrderImpl(orderItems);
    }

    EventType getType() {
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

    List<MenuItem> getDeductions() { return deduction; }
}
