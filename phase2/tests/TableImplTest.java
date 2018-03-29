// TODO: write these tests

import menu.BurgerMenu;
import menu.Menu;
import org.junit.Test;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;
import restaurant.TableImpl;

import static junit.framework.TestCase.assertEquals;
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
    public void testRemoveFromBill(){}

    @Test
    public void testAddSeat(){}

    @Test
    public void testRemoveSeat(){}

    @Test
    public void testJoinCheques(){}

    @Test
    public void testResetTable(){}
}