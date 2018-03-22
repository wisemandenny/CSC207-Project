package restaurant;

import java.util.List;

public interface Table {
    void addOrder(Order o);

    List<Order> getOrders();

    void addOrderToBill(Order o);

    void removeFromBill(Order o);

    void comp(Order o);

    void printBill();

    Bill getBill();

    int getId();
}

