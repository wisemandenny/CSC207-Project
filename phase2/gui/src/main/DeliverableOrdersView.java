package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;
import java.util.logging.Level;

public class DeliverableOrdersView extends Observable implements Initializable{
    @FXML private StackPane deliverableOrderStackPane;
    @FXML private JFXListView<HBox> deliverableOrdersListView;
    private final List<OrderBox> deliverableOrders = new ArrayList<>();

    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDeliverableOrders();
    }

    boolean hasOrders(){
        return !deliverableOrders.isEmpty();
    }

    private void addDeliverableOrders(){
        List<Order> orderList = Restaurant.getInstance().getReadyOrders();
        if(orderList.isEmpty()){
            deliverableOrdersListView.getItems().add(makeNoOrdersHBox());
        }
        for(Order o : orderList){
            deliverableOrdersListView.getItems().add(makeOrderHBox(o));
        }
    }

    private HBox makeNoOrdersHBox(){
        HBox box = new HBox();
        Label header = new Label("No deliverable orders found.");
        header.setFont(Font.font(header.getFont().getFamily(), FontPosture.ITALIC, header.getFont().getSize()));
        box.getChildren().add(header);
        return box;
    }

    private HBox makeOrderHBox(Order o){
        OrderBox orderBox = new OrderBox(o);
        deliverableOrders.add(orderBox);
        return orderBox.getHbox();

    }

    void refresh() {
        deliverableOrdersListView.getItems().clear();
        addDeliverableOrders();
    }

    private class OrderBox {
        private HBox hbox;
        private Order order;

        OrderBox(Order order){
            this.order = order;
            Label orderHeader = new Label("Order: " + order.getId() + " Table: " + order.getTableId());
            Region filler = new Region();
            hbox = new HBox();
            hbox.setBackground(Backgrounds.GREEN_BACKGROUND);
            hbox.setOnMouseClicked(e -> loadDialog(order));

            HBox.setHgrow(filler, Priority.ALWAYS);
            hbox.getChildren().add(orderHeader);
            hbox.getChildren().add(filler);
            for(MenuItem item : order.getItems()){
                hbox.getChildren().add(makeItemLabel(item));
            }
        }
        private void loadDialog(Order o){
            Label deliverDialogHeader = new Label("Deliver This Order");
            HBox container = new HBox();
            JFXListView<Label> orderItemListView = new JFXListView<>();
            orderItemListView.setMinSize(200, 200);
            container.getChildren().add(orderItemListView);
            addOrderToListView(orderItemListView, o);
            VBox buttonContainer = new VBox();
            JFXButton deliverButton = new JFXButton("Deliver");
            JFXButton closeButton = new JFXButton("Close");

            buttonContainer.getChildren().addAll(deliverButton, closeButton);
            container.getChildren().add(buttonContainer);
            StackPane stackPane = (StackPane) deliverableOrderStackPane.getParent().getParent().getParent();
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(deliverDialogHeader);
            content.setBody(container);
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
            deliverButton.setOnAction(e -> {
                deliver();
                dialog.close();
            });
            closeButton.setOnAction(e -> dialog.close());
            dialog.show();
        }
        private void addOrderToListView(JFXListView<Label> lv, Order o){
            ObservableList<Label> orderItemList = FXCollections.observableArrayList();
            for(MenuItem item : o.getItems()){
                orderItemList.add(makeItemLabel(item));
            }
            lv.setItems(orderItemList);
        }
        private Label makeItemLabel(MenuItem item){
            Label itemLabel = new Label(item.getQuantity() + " " + item.getName() + " ");
            itemLabel.autosize();
            return itemLabel;
        }
        Order getOrder(){ return order; }
        HBox getHbox() { return hbox; }

        private void deliver(){
            Restaurant.getInstance().newEvent("update | delivered | " + getOrder().getId());
            try{ //wait for backend....
                Thread.sleep(300);
            } catch(InterruptedException ex) {
                RestaurantLogger.log(Level.SEVERE, ex.toString());
            }
            setChanged();
            notifyObservers();
        }
    }
}
