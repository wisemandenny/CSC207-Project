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
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class BillView implements Initializable {
    private int shownTable = 1;
    private int selectedSeat = 0;
    private static final Background GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#EEEEEE"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background LIGHT_GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY));

    private final JFXPopup chooseTablePopup = new JFXPopup();
    @FXML
    private Label billHeader;
    @FXML
    private JFXButton changeTableButton;
    @FXML
    private JFXListView<HBox> itemList;
    @FXML
    private JFXButton subtotalButton;
    @FXML
    private JFXButton totalButton;
    @FXML
    private Label unpaidLabel;
    @FXML
    private Label paidLabel;
    @FXML
    private JFXButton payButton;
    @FXML
    private JFXButton deleteButton;

    final ObservableList<HBox> tableItems = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChooseTablePopup(Restaurant.getInstance().getNumOfTables());
        itemList.setItems(tableItems);
        changeTable(shownTable);
    }

    private HBox generateSeatHeader(int seatNumber){
        HBox seatHeaderBox = new HBox();
        if (seatNumber == 0) { //"shared" seat (items that belong to the whole table
            seatHeaderBox.getChildren().add(new JFXButton("Shared Items"));
        } else {
            seatHeaderBox.getChildren().add(new JFXButton("Seat " + seatNumber));
        }
        seatHeaderBox.setBackground(GREY_BACKGROUND);
        seatHeaderBox.setOnMouseClicked(e -> selectSeat(seatNumber));
        return seatHeaderBox;
    }

    private HBox generateOrderHeader(Order order, int orderNumber) {
        HBox orderHeaderBox = new HBox();
        orderHeaderBox.getChildren().add(new JFXButton("Order " + orderNumber + " (id: " + order.getId() + ")"));
        orderHeaderBox.setBackground(LIGHT_GREY_BACKGROUND);
        return orderHeaderBox;
    }

    private HBox generateItemListEntry(MenuItem item) {
        HBox itemNameAndPrice = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        itemNameAndPrice.getChildren().add(new JFXButton(item.getQuantity() + " " + item.getName()));
        itemNameAndPrice.getChildren().add(filler);
        itemNameAndPrice.getChildren().add(new JFXButton(String.format("%.2f", item.getQuantity() * item.getPrice())));
        return itemNameAndPrice;
    }

    private HBox generateTotalBox(double amount) {
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    private void selectSeat(int seatNumber){
        selectedSeat = seatNumber;
    }

    private void initChooseTablePopup(int numOfTables) {
        VBox box = new VBox();
        for (int i = 1; i < numOfTables; i++) {
            int tableNumber = i;
            JFXButton button = new JFXButton("Table " + tableNumber);
            button.setOnAction(e -> changeTable(tableNumber));
            button.setPadding(new Insets(10));
            box.getChildren().add(button);
        }
        chooseTablePopup.setPopupContent(box);
        changeTableButton.setOnAction(e -> chooseTablePopup.show(changeTableButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    void changeTable(int selectedTable) {
        shownTable = selectedTable;
        refresh();
        //updatePaid() TODO: implement this
    }

    private void updatePaid(Table table, double paidAmount) {
        unpaidLabel.setText(String.format("%.2f", table.getBill().getTotal() - paidAmount));
        paidLabel.setText(String.format("%.2f", paidAmount));
    }
    public void paySelectedItems() {
        //ability to select items from the bill similar to menulist
    }
    int getShownTable() {
        return shownTable;
    }

    int getSelectedSeat() {
        return selectedSeat;
    }

    public void refresh(){
        tableItems.clear();
        billHeader.setText("BILL FOR TABLE " + shownTable);
        List<List<Order>> allTableOrders = Restaurant.getInstance().getTable(shownTable).getAllOrders();
        if(!allTableOrders.isEmpty()){
            int seatNumber = 0;
            for(List<Order> seatOrderList : allTableOrders){
                tableItems.add(generateSeatHeader(seatNumber++));
                int orderNumber = 1;
                double seatSubtotal = 0.0;
                for(Order order : seatOrderList){
                    tableItems.add(generateOrderHeader(order, orderNumber++));
                    for(MenuItem item: order.getItems()){
                        tableItems.add(generateItemListEntry(item));
                        seatSubtotal += item.getQuantity() * item.getPrice();
                    }
                }
                tableItems.add(generateTotalBox(seatSubtotal));
            }
        tableItems.add(generateTotalBox(Restaurant.getInstance().getTable(shownTable).getBill().getSubtotal()));
        tableItems.add(generateTotalBox(Restaurant.getInstance().getTable(shownTable).getBill().getTotal()));
        } else {
            HBox noOrderFoundBox = new HBox();
            noOrderFoundBox.getChildren().add(new Label("No orders found for Table #" + shownTable));
            tableItems.add(noOrderFoundBox);
        }
        itemList.setItems(tableItems);
    }
}