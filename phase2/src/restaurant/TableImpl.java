package restaurant;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class TableImpl implements Table {
    private final int id;
    private final List<Order> orders = new ArrayList<>();
    private Bill bill;
    private boolean joined;
    //a table has seats. the seats are "subtables" of the parent table. There is a placeholder table, table 0, which
    //holds items which will be paid by the whole table (split)
    private final List<Table> seats = new ArrayList<>();
    /**
     * Constructs a new restaurant.TableImpl object.
     *  @param id the integer representing the id of this restaurant.TableImpl
     *
     */
    public TableImpl(int id, boolean isSubtable) {
        this.id = id;
        bill = new BillImpl();
        //initialize all tables with 4 seats
        if(!isSubtable) {
            for (int i = 0; i <= 4; i++) {
                seats.add(new TableImpl(i, true));
            }
        }
    }
    /**
     * Sets this restaurant.TableImpl's order to contain the restaurant.Order o.
     *
     * @param o The restaurant.Order object to be stored in this restaurant.TableImpl
     */
    @Override
    public void addOrder(Order o) {
        orders.add(o);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<Order> getOrders(){ return orders; }
    /**
     * {@inheritDoc}
     */
    @Override
    public List<List<Order>> getAllOrders(){
        List<List<Order>> allOrders = new ArrayList<>();
        for(int i = 0; i < seats.size(); i++){
            allOrders.add(getSeat(i).getOrders());
        }
        return allOrders;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public int getId() {
        return id;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Bill getBill(){
        if(seats.isEmpty()){ //seat bill
            return bill;
        } else {
            //add all the bills together and return that bill
            Bill joinedBill = new BillImpl();
            for(Table seat : seats){
                joinedBill.joinBill(seat.getBill());
            }
            return  joinedBill;
        }

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void addOrderToBill(Order o) {
        orders.remove(o);
        bill.add(o);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeFromBill(Order o) {
        bill.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addSeat(){
        seats.add(new TableImpl(seats.size()+1, true));
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void removeSeat(int seatNumber){
        if (seats.size() - 1 >= 0 && seats.get(seatNumber) != null) { //if there are seats to remove, and the sel

            Table s = seats.get(seatNumber);
            if (s.getOrders().isEmpty()){
                seats.remove(s);
            } else {
                RestaurantLogger.log(Level.WARNING, "You cannot remove seats because orders have been made!");
            }
        }
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public Table getSeat(int index){
        return seats.get(index);
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public double getAutogratuityAmount() {
        if(seats.size() >= 9){
            return bill.getSubtotal() * Restaurant.getInstance().getAutoGratRate();
        }
        return 0.0;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void joinCheques() {
        if(seats.isEmpty()){
            RestaurantLogger.log(Level.WARNING, "Cannot join a seats' cheque (need to join the TABLE'S cheque)");
        } else{
            List<Order> allOrders = new ArrayList<>();
            for(Table seat : seats){
                List<Order> ordersToRemove = new ArrayList<>();
                for(Order seatOrder : seat.getOrders()){
                    allOrders.add(seatOrder);
                   ordersToRemove.add(seatOrder);
                }
                seat.getOrders().removeAll(ordersToRemove);
            }
            seats.get(0).getOrders().addAll(allOrders);
            joined = true;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void resetTable(){
        bill = new BillImpl();
        seats.clear();
        for (int i = 0; i <= 4; i++) {
            seats.add(new TableImpl(i, true));
        }
        joined = false;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public String getTableBillString(){
        String billString = getBill().getBillString();
        return ("BILL FOR TABLE " + id + ":\n").concat(billString);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean getJoin(){return joined;}


}
