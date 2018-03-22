package main;

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
import java.util.ResourceBundle;
import java.util.Set;

public class CookOrderView implements Initializable {
    @FXML
    private JFXMasonryPane orderMasonryPane;

    private Set<Order> placedOrders;// = Restaurant.getPlacedOrders();
    private Set<Order> cookingOrders; //= Restaurant.getCookingOrders();
    private Set<Order> readyOrders; //= Restaurant.getReadyOrders();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateOrderSet();
    }

    void updateOrderSet(){
        System.out.println("inside updateorder set");
        placedOrders = Restaurant.getPlacedOrders();
        cookingOrders = Restaurant.getCookingOrders();
        readyOrders = Restaurant.getReadyOrders();
        orderMasonryPane.getChildren().clear();
        drawOrders();
    }

    private void drawOrders(){
        orderMasonryPane.getChildren().clear();
        for(Order placedOrder : placedOrders){
            orderMasonryPane.getChildren().add(makeOrderBox(placedOrder, "placed"));
        }
        for(Order cookingOrder : cookingOrders){
            orderMasonryPane.getChildren().add(makeOrderBox(cookingOrder, "cooking"));
        }
        for(Order readyOrder : readyOrders){
            orderMasonryPane.getChildren().add(makeOrderBox(readyOrder, "ready"));
        }
    }

    private VBox makeOrderBox(Order o, String flag){
        VBox orderBox = new VBox(5);
        //order header
        orderBox.getChildren().add(new Label("Order: " + o.getId() + "\nTable: " + o.getTableId()));
        for(MenuItem item : o.getItems()){
            orderBox.getChildren().add(makeItemLabel(item));
        }

        switch(flag){
            case "placed":
                Background red = new Background( new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(red);
                break;
            case "cooking":
                Background yellow = new Background( new BackgroundFill(Color.web("#FFEE5"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(yellow);
                break;
            case "ready":
                Background green = new Background( new BackgroundFill(Color.web("#9CCC65"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(green);
                break;
            default:
                Background grey = new Background( new BackgroundFill(Color.web("#BDBDBD"), CornerRadii.EMPTY, Insets.EMPTY));
                orderBox.setBackground(grey);
                break;
        }
        return orderBox;
    }

    private Label makeItemLabel(MenuItem item){
        Label itemLabel = new Label(item.getQuantity() + " " +item.getName());
        itemLabel.setBackground(Background.EMPTY); //transparent background
        return itemLabel;
    }

    //private void selectBox()

}
