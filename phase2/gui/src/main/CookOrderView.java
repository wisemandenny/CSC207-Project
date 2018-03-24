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
import java.util.ResourceBundle;

public class CookOrderView implements Initializable {
    @FXML
    private JFXMasonryPane orderMasonryPane;

    @FXML
    private JFXButton receivedButton;

    @FXML
    private JFXButton firedButton;

    @FXML
    private JFXButton readyButton;

    private VBox selectedOrderBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        receivedButton.setOnAction(e -> receiveOrder());
        firedButton.setOnAction(e -> fireOrder());
        readyButton.setOnAction(e -> readyOrder());
        refresh();
    }

     void refresh() {
        orderMasonryPane.getChildren().clear();
        List<VBox> orderList = new ArrayList<>();
        for (Order placedOrder : Restaurant.getInstance().getPlacedOrders()) {
            orderList.add(makeOrderBox(placedOrder, "placed"));
        }
        for (Order cookingOrder : Restaurant.getInstance().getCookingOrders()) {
           orderList.add(makeOrderBox(cookingOrder, "cooking"));
        }
        for (Order readyOrder : Restaurant.getInstance().getReadyOrders()) {
            orderList.add(makeOrderBox(readyOrder, "ready"));
        }
        orderMasonryPane.getChildren().addAll(orderList);
    }
    //class orderBox(Order o, String)
    //TODO: make an internal class orderbox which holds the order itself and the Vbox representation.
    //then add orderbox.getVbox or something to the masonry pane
    //when you need to update the order, you can do Restaurant.makeEvent(orderBox.getOrder) which will update the sttus of the order in the backend
    //then you can just do refresh and it will update the order's color in the frontend

    private VBox makeOrderBox(Order o, String flag) {
        VBox orderBox = new VBox();
        orderBox.setOnMouseClicked(e -> selectBox(orderBox));
        //order header
        Label orderHeader = new Label("Order: " + o.getId() + "\nTable: " + o.getTableId());
        orderHeader.autosize();
        orderBox.getChildren().add(orderHeader);
        for (MenuItem item : o.getItems()) {
            orderBox.getChildren().add(makeItemLabel(item));
        }

        switch (flag) {
            case "placed":
                Background red = new Background(new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(red);
                break;
            case "cooking":
                Background yellow = new Background(new BackgroundFill(Color.web("#FFEE58"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(yellow);
                break;
            case "ready":
                Background green = new Background(new BackgroundFill(Color.web("#9CCC65"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(green);
                break;
            default:
                Background grey = new Background(new BackgroundFill(Color.web("#BDBDBD"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(grey);
                break;
        }
        orderBox.setPrefSize(100, 150);
        orderBox.autosize();
        return orderBox;
    }

    private Label makeItemLabel(MenuItem item) {
        Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
        itemLabel.setBackground(Background.EMPTY); //transparent background
        itemLabel.autosize();
        return itemLabel;
    }

    private void selectBox(VBox orderBox){
        selectedOrderBox = orderBox;
    }

    private void receiveOrder(){}
    private void fireOrder(){}
    private void readyOrder(){}
}
