package main;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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

    private VBox makeOrderBox(Order o, String flag) {
        VBox orderBox = new VBox();
        //order header
        orderBox.getChildren().add(new Label("Order: " + o.getId() + "\nTable: " + o.getTableId()));
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
        orderBox.setPrefSize(50, 50);
        return orderBox;
    }

    private Label makeItemLabel(MenuItem item) {
        Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
        System.out.println("makeItemLabel " + item.getName());
        itemLabel.setBackground(Background.EMPTY); //transparent background
        itemLabel.setAlignment(Pos.CENTER);
        return itemLabel;
    }

    //private void selectBox()

}
