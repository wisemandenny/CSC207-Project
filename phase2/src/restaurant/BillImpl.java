package restaurant;

import menu.Ingredient;
import menu.MenuItem;

public class BillImpl implements Bill {
    private final int tableId;
    private final Order bill = new OrderImpl();
    private double paidAmount = 0.00;
    private double tipAmount = 0.00;

    BillImpl(int tableId) {
        this.tableId = tableId;
    }

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
    public String getBillString() {
        StringBuilder sb = new StringBuilder("");
        sb.append("BILL FOR TABLE #" + tableId + "\n");
        for (MenuItem item : bill.getItems()) {
            if (item.getPrice() == 0.0) {
                sb.append(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getTotal()) + "   Sent back because: " + item.getComment() + ".\n");
            } else {
                sb.append(item.getQuantity() + " " + item.getName() + ": $" + String.format("%.2f", item.getTotal()) + "\n");
            }
            for (Ingredient addedIng : item.getExtraIngredients()) {
                sb.append("add " + item.getQuantity() + " " + addedIng.getName() + ": $" + String.format("%.2f", addedIng.getPrice() * item.getQuantity()) + "\n");
            }
            for (Ingredient removedIng : item.getRemovedIngredients()) {
                sb.append("remove " + item.getQuantity() + " " + removedIng.getName() + ": -$" + String.format("%.2f", removedIng.getPrice() * item.getQuantity()) + "\n");
            }

        }
        sb.append("Subtotal: $" + String.format("%.2f", getSubtotal()) + "\n");
        sb.append("Total: $" + String.format("%.2f", getTotal()) + "\n");
        return sb.toString();
    }

    @Override
    public double getTotal() {
        return getSubtotal() * (1.00 + Restaurant.getInstance().getTaxRate());
    }

    @Override
    public double getSubtotal() {
        double cost = 0.00;

        for (MenuItem item : bill.getItems()) {
            cost += (item.getPrice() * item.getQuantity());
            cost += item.getExtraIngredientPrice();
            cost -= item.getRemovedIngredientsPrice();
        }
        return cost;
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
