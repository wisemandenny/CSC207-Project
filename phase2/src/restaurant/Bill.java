package restaurant;

public interface Bill {
    void add(Order o);

    void remove(Order o);

    void comp(Order o);

    void joinBill(Bill toJoin);

    Order getOrder();

    double getTaxAmount();
    double getSubtotal();
    double getTotal();


    double getPaidAmount();

    double getTipAmount();

    void tip(double amount);

    void pay(double amount);
}
