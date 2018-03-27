package restaurant;

public interface Bill {

    /**
     * Add Order o to the bill.
     *
     * @param o the Order to be added.
     */
    void add(Order o);

    /**
     * Remove Order o from this bill.
     *
     * @param o the order to be removed.
     */
    void remove(Order o);

    /**
     * Give away Order o for free.
     *
     * @param o the Order to be given away for free.
     */
    void comp(Order o);

    /**
     * Join the tables bill.
     *
     * @param toJoin the bill that is to be joined.
     */
    void joinBill(Bill toJoin);

    /**
     * Return this bill.
     *
     * @return the Order that is this bill.
     */
    Order getOrder();

    /**
     * Return the tax amount of this bill
     *
     * @return a double that represents the tax amount of this bill.
     */
    double getTaxAmount();

    /**
     * Return the bill without tax added.
     *
     * @return a double that represents this bill without tax.
     */
    double getSubtotal();

    /**
     * Return the bill with the tax amount added to the subtotal
     *
     * @return a double that represents the addition of this bill's subtotal and tax amount.
     */
    double getTotal();

    /**
     * Return the amount the guest has paid so far.
     *
     * @return a double representing the amount this guest has paid so far.
     */
    double getPaidAmount();

    /**
     * Return the remaining balance on the bill.
     *
     * @return a double representing the remaining balance on the bill.
     */
    double getUnpaidAmount();

    /**
     * Return the amount the guest tipped the restaurant.
     *
     * @return a double representing the tip.
     */
    double getTipAmount();

    /**
     * Increases the tipAmount by amount.
     *
     * @param amount the amount to increase the tipAmount
     */
    void tip(double amount);

    /**
     * Pay amount.
     *
     * @param amount the amount that the guest is paying.
     */
    void pay(double amount);

    /**
     * Returns a String representation of the Bill.
     *
     * @return a String that displays the Bill.
     */
    String getBillString();
}
