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

    private void initChooseTablePopup(int numOfTables) {
        VBox box = new VBox();
        for (int i = 1; i < numOfTables; i++) {
            final int tableNumber = i;
            JFXButton button = new JFXButton("Table " + tableNumber);
            button.setOnAction(e -> changeTable(tableNumber));
            button.setPadding(new Insets(10));
            box.getChildren().add(button);
        }
        chooseTablePopup.setPopupContent(box);

        changeTableButton.setOnAction(e -> chooseTablePopup.show(changeTableButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    void refresh(){
        List<Label> items = new ArrayList<>(tableOrderListView.getItems());
        tableOrderListView.getItems().removeAll(items);
        tableOrderListView.getItems().addAll(items);
    }

    private void changeTable(int selectedTable){
        shownTable = selectedTable;
        tableOrderListView.getItems().clear();
        //List<Label> newItems = new ArrayList<>();
        if(!Restaurant.getTable(selectedTable).getOrders().isEmpty()){
            Table table = Restaurant.getTable(selectedTable);
            tableLabel.setText("Table #" + table.getId());
            int j = 1;
            for (Order o : table.getOrders()){
                Label lbl = new Label("Order "+ j++ + " (id# "+o.getId()+")");
                tableOrderListView.getItems().add(lbl);

                for (MenuItem i: o.getItems()){
                    try{
                        Label label = new Label("     >"+ i.getQuantity() + " " + i.getName());
                        //set a graphic
                        tableOrderListView.getItems().add(label);
                    } catch (Exception ex) {
                        //log it
                    }
                }
            }
        } else {
            System.out.println("inside changetable if case negative");
            tableLabel.setText("Table #" + selectedTable);
            tableOrderListView.getItems().add(new Label("No orders found for Table #" + selectedTable));
        }
        //ObservableList<Label> observableList = FXCollections.observableList(newItems);
        //tableOrderListView.setItems(observableList);
    }
}
