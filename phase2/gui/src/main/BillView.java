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
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.Table;

import java.net.URL;
import java.util.ResourceBundle;

public class BillView implements Initializable {
    private static int shownTable = 1;

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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChooseTablePopup(Restaurant.getNumOfTables());
        //initialize observable lists to the jfxlistviews so they will refresh on changes
        ObservableList<HBox> tableItems = FXCollections.observableArrayList();
        itemList.setItems(tableItems);

        changeTable(BillView.shownTable);
    }

    private HBox generateOrderHeader(Order order, int orderNumber) {
        HBox orderBox = new HBox();
        orderBox.getChildren().add(new JFXButton("Order " + orderNumber + " (id: " + order.getId() + ")"));
        return orderBox;
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

    private HBox generateBox(double amount) {
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    private void drawTableBill(Table table) {
        int j = 1;
        for (Order order : table.getOrders()) {
            itemList.getItems().add(generateOrderHeader(order, j++)); //order header
            for (MenuItem item : order.getItems()) {
                itemList.getItems().add(generateItemListEntry(item));
            }
        }

        itemList.getItems().add(generateBox(table.getBill().getSubtotal()));
        itemList.getItems().add(generateBox(table.getBill().getTotal()));
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

    private void changeTable(int selectedTable) {
        BillView.shownTable = selectedTable;

        itemList.getItems().clear();
        if (!Restaurant.getTable(BillView.shownTable).getOrders().isEmpty()) {
            Table table = Restaurant.getTable(BillView.shownTable);
            billHeader.setText("BILL FOR TABLE " + table.getId());
            drawTableBill(table);
            //updatePaid(table); TODO: implement this
        } else {
            billHeader.setText("BILL FOR TABLE " + BillView.shownTable);
            HBox noOrderFoundBox = new HBox();
            noOrderFoundBox.getChildren().add(new Label("No orders found for Table #" + BillView.shownTable));
            itemList.getItems().add(noOrderFoundBox);
        }
        //ObservableList<Label> observableList = FXCollections.observableList(newItems);
        //tableOrderListView.setItems(observableList);
    }

    private void updatePaid(Table table, double paidAmount) {
        unpaidLabel.setText(String.format("%.2f", table.getBill().getTotal() - paidAmount));
        paidLabel.setText(String.format("%.2f", paidAmount));
    }


    public void paySelectedItems() {
        //ability to select items from the bill similar to menulist
    }

    int getShownTable() {
        return BillView.shownTable;
    }


}
