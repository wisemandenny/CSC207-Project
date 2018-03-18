package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TableView implements Initializable {
    @FXML
    private JFXListView<Label> tableOrderListView;

    @FXML
    private Label tableLabel;

    @FXML
    private JFXButton changeTableButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        int numOfTables = Restaurant.getNumOfTables();
        initPopup(numOfTables);
        changeTable(1);

        //display all of the ordered items for table 1.
        //later: color code according to status of item?
    }

    private void initPopup(int numOfTables) {
        VBox box = new VBox();
        for (int i = 1; i < numOfTables; i++) {
            final int tableNumber = i;
            JFXButton button = new JFXButton("Table " + tableNumber);
            button.setOnAction(e -> changeTable(tableNumber));
            button.setPadding(new Insets(10));
            box.getChildren().add(button);
        }
        JFXPopup popup = new JFXPopup(box);
        changeTableButton.setOnAction(e -> popup.show(changeTableButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    private void changeTable(int selectedTable){
        if(Restaurant.getTable(selectedTable).getOrders().size() > 0){
            tableOrderListView.getItems().clear();
            Table table = Restaurant.getTable(selectedTable);
            tableLabel.setText("Table #" + table.getId());
            List<Order> tableOrders = table.getOrders();
            int j = 1;
            for (Order o : tableOrders){
                tableOrderListView.getItems().add(new Label("Order "+ j++ + " (id# "+o.getId()+")"));

                for (MenuItem i: o.getItems()){
                    try{
                        Label lbl = new Label("     >"+ i.getQuantity() + " " + i.getName());
                        //set a graphic
                        tableOrderListView.getItems().add(lbl);
                    } catch (Exception ex) {
                        //log it
                    }
                }
            }
        } else {
            tableLabel.setText("Table #" + selectedTable);
            tableOrderListView.getItems().clear();
            tableOrderListView.getItems().add(new Label("No orders found for Table #" + selectedTable));
        }
    }
}
