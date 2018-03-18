package main;

import com.jfoenix.controls.JFXListView;
import java.net.URL;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;

import java.util.List;
import java.util.ResourceBundle;

public class TableView implements Initializable {
    @FXML
    private JFXListView<Label> tableOrderListView;

    @FXML
    private Label tableLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        Table table = Restaurant.getTable(1);
        System.out.println("tableview intialize after get table from restaurant");
        tableLabel.setText("Table #" + table.getId());
        tableOrderListView.getItems();

        //display all of the ordered items for table 1.
        //later: color code according to status of item?

        List<Order> tableOrders = table.getOrders();
        for (Order o : tableOrders){
            tableOrderListView.getItems().add(new Label("Order "+ o.getId()));

            for (MenuItem i: o.getItems()){
                try{
                    Label lbl = new Label(i.getQuantity() + " " + i.getName());
                    //set a graphic
                    tableOrderListView.getItems().add(lbl);
                } catch (Exception ex) {
                    //log it
                }
            }
        }
    }
}
