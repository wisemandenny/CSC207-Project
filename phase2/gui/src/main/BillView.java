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

    private JFXPopup chooseTablePopup = new JFXPopup();
    private int shownTable = 1;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChooseTablePopup(Restaurant.getNumOfTables());
        //initialize observable lists to the jfxlistviews so they will refresh on changes
        final ObservableList<HBox> tableItems = FXCollections.observableArrayList();
        itemList.setItems(tableItems);

        //draw the bill for the table
        changeTable(1);

        //load any elements that are already present in the tables order into the bill table.
        //use two lists: one list for items and the other for their prices
            //you will need a method similar to changetable (maybe showBill) which will update both lists if you choose to see your ingredients/mods
    }
    private HBox generateOrderHeader(Order order, int orderNumber){
        HBox orderBox = new HBox();
        orderBox.getChildren().add(new JFXButton("Order " + orderNumber));
        return orderBox;
    }
    private HBox generateItemListEntry(MenuItem item){
        HBox itemNameAndPrice = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        itemNameAndPrice.getChildren().add(new JFXButton(item.getQuantity() + " " + item.getName()));
        itemNameAndPrice.getChildren().add(filler);
        itemNameAndPrice.getChildren().add(new JFXButton(String.format("%.2f", item.getQuantity()*item.getPrice())));
        return itemNameAndPrice;
    }

    private void drawTableBill(Table table){
        int j = 1;
        for (Order order : table.getOrders()) {
            itemList.getItems().add(generateOrderHeader(order, j++)); //order header
            for (MenuItem item : order.getItems()) {
                itemList.getItems().add(generateItemListEntry(item));
            }
        }
    }

    private void initChooseTablePopup(int numOfTables) {
        VBox box = new VBox();
        for (int i = 1; i < numOfTables; i++) {
            final int tableNumber = i;
            JFXButton button = new JFXButton("Table " +tableNumber);
            button.setOnAction(e -> changeTable(tableNumber));
            button.setPadding(new Insets(10));
            box.getChildren().add(button);
        }
        chooseTablePopup.setPopupContent(box);

        changeTableButton.setOnAction(e -> chooseTablePopup.show(changeTableButton, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));
    }

    private void changeTable(int selectedTable){
        shownTable = selectedTable;
        itemList.getItems().clear();
        if(!Restaurant.getTable(selectedTable).getOrders().isEmpty()){
            Table table = Restaurant.getTable(selectedTable);
            billHeader.setText("BILL FOR TABLE " + table.getId());
            drawTableBill(table);
        } else {
            billHeader.setText("BILL FOR TABLE " + selectedTable);
            HBox noOrderFoundBox = new HBox();
            noOrderFoundBox.getChildren().add(new Label("No orders found for Table #" + selectedTable));
            itemList.getItems().add(noOrderFoundBox);
        }
        //ObservableList<Label> observableList = FXCollections.observableList(newItems);
        //tableOrderListView.setItems(observableList);
    }
    public void paySelectedItems(){}
    int getShownTable(){
        return shownTable;
    }


}
