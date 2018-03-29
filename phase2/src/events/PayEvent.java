package events;

import restaurant.Bill;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;
import restaurant.Table;

import java.util.logging.Level;

public class PayEvent implements Event {
    private Table table;
    private int seatNumber;
    private double paymentAmount;
    PayEvent(Table table, int seatNumber, String paymentAmount){
        this.table = table;
        this.seatNumber = seatNumber;
        this.paymentAmount = Double.parseDouble(paymentAmount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public EventType getType() {
        return EventType.PAY;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    // TODO: issue paying for Table as seat 0. Check tableImpl joinCheques, try getting the bill Total of seat 0.
    // TODO: this means getting the bill of seat 0 (the joined "Table" seat) is pointless.
    public void doEvent() {
        if (!table.isJoined()) {
            Bill bill = table.getSeat(seatNumber).getBill();
            process(bill);

            RestaurantLogger.log(Level.INFO, "Payment of $" + paymentAmount + " received from Table " + table.getId() + " seat " + seatNumber);
        } else {
            Bill bill = table.getBill();
            process(bill);
            RestaurantLogger.log(Level.INFO, "Payment of $" + paymentAmount + " received from Table " + table.getId() + ".");

        }
    }

    /**
     * Does most of the work for doEvent, pays the Bill, adds the total payment and the tip.
     *
     * @param b the Bill.
     */
    private void process(Bill b){
        b.pay(paymentAmount);
        Restaurant.getInstance().addPayment(paymentAmount - b.getTipAmount());
        Restaurant.getInstance().addToTipTotal(b.getTipAmount());
    }
}
