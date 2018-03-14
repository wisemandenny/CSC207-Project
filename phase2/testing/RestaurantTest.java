//make this work by following: https://stackoverflow.com/a/19330833
import org.junit.Test;
import org.junit.Assert;
import restaurant.Order;
import restaurant.OrderImpl;
import restaurant.Restaurant;

//TODO: none of these tests work or do anything.... somebody fix them!
public class RestaurantTest {
    /*
    @Test
    public void test1(){
        Restaurant r = Restaurant.getInstance(1);
        Order o = new OrderImpl("order | table 1 | 100 Hamburger");
        r.addPlacedOrder(o);
        r.addCookingOrder(1);
        r.addReadyOrder(1);
        r.addDeliveredOrder(1);
        //Assert.assertEquals("BILL FOR TABLE #1\n" + "10 Hamburger: $79.90\n" + "Total: $79.90", Restaurant.getTable(1).getBillString());
    }*/

    @Test
    public void test2(){
        Order o = new OrderImpl("order | table 1 | 100 Hamburger, 1 Coke");

    }

    @Test
    public void test3(){
        Order o = new OrderImpl("order | table 1 | 100 Hamburger, 100 Coke");
    }

    @Test
    public void test4(){
        Order o1 = new OrderImpl("order | table 1 | 10 Hamburger");
        Order o2 = new OrderImpl("order | table 1 | 1 Hamburger");

    }

    @Test
    public void test5(){
        Order o = new OrderImpl("order | table 1 | 10 Hamburger / +Patty\n");
    }
}
