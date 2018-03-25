package restaurant;

import java.util.ArrayList;
import java.util.List;

public class TableImpl implements Table {
    private final int id;
    private final List<Order> orders = new ArrayList<>();
    private final Bill bill;
    private final List<Seat> seats = new ArrayList<>();


    /**
     * Constructs a new restaurant.TableImpl object.
     *  @param id the integer representing the id of this restaurant.TableImpl
     *
     */
    TableImpl(int id) {
        this.id = id;
        bill = new BillImpl(id);
        int i;
        for (i = 1; i <= 4; i++){
            Seat curr = new Seat(i);
            seats.add(curr);
        }
    }

    TableImpl() {
        this.id = 0;
        this.bill = new BillImpl(id);
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
        int i;
        if (amount > 0) {
            for (i = 1; i < amount; i++){
                // TODO this might throw an error if the array list is empty
                int last = seats.get(seats.size() - 1).getId();
                seats.add(new Seat(last + i));
            }
        }
    }

    @Override
    public void removeSeats(int amount){
        if (amount < 0) {
            boolean flag = false;
            if(seats.size() + amount >= 0){
                for (Seat s : seats){
                    if (! s.getOrders().isEmpty()){
                        flag = true;
                        break;
                    }
                }
                if (!flag){
                    int i;
                    for (i = 1; i <= (-1*amount); i++) {
                        seats.remove(seats.size() - 1);
                    }
                } else{
                    throw new IllegalArgumentException("You cannot remove seats because orders have been made!");
                }
            }
        }
    }

    @Override
    public Seat getSeat(int index){
        return seats.get(index);
    }

    /**
     * Prints to the screen this restaurant.TableImpl's current bill.
     * The bill includes how many of each menu.MenuItem were ordered, and its respective menu.Ingredient modifications, along with their respective prices.
     * If the bill had deductions they are shown below the order, with the number of deducted items, the price of the deduction and the total price of the order.
     */
    @Override
    public void printBill() {
        bill.getBillString();
    }
}
