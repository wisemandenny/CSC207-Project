public class Event {
    private String type;
    private String text;

    private int tableId;
    private String[] order;

    Event(final String type, final String text) {
        this.type = type;
        this.text = text;
    }

    Event(final String type, final int tableId) {
        this.type = type;
        this.tableId = tableId;
        this.text = "Bill requested for table #" + tableId;
    }

    Event(final String type, final int tableId, final String order) {
        this.type = type;
        this.tableId = tableId;
        this.order = order.split(",\\s");
        this.text = "Table " + tableId + " ordered " + order + ".";
    }

    public String toString() {
        return text;
    }

    String getType() {
        return type;
    }

    String[] getOrder() {
        return order;
    }

    int getTableId() {
        return tableId;
    }
}
