package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Background;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import menu.Ingredient;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * This class contains the viewer for the cook. They can see orders here, as well as acknowledge that they have
 * received, cooked, and finished cooking a given order.
 */
public class CookOrderView extends Observable implements Initializable {
    @FXML private JFXMasonryPane orderMasonryPane;
    @FXML private JFXButton receivedButton;
    @FXML private JFXButton firedButton;
    @FXML private JFXButton readyButton;

    private List<OrderBox> orderBoxList = new ArrayList<>();
    private VBox selectedOrderBox;
    private Background storedBoxBackground;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedButton.setOnAction(e -> receiveOrder());
        firedButton.setOnAction(e -> fireOrder());
        readyButton.setOnAction(e -> readyOrder());
        refresh();
    }

    /**
     * Refreshes the elements in the view.
     */
    void refresh() {
        orderMasonryPane.getChildren().clear();
        orderBoxList.clear();

        List<VBox> orderList = new ArrayList<>();
        for (Order placedOrder : Restaurant.getInstance().getPlacedOrders()) {
            orderList.add(makeOrderBox(placedOrder, "placed"));
        }
        for (Order receivedOrder : Restaurant.getInstance().getReceivedOrders()) {
            orderList.add(makeOrderBox(receivedOrder, "received"));
        }
        for (Order cookingOrder : Restaurant.getInstance().getCookingOrders()) {
           orderList.add(makeOrderBox(cookingOrder, "cooking"));
        }
        for (Order readyOrder : Restaurant.getInstance().getReadyOrders()) {
            orderList.add(makeOrderBox(readyOrder, "ready"));
        }
        //In order for the vboxes' size to be calculated, they need to be put into a scene and then called applyCss and layout.
        //if not, the boxes will not be rendered because there height and width will be null.
        Group group = new Group();
        Scene scene = new Scene(group);
        group.getChildren().addAll(orderList);
        group.applyCss();
        group.layout();
        for(Node n : group.getChildren()){
            VBox v = (VBox) n;
            v.setMinWidth(v.getWidth() + v.getSpacing());
            VBox.setVgrow(v, Priority.NEVER);
            v.setPadding(new Insets(10,10,10,10));
            v.autosize();
        }
        orderMasonryPane.getChildren().addAll(group.getChildren());
    }

    /**
     * Builds a new box from an order.
     * @param o The Order object containing the order
     * @param flag the flag representing the order's status
     * @return the colored VBox object representing the order
     */
    private VBox makeOrderBox(Order o, String flag) {
        OrderBox orderBox = new OrderBox(o, flag);
        orderBoxList.add(orderBox);
        return orderBox.getVBox();
    }

    /**
     * Performs the select operation on the given orderBox.
     * @param orderBox the box to be selected
     */
    private void selectBox(VBox orderBox){
        if(orderBox.getBackground().equals(Backgrounds.SELECTED_BACKGROUND)){ //click the selected box
            deselectBox(orderBox);
        } else if (selectedOrderBox == null){ //click an unselected box, make sure you can only click one at a time
            storedBoxBackground = orderBox.getBackground();
            orderBox.setBackground(Backgrounds.SELECTED_BACKGROUND);
            selectedOrderBox = orderBox;
        }
    }

    /**
     * Deselects the given order box
     * @param orderBox the order box to be deselected
     */
    private void deselectBox(VBox orderBox){
        selectedOrderBox = null;
        orderBox.setBackground(storedBoxBackground);
    }

    /**
     * Receives the selected order
     */
    private void receiveOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                Restaurant.getInstance().newEvent("update | received | "+orderId);
                deselectBox(orderBox.getVBox());
                letBackendCatchUp();
                break;
            }
        }
    }

    /**
     * Starts cooking (fires) the selected order.
     */
    private void fireOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                if(!Restaurant.getInstance().getPlacedOrders().isEmpty()){
                    mustAcknowledgeOrderWarning(orderId, "cook");
                    deselectBox(orderBox.getVBox());
                    break;
                } else{
                    Restaurant.getInstance().newEvent("update | fired | "+orderId);
                    deselectBox(orderBox.getVBox());
                    letBackendCatchUp();
                    break;
                }
            }
        }
    }

    /**
     * Finishes cooking (readys) the selected order.
     */
    private void readyOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                if(!Restaurant.getInstance().getPlacedOrders().isEmpty()){
                    mustAcknowledgeOrderWarning(orderId, "finish");
                    deselectBox(orderBox.getVBox());
                    break;

                } else {
                    Restaurant.getInstance().newEvent("update | ready | " + orderId);
                    deselectBox(orderBox.getVBox());
                    letBackendCatchUp();
                    break;
                }
            }
        }
    }

    /**
     * Makes a popup if the cook tries to cook or ready an order before acknowledging a newly placed order.
     * @param orderId the order the cook tried to update.
     * @param verb the "action" the cook tried to do (fire, ready)
     */
    private void mustAcknowledgeOrderWarning(int orderId, String verb){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("You must acknowledge all placed orders before " + verb + "ing order " + orderId +".");
        alert.showAndWait();
    }

    /**
     * Pauses the frontend thread for 300ms since the backend does not do events instantly.
     */
    private void letBackendCatchUp(){
        try{
            Thread.sleep(300);
        }catch(InterruptedException ex){
            RestaurantLogger.log(Level.SEVERE, ex.toString());
        }
        setChanged();
        notifyObservers();
    }

    /**
     * A container class which holds an Order as well as a VBox representation of that order.
     */
    private class OrderBox {
        private VBox box;
        private Order order;

        /**
         * Constructs an orderbox for the given order and order status.
         * @param order The order itself
         * @param flag the status of the order (placed, received, fired, ready)
         */
        OrderBox(Order order, String flag){
            this.order = order;
            box = new VBox(10);
            box.setOnMouseClicked(e -> selectBox(box));
            Text orderHeader = new Text("Order: " + order.getId() + "\nTable: " + order.getTableId());
            box.getChildren().add(orderHeader);
            for (MenuItem item : order.getItems()){
                box.getChildren().add(makeItemText(item));
            }

            switch (flag) {
                case "placed":
                    box.setBackground(Backgrounds.GREY_BACKGROUND);
                    break;
                case "received":
                    box.setBackground(Backgrounds.RED_BACKGROUND);
                    break;
                case "cooking":
                    box.setBackground(Backgrounds.YELLOW_BACKGROUND);
                    break;
                case "ready":
                    box.setBackground(Backgrounds.GREEN_BACKGROUND);
                    break;
                default:
                    box.setBackground(Backgrounds.GREY_BACKGROUND);
                    break;
            }
        }

        /**
         * Returns a Text object for displaying a MenuItem's properties in an OrderBox.
         * @param item the MenuItem to be displayed
         * @return the Text object comprised of the item's quantity, name, and any present mods.
         */
        private Text makeItemText(MenuItem item) {
            //item header
            StringBuilder sb = new StringBuilder(item.getQuantity() + " " + item.getName());
            //item mods
            if(!item.getExtraIngredients().isEmpty()) {
                sb.append(" with extra ");
                for (Ingredient extra : item.getExtraIngredients()) {
                    sb.append(extra.getName()).append(", ");
                }
                sb.delete(sb.length() - 2, sb.length()); //delete trailing commas
            }
            if(!item.getRemovedIngredients().isEmpty()){
                sb.append(" with no ");
                for(Ingredient extra : item.getRemovedIngredients()){
                    sb.append(extra.getName()).append(", ");
                }
                sb.delete(sb.length()-2, sb.length()); //delete trailing commas
            }
            Text itemLabel = new Text(sb.toString());
            itemLabel.setWrappingWidth(200);
            return itemLabel;
        }

        /**
         * Returns the order of this OrderBox.
         * @return the order of this OrderBox
         */
        Order getOrder(){ return order; }

        /**
         * Returns the VBox representation of the Order.
         * @return the VBox representation of the Order
         */
        VBox getVBox() { return box; }

        /**
         * {@inheritDoc}
         */
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderBox orderBox = (OrderBox) o;
            return Objects.equals(box, orderBox.box) ||
                    Objects.equals(order, orderBox.order);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        public int hashCode() {
            return Objects.hash(box, order);
        }
    }
}
