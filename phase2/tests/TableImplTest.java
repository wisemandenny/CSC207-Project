
import menu.BurgerMenu;
import menu.Menu;
import org.junit.Test;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;
import restaurant.TableImpl;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;
import static restaurant.OrderFactory.makeOrder;

public class TableImplTest {
    private static final Menu menu = new BurgerMenu();

    Restaurant r = Restaurant.getInstance(10, 0.13);

    @Test
    public void testAddOrder(){
        r.start();
        Order o = makeOrder(menu.getMenu(), 1, 1, 1);
        Table t = new TableImpl(1, false);
        int currValue = t.getOrders().size();
        t.addOrder(o);
        assertEquals(currValue + 1, t.getOrders().size() );

    }

    @Test
    public void testAddOrderToBill(){
        r.start();
        Order o = makeOrder(menu.getMenu(), 1, 1, 1);
        Table t = new TableImpl(1, false);
        t.addOrder(o);
        t.addOrderToBill(o);
        assertEquals(0, t.getOrders().size());
    }

    @Test
    public void testRemoveFromBill(){
        r.start();
        Order o = makeOrder(menu.getMenu(), 1, 1, 1);
        Table t = new TableImpl(1, false);
        t.addOrderToBill(o);
        t.removeFromBill(o);
        assertTrue(t.getBill().getOrder().getItems().isEmpty());
    }

    @Test
    public void testAddSeat(){
        Table t = new TableImpl(1, false);
        int currValue = t.getSeats().size();
        t.addSeat();
        assertEquals(currValue + 1, t.getSeats().size());
    }

    @Test
    public void testRemoveSeat(){
        Table t = new TableImpl(1, false);
        int currValue = t.getSeats().size();
        t.addSeat();
        t.removeSeat(0);
        assertEquals(currValue, t.getSeats().size());
    }

    @Test
    public void testJoinCheques(){
        Table t = new TableImpl(1, false);
        t.joinCheques();
        assertEquals(true, t.isJoined());
    }

    @Test
    public void testResetTable(){
        Table t = new TableImpl(1, false);
        Order o = makeOrder(menu.getMenu(), 1, 1, 1);
        t.addOrder(o);
        t.addOrderToBill(o);
        t.joinCheques();
        t.resetTable();

        assertTrue(t.getBill().getOrder().getItems().isEmpty());
        assertTrue(t.getSeats().size() == 5);
        assertEquals(false, t.isJoined());
    }
}