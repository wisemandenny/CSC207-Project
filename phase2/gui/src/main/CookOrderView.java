package main;

import com.jfoenix.controls.JFXMasonryPane;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class CookOrderView implements Initializable, Observer {
    private ObservableList<Order> placedOrders;
    private ObservableList<Order> cookingOrders; //= Restaurant.getCookingOrders();
    private ObservableList<Order> readyOrders; //= Restaurant.getReadyOrders();

    @FXML
    private JFXMasonryPane orderMasonryPane;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        updateOrderSet();
    }

    private void initOrderSets(){}

    void updateOrderSet() {
        if (!orderMasonryPane.getChildren().isEmpty()) {
            orderMasonryPane.getChildren().clear();
        }
        for (Order o : Restaurant.getInstance().getPlacedOrders()){
            System.out.println(o.toString());
        }
        placedOrders = FXCollections.observableArrayList(Restaurant.getInstance().getPlacedOrders());
        cookingOrders = FXCollections.observableArrayList(Restaurant.getInstance().getCookingOrders());
        readyOrders = FXCollections.observableArrayList(Restaurant.getInstance().getReadyOrders());

        drawOrders();

    }

    private void drawOrders() {
        for (Order placedOrder : placedOrders) {
            orderMasonryPane.getChildren().add(makeOrderBox(placedOrder, "placed"));
        }
        for (Order cookingOrder : cookingOrders) {
            orderMasonryPane.getChildren().add(makeOrderBox(cookingOrder, "cooking"));
        }
        for (Order readyOrder : readyOrders) {
            orderMasonryPane.getChildren().add(makeOrderBox(readyOrder, "ready"));
        }
    }

    private VBox makeOrderBox(Order o, String flag) {
        VBox orderBox = new VBox(5);
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
        return orderBox;
    }

    private Label makeItemLabel(MenuItem item) {
        Label itemLabel = new Label(item.getQuantity() + " " + item.getName());
        itemLabel.setBackground(Background.EMPTY); //transparent background
        return itemLabel;
    }

    @Override
    public void update(Observable o, Object arg) {
        System.out.println("inside COV update");
        updateOrderSet();
    }

    //private void selectBox()

}
