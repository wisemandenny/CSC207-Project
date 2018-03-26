package restaurant;

import java.util.List;

public interface Table {
    /** Add Order o to this Table's list of Orders.
     *
     * @param o the Order to be added to this Table's list of orders.
     */
    void addOrder(Order o);

    /** Return a list of Orders that belong to this Table.
     *
     * @return  a List of Orders
     */
    List<Order> getOrders();

    /** Return a list that contains a list of Orders for every seat at this Table
     *
     * @return  A List of Lists that contain Orders.
     */
    List<List<Order>> getAllOrders();

    /** Add Order o to this Table's Bill.
     *
     * @param o the Order to be added to this Table's Bill.
     */
    void addOrderToBill(Order o);

    /** Remove Order o from this Table's Bill.
     *
     * @param o  the Order to be removed from this Table's Bill.
     */
    void removeFromBill(Order o);

    /** Give away Order o for free.
     *
     * @param o  the Order to be given away for free.
     */
    void comp(Order o);

    /** Return this Table's Bill.
     *
     * @return  the Table's Bill.
     */
    Bill getBill();

    /** Add all Orders from each seat to seat 0, which is the Table Order.
     *
     */
    void joinCheques();

    /** Get the amount the Table or seat owes depending on if they have surpassed the auto-gratuity limit.
     *
     * @return a double that represents the subtotal multiplied by the auto gratuity if there 8 or more seats, and 0 otherwise.
     */
    double getAutogratuityAmount();

    /** Return this Table's unique ID.
     *
     * @return an int representing a Table Id
     */
    int getId();

    /** Add a seat to this Table
     *
     */
    void addSeat();

    /** Remove seat at index seatNumber from this Table
     *
     * @param seatNumber the index of a seat in the list of seats that is to be removed.
     */
    void removeSeat(int seatNumber);

    /** Return the seat at index.
     *
     * @param index the index of the seat that is to be removed.
     * @return a Table that represents a seat.
     */
    Table getSeat(int index);

}

