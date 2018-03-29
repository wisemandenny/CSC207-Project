import restaurant.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class BillImplTest {
    Restaurant r = Restaurant.getInstance(10, 0.13);


    @Test
    public void testSubtotal(){
        r.start();

        Order order = OrderFactory.makeOrder("1 Hamburger / +Ketchup -Bacon -Mustard, 1 Hamburger, 1 Coke");
        Bill bill = new BillImpl();
        bill.add(order);
        assertTrue(bill.getSubtotal() == 14.23);
        Order order1 = OrderFactory.makeOrder("1 Hamburger / -Ketchup -Bacon -Mustard +Ketchup +Bacon +Mustard, 1 Hamburger, 1 Coke");
        bill.add(order1);
        assertTrue(bill.getSubtotal() == 31.46);
    }

    @Test
    public void testTotal(){
        Order order = OrderFactory.makeOrder("1 Hamburger / +Ketchup -Bacon -Mustard, 1 Hamburger, 1 Coke");
        Bill bill = new BillImpl();
        bill.add(order);
        Order order1 = OrderFactory.makeOrder("1 Hamburger / -Ketchup -Bacon -Mustard +Ketchup +Bacon +Mustard, 1 Hamburger, 1 Coke");
        bill.add(order1);
        assertTrue(bill.getTotal() == 35.549800000000005);

    }

    @Test
    public void testGetTaxAmount(){
        Order order = OrderFactory.makeOrder("1 Hamburger / +Ketchup -Bacon -Mustard, 1 Hamburger, 1 Coke");
        Bill bill = new BillImpl();
        bill.add(order);
        Order order1 = OrderFactory.makeOrder("1 Hamburger / -Ketchup -Bacon -Mustard +Ketchup +Bacon +Mustard, 1 Hamburger, 1 Coke");
        bill.add(order1);
        assertTrue(bill.getTaxAmount() == 4.0898);

    }
}
