package restaurant;

import java.util.List;

public interface Table {
    void addOrder(Order o);

    List<Order> getOrders();
    List<List<Order>> getAllOrders();

    void addOrderToBill(Order o);

    void removeFromBill(Order o);

    void comp(Order o);

    Bill getBill();

    int getId();

    void addSeats(int amount);

    void removeSeats(int amount);

    Table getSeat(int index);
}

