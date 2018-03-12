package restaurant;

import menu.MenuItem;

public interface Table {
    void addOrder(Order o);

    void addOrderToBill(Order o);

    void removeFromBill(MenuItem item);

    //void removeFromBill(Order o);
    void printBill();

    int getId();

    //Order getOrder();
}

