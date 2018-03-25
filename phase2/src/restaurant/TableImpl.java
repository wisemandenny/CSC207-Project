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
        bill = new BillImpl(id);
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
    public int getId() {
        return id;
    }

    @Override
    public Bill getBill(){
        return bill;
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
    public void addSeats(int amount){
        if (amount > 0) { //the amount of seats being removed should always be positive
            for (int i = 1; i < amount; i++){
                int lastSeat = seats.size()-1;
                //int last = seats.get(seats.size() - 1).getId(); //get the index of the last seat
                // TODO this might throw an error if the array list is empty
                seats.add(new TableImpl(lastSeat + i, true)); //add a new seat at this index
            }
        }
    }

    @Override
    public void removeSeats(int amount){
        if (amount > 0 && seats.size() + amount >= 0) { //if the amount of seats being removed from the table is positive and there are enough seats to remove
//if there are enough seats to remove
            for (Table s : seats){
                if (! s.getOrders().isEmpty()){ //check if there are orders for that seat.
                    throw new IllegalArgumentException("You cannot remove seats because orders have been made!");
                }
            }
            for (int i = 1; i <= amount; i++) { //no orders on any of the seats
                seats.remove(seats.size() - 1); //remove the seat
            }
        }
    }

    @Override
    public Table getSeat(int index){
        return seats.get(index);
    }
}
