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

    @Override
    public EventType getType() {
        return EventType.PAY;
    }

    @Override
    public void doEvent() {
        Bill bill = table.getSeat(seatNumber).getBill();
        table.getSeat(seatNumber).getBill().pay(paymentAmount);
        Restaurant.getInstance().addPayment(bill.getTotal());
        Restaurant.getInstance().addToTipTotal(bill.getTipAmount());

        RestaurantLogger.log(Level.INFO, "Payment of $" + paymentAmount + " received from Table " + table.getId() + " seat " + seatNumber);
    }
}
