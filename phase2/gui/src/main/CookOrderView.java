package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.ResourceBundle;

public class CookOrderView implements Initializable {
    private static final Background RED_BACKGROUND = new Background(new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background YELLOW_BACKGROUND = new Background(new BackgroundFill(Color.web("#FFEE58"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background GREEN_BACKGROUND = new Background(new BackgroundFill(Color.web("#9CCC65"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#BDBDBD"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background SELECTED_BACKGROUND = new Background(new BackgroundFill(Color.web("#29B6F6"), CornerRadii.EMPTY, Insets.EMPTY));

    @FXML
    private JFXMasonryPane orderMasonryPane;

    @FXML
    private JFXButton receivedButton;

    @FXML
    private JFXButton firedButton;

    @FXML
    private JFXButton readyButton;

    private List<OrderBox> orderBoxList = new ArrayList<>();
    private VBox selectedOrderBox;
    private Background storedBoxBackground;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedButton.setOnAction(e -> receiveOrder());
        firedButton.setOnAction(e -> fireOrder());
        readyButton.setOnAction(e -> readyOrder());
        refresh();
    }

    class OrderBox {
        private VBox box;
        private Order order;

        OrderBox(Order order, String flag){
            this.order = order;
            box = new VBox();
            box.setOnMouseClicked(e -> selectBox(box));
            Label orderHeader = new Label("Order: " + order.getId() + "\nTable: " + order.getTableId());
            box.getChildren().add(orderHeader);
            for (MenuItem item : order.getItems()){
                box.getChildren().add(makeItemLabel(item));
            }

            switch (flag) {
                case "placed":
                    box.setBackground(GREY_BACKGROUND);
                    break;
                case "received":
                    box.setBackground(RED_BACKGROUND);
                    break;
                case "cooking":
                    box.setBackground(YELLOW_BACKGROUND);
                    break;
                case "ready":
                    box.setBackground(GREEN_BACKGROUND);
                    break;
                default:
                    box.setBackground(GREY_BACKGROUND);
                    break;
            }

            box.setPrefSize(100, 150);
            box.autosize();
        }
        private Label makeItemLabel(MenuItem item) {
            Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
            itemLabel.setBackground(Background.EMPTY); //transparent background
            itemLabel.autosize();
            return itemLabel;
        }
        Order getOrder(){ return order; }
        VBox getVBox() { return box; }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            OrderBox orderBox = (OrderBox) o;
            return Objects.equals(box, orderBox.box) ||
                    Objects.equals(order, orderBox.order);
        }

        @Override
        public int hashCode() {
            return Objects.hash(box, order);
        }
    }

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
        orderMasonryPane.getChildren().addAll(orderList);
    }
    //TODO: make an internal class orderbox which holds the order itself and the Vbox representation.
    //then add orderbox.getVbox or something to the masonry pane
    //when you need to update the order, you can do Restaurant.makeEvent(orderBox.getOrder) which will update the sttus of the order in the backend
    //then you can just do refresh and it will update the order's color in the frontend

    private VBox makeOrderBox(Order o, String flag) {
        OrderBox orderBox = new OrderBox(o, flag);
        orderBoxList.add(orderBox);
        return orderBox.getVBox();
    }

    private void selectBox(VBox orderBox){
        if(orderBox.getBackground().equals(SELECTED_BACKGROUND)){ //click the selected box
            deselectBox(orderBox);
        } else if (selectedOrderBox == null){ //click an unselected box, make sure you can only click one at a time
            storedBoxBackground = orderBox.getBackground();
            orderBox.setBackground(SELECTED_BACKGROUND);
            selectedOrderBox = orderBox;
        }
    }

    private void deselectBox(VBox orderBox){
        selectedOrderBox = null;
        orderBox.setBackground(storedBoxBackground);
    }

    private void receiveOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                Restaurant.getInstance().addReceivedOrder(orderId);
                deselectBox(orderBox.getVBox());
                refresh();
                break;
            }
        }

    }
    private void fireOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                Restaurant.getInstance().addCookingOrder(orderId);
                deselectBox(orderBox.getVBox());
                refresh();
                break;
            }
        }
    }
    private void readyOrder(){
        for(OrderBox orderBox : orderBoxList){
            int orderId = orderBox.getOrder().getId();
            if(orderBox.getVBox().equals(selectedOrderBox)){
                Restaurant.getInstance().addReadyOrder(orderId);
                deselectBox(orderBox.getVBox());
                refresh();
                break;
            }
        }
    }
}
