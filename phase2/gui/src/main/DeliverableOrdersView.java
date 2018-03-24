package main;

import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeliverableOrdersView implements Initializable{
    @FXML private JFXListView<HBox> deliverableOrdersListView;
    private final List<OrderBox> deliverableOrders = new ArrayList<>();
    private HBox selectedDeliverableOrder;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //refresh();
    }

    private void addDeliverableOrders(){
        List<Order> orderList = Restaurant.getInstance().getReadyOrders();
        for(Order o : orderList){
            deliverableOrdersListView.getItems().add(makeOrderBox(o));
        }
    }

    private HBox makeOrderBox(Order o){
        OrderBox orderBox = new OrderBox(o);
        deliverableOrders.add(orderBox);
        return orderBox.getHBox();

    }

    void refresh() {
        deliverableOrdersListView.getItems().clear();
        addDeliverableOrders();
    }

    private class OrderBox {
        private HBox box;
        private Order order;

        OrderBox(Order order){
            this.order = order;
            box = new HBox();
            box.setOnMouseClicked(e -> selectBox(box));
            Label orderHeader = new Label("Order: " + order.getId());
            box.getChildren().add(orderHeader);
            for(MenuItem item : order.getItems()){
                box.getChildren().add(makeItemLabel(item));
            }
        }

        private Label makeItemLabel(MenuItem item){
            Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
            itemLabel.autosize();
            return itemLabel;
        }
        Order getOrder(){ return order; }
        HBox getHBox() { return box; }
    }

    private void selectBox(HBox box){
        //TODO
    }
}
