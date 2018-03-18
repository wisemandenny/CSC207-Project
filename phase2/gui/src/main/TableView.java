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
        initPopup();
        changeTable(1);

        //display all of the ordered items for table 1.
        //later: color code according to status of item?
    }

    private void initPopup() {
        JFXButton b1 = new JFXButton("Table 1");
        b1.setOnAction(e -> changeTable(1));
        JFXButton b2 = new JFXButton("Table 2");
        b2.setOnAction(e -> changeTable(2));
        JFXButton b3 = new JFXButton("Tab;e 3");
        b3.setOnAction(e -> changeTable(3));
        b1.setPadding(new Insets(10));
        b2.setPadding(new Insets(10));
        b3.setPadding(new Insets(10));
        VBox box = new VBox(b1,b2,b3);
        JFXPopup popup = new JFXPopup(box);
        changeTableButton.setOnAction(e -> popup.show(changeTableButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    private void changeTable(int selectedTable){
        if(Restaurant.getTable(selectedTable) != null){
            tableOrderListView.getItems().clear();
            Table table = Restaurant.getTable(selectedTable);
            tableLabel.setText("Table #" + table.getId());
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
        } else {
            tableLabel.setText("Table #" + selectedTable);
            tableOrderListView.getItems().clear();
            tableOrderListView.getItems().add(new Label("Table #"+selectedTable+" not found."));
        }
    }
}
