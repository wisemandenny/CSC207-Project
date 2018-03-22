/*
package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXPopup;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class TableView implements Initializable {
    @FXML
    private JFXListView<Label> tableOrderListView;
    @FXML
    private Label tableLabel;
    @FXML
    private JFXButton changeTableButton;


    private JFXPopup chooseTablePopup = new JFXPopup();
    private JFXPopup newOrderPopup = new JFXPopup();

    private int shownTable = 1;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        //TODO: some issue here with the list view not refreshing. fix this later.
        initChooseTablePopup(Restaurant.getNumOfTables());
        final ObservableList<Label> items = FXCollections.observableArrayList();
        tableOrderListView.setItems(items);
        changeTable(shownTable);

        //display all of the ordered items for table 1.
        //later: color code according to status of item?
    }


    public int getShownTable(){
        return shownTable;
    }



    void refresh(){
        List<Label> items = new ArrayList<>(tableOrderListView.getItems());
        tableOrderListView.getItems().removeAll(items);
        tableOrderListView.getItems().addAll(items);
    }


}
*/
