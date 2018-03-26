package restaurant;

import menu.MenuItem;

public class BillImpl implements Bill {
    private final Order bill = new OrderImpl();
    private double paidAmount = 0.00;
    private double tipAmount = 0.00;

    @Override
    public void add(Order o) {
        bill.add(o);
    }

    @Override
    public void remove(Order o) {
        bill.remove(o);
    }

    @Override
    public void comp(Order o) {
        for (MenuItem item : o.getItems()) {
            bill.returned(item);
        }
    }

    @Override
    public Order getOrder(){
        return bill;
    }

    @Override
    public void joinBill(Bill toJoin) {
        bill.add(toJoin.getOrder());
    }

    @Override
    public double getTotal() {
        return getSubtotal() + getTaxAmount();
    }

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
    @Override
    public double getTaxAmount(){
        return getSubtotal() * Restaurant.getInstance().getTaxRate();
    }

    @Override
    public double getPaidAmount(){
        return paidAmount;
    }

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

    @Override
    public void tip(double amount){
        tipAmount += amount;
    }

    @Override
    public double getTipAmount(){
        return tipAmount;
    }
}
