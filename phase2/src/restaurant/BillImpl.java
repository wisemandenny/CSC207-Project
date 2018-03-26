package restaurant;

import menu.MenuItem;

public class BillImpl implements Bill {
    private final Order bill = new OrderImpl();
    private double paidAmount = 0.00;
    private double tipAmount = 0.00;

    /**
     * {@inheritDoc}
     */
    @Override
    public void add(Order o) {
        bill.add(o);
    }

    /**
     * {@inheritDoc}
     * */
    @Override
    public void remove(Order o) {
        bill.remove(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void comp(Order o) {
        for (MenuItem item : o.getItems()) {
            bill.returned(item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Order getOrder(){
        return bill;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void joinBill(Bill toJoin) {
        bill.add(toJoin.getOrder());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTotal() {
        return getSubtotal() + getTaxAmount();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getSubtotal() {
        double subtotal = 0.00;

        for (MenuItem item : bill.getItems()) {
            subtotal += (item.getPrice() * item.getQuantity());
            subtotal += item.getExtraIngredientPrice();
            subtotal -= item.getRemovedIngredientsPrice();
        }
        return subtotal;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTaxAmount(){
        return getSubtotal() * Restaurant.getInstance().getTaxRate();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getPaidAmount(){
        return paidAmount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void pay(double amount){
        double balance = getTotal()-paidAmount;
        if(balance-amount >= 0){
            paidAmount += amount;
        } else {
            paidAmount = getTotal();
            tipAmount += amount-balance;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void tip(double amount){
        tipAmount += amount;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public double getTipAmount(){
        return tipAmount;
    }
}
