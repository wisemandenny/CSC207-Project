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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
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

/**
 * A simple component comprised of a list of orders which are ready to be delivered.
 * If there are no orders ready to be displayed, then a message indicating that will be displayed instead.
 */
public class DeliverableOrdersView extends Observable implements Initializable{
    @FXML private StackPane deliverableOrderStackPane;
    @FXML private JFXListView<HBox> deliverableOrdersListView;
    private final List<OrderBox> deliverableOrders = new ArrayList<>();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        addDeliverableOrders();
    }

    /**
     * Adds any orders which are ready to be delivered to the DeliverableOrdersView.
     */
    private void addDeliverableOrders(){
        List<Order> orderList = Restaurant.getInstance().getReadyOrders();
        if(orderList.isEmpty()){
            deliverableOrdersListView.getItems().add(makeNoOrdersHBox());
        }
        for(Order o : orderList){
            deliverableOrdersListView.getItems().add(makeOrderHBox(o));
        }
    }

    /**
     * Returns a box indicating that there are no orders available to be delivered.
     * @return the Hbox indicating that there are no orders available to be delivered
     */
    private HBox makeNoOrdersHBox(){
        HBox box = new HBox();
        Label header = new Label("No deliverable orders found.");
        header.setFont(Font.font(header.getFont().getFamily(), FontPosture.ITALIC, header.getFont().getSize()));
        box.getChildren().add(header);
        return box;
    }

    /**
     * Returns a box containing any orders which are ready to be delivered.
     * @param o the order which is ready to be delivered
     * @return the HBox representation of the order ready to be delivered
     */
    private HBox makeOrderHBox(Order o){
        OrderBox orderBox = new OrderBox(o);
        deliverableOrders.add(orderBox);
        return orderBox.getHbox();
    }

    /**
     * Refreshes the deliverableorderview element.
     */
    void refresh() {
        deliverableOrdersListView.getItems().clear();
        addDeliverableOrders();
    }

    /**
     * True if there are orders ready to be delivered, else false.
     * @return the boolean representing if there are orders ready to be delivered.
     */
    boolean hasOrders(){
        return !deliverableOrders.isEmpty();
    }

    /**
     * A container class which holds an Order as well as a HBox representation of that order.
     */
    private class OrderBox {
        private HBox hbox;
        private Order order;

        /**
         * Constructs an orderbox for the given order.
         * @param order The order itself
         */
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

        /**
         * Builds the deliver dialog box.
         * @param o the order to be delivered.
         */
        private void loadDialog(Order o){
            //INITIALIZE COMPONENTS
            StackPane stackPane = (StackPane) deliverableOrderStackPane.getParent().getParent().getParent();
            JFXDialogLayout content = new JFXDialogLayout();
            HBox container = new HBox();
            //HEADER
            Label deliverDialogHeader = new Label("Deliver This Order");
            content.setHeading(deliverDialogHeader);
            //BODY
            JFXListView<Label> orderItemListView = new JFXListView<>();
            orderItemListView.setMinSize(200, 200);
            container.getChildren().add(orderItemListView);
            addOrderToListView(orderItemListView, o);
            content.setBody(container);
            //BUTTONS
            JFXButton deliverButton = new JFXButton("Deliver");
            JFXButton closeButton = new JFXButton("Close");
            content.setActions(closeButton, deliverButton);
            //BUILD THE DIALOG BOX
            JFXDialog dialog = new JFXDialog(stackPane, content, JFXDialog.DialogTransition.CENTER, true);
            //SET BUTTON ACTION LISTENERS
            deliverButton.setOnAction(e -> {
                deliver();
                dialog.close();
            });
            closeButton.setOnAction(e -> dialog.close());
            //SHOW THE DIALOG BOX
            dialog.show();
        }

        /**
         * Adds a given order to the listview of deliverable orders.
         * @param lv the listview of deliverable orders
         * @param o the deliverable order
         */
        private void addOrderToListView(JFXListView<Label> lv, Order o){
            ObservableList<Label> orderItemList = FXCollections.observableArrayList();
            for(MenuItem item : o.getItems()){
                orderItemList.add(makeItemLabel(item));
            }
            lv.setItems(orderItemList);
        }

        /**
         * Makes labels for each MenuItem in the order for the dialog box.
         * @param item the MenuItem in the order
         * @return the Label element representing the MenuItem
         */
        private Label makeItemLabel(MenuItem item){
            Label itemLabel = new Label(item.getQuantity() + " " + item.getName() + " ");
            itemLabel.autosize();
            return itemLabel;
        }

        /**
         * Returns the order of this OrderBox.
         * @return the order of this OrderBox
         */
        Order getOrder(){ return order; }

        /**
         * Returns the HBox representation of the Order.
         * @return the HBox representation of the Order
         */
        HBox getHbox() { return hbox; }

        /**
         * Sends the delivery event to the backend.
         */
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
