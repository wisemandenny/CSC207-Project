package restaurant;

public interface Bill {
    void add(Order o);

    void remove(Order o);

    void comp(Order o);

    String getBillString();

    double getTotal();

    double getSubtotal();

    //We will get rid of this during phase 2.
    void printBill();

}
