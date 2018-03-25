package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class DeliverableOrdersView implements Initializable{
    @FXML private StackPane deliverableOrderStackPane;
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
            deliverableOrdersListView.getItems().add(makeOrderHBox(o));
        }
    }

    private HBox makeOrderHBox(Order o){
        OrderBox orderBox = new OrderBox(o);
        deliverableOrders.add(orderBox);
        return orderBox.getHBox();

    }

    void refresh() {
        deliverableOrdersListView.getItems().clear();
        addDeliverableOrders();
    }

    private class OrderBox {
        private HBox HBox;
        private Order order;

        OrderBox(Order order){
            this.order = order;
            Label orderHeader = new Label("Order: " + order.getId() + " Table: " + order.getTableId());
            Region filler = new Region();
            HBox = new HBox();
            HBox.setOnMouseClicked(e -> loadDialog(HBox));

            HBox.setHgrow(filler, Priority.ALWAYS);
            HBox.getChildren().add(orderHeader);
            HBox.getChildren().add(filler);
            for(MenuItem item : order.getItems()){
                HBox.getChildren().add(makeItemLabel(item));
            }
        }
        private Label makeItemLabel(MenuItem item){
            Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
            itemLabel.autosize();
            return itemLabel;
        }
        Order getOrder(){ return order; }
        HBox getHBox() { return HBox; }
    }

    private void loadDialog(HBox box){
        Label deliverDialogHeader = new Label("Deliver This Order"); //maybe add the order info here
        HBox container = new HBox();
        JFXListView orderItemListView = new JFXListView();
        container.getChildren().add(orderItemListView);
        VBox buttonContainer = new VBox();
        JFXButton deliverButton = new JFXButton("Deliver");
        JFXButton returnButton = new JFXButton("Return");
        buttonContainer.getChildren().addAll(deliverButton, returnButton);
        container.getChildren().add(buttonContainer);
        StackPane stackPane = (StackPane) deliverableOrderStackPane.getParent().getParent().getParent();

        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(deliverDialogHeader);
        content.setBody(container);
        JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
        dialog.show();
    }
}
