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
    void joinCheques();

    double getAutogratuityAmount();

    int getId();

    void addSeat();

    void removeSeat(int seatNumber);

    Table getSeat(int index);

    double getAutoGrat();
}

