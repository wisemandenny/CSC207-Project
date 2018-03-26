package restaurant;

import java.util.ArrayList;
import java.util.List;

public class TableImpl implements Table {
    private final int id;
    private final List<Order> orders = new ArrayList<>();
    private final Bill bill;
    //a table has seats. the seats are "subtables" of the parent table. There is a placeholder table, table 0, which
    //holds items which will be paid by the whole table (split)
    private final List<Table> seats = new ArrayList<>();


    /**
     * Constructs a new restaurant.TableImpl object.
     *  @param id the integer representing the id of this restaurant.TableImpl
     *
     */
    TableImpl(int id, boolean isSubtable) {
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

    @Override
    public List<Order> getOrders(){ return orders; }

    @Override
    public List<List<Order>> getAllOrders(){
        List<List<Order>> allOrders = new ArrayList<>();
        for(int i = 0; i < seats.size(); i++){
            allOrders.add(getSeat(i).getOrders());
        }
        return allOrders;
    }
    @Override
    public int getId() {
        return id;
    }

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

    @Override
    public void addOrderToBill(Order o) {
        orders.remove(o);
        bill.add(o);
    }

    @Override
    public void removeFromBill(Order o) {
        bill.remove(o);
    }

    @Override
    public void comp(Order o) {
        bill.comp(o);
    }

    @Override
    public void addSeat(){
        seats.add(new TableImpl(seats.size()+1, true));
    }

    @Override
    public void removeSeat(int seatNumber){
        if (seats.size() - 1 >= 0 && seats.get(seatNumber) != null) { //if there are seats to remove, and the sel
            Table s = seats.get(seatNumber);
            if (s.getOrders().isEmpty()){
                seats.remove(s);
            } else {
                System.out.println("You cannot remove seats because orders have been made!"); //TODO: change this to logger
            }
        }
    }

    @Override
    public Table getSeat(int index){
        return seats.get(index);
    }

    @Override
    public double getAutogratuityAmount() {
        if(seats.size() >= 8){
            return bill.getSubtotal() * Restaurant.getInstance().getAutoGratRate();
        }
        return 0.0;
    }

    @Override
    public void joinCheques() {
        if(seats.isEmpty()){
            System.out.println("can't join seats cheques (need to join the TABLE'S cheques"); //TODO: change this to loger
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
        }
    }

    @Override
    public String getTableBillString(){
        String billString = getBill().getBillString();
        return ("BILL FOR TABLE " + id).concat(billString);
    }
}
