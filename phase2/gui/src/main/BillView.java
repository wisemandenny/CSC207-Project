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
import java.util.Observable;
import java.util.ResourceBundle;

public class BillView extends Observable implements Initializable {
    private static final Background GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#EEEEEE"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background LIGHT_GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY));
    private static final Background BLUE_GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#607D8B"), CornerRadii.EMPTY, Insets.EMPTY));

    @FXML private VBox billViewRoot;
    @FXML private Label billHeader;
    @FXML private JFXButton changeTableButton;
    @FXML private JFXListView<HBox> itemList;
    @FXML private JFXButton subtotalButton;
    @FXML private JFXButton totalButton;
    @FXML private Label unpaidLabel;
    @FXML private Label paidLabel;
    @FXML private JFXButton payButton;
    @FXML private JFXButton deleteButton;
    @FXML private JFXButton addSeat;
    @FXML private JFXButton removeSeat;
    @FXML private JFXButton joinButton;


    final ObservableList<HBox> tableItems = FXCollections.observableArrayList();
    private int shownTable = 1;
    private int selectedSeat = 0;
    private final JFXPopup chooseTablePopup = new JFXPopup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        initChooseTablePopup(Restaurant.getInstance().getNumOfTables());
        itemList.setItems(tableItems);
        changeTable(shownTable);
    }
    private HBox generateSpacerBox(){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(filler);
        filler.setBackground(BLUE_GREY_BACKGROUND);
        return box;
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
    private HBox generateSubtotalBox(double amount) {
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Subtotal: "));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }
    private HBox generateTaxBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Tax (" + Restaurant.getInstance().getTaxRate()*100 +"%):"));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }
    private HBox generateGratuityBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("AN AUTOMATIC GRATUITY OF "+ Restaurant.getInstance().getAutoGratRate() * 100 + "% HAS BEEN ADDED TO YOUR BILL"));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }
    private HBox generateTotalBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Total: "));
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
    private void updatePaid(Table table, double paidAmount) {
        unpaidLabel.setText(String.format("%.2f", table.getBill().getTotal() - paidAmount));
        paidLabel.setText(String.format("%.2f", paidAmount));
    }
    @FXML private void addSeat(){
        Restaurant.getInstance().newEvent("addseat | table " + shownTable);
        letBackendCatchUp();
    }
    @FXML private void removeSeat(){
        Restaurant.getInstance().newEvent("removeseat | table " + shownTable + " | " + selectedSeat);
        letBackendCatchUp();
    }
    @FXML private void joinCheques(){
        Restaurant.getInstance().newEvent("join | table "+ shownTable);
        letBackendCatchUp();
    }

    private void letBackendCatchUp(){
        try{
            Thread.sleep(300);
        } catch (InterruptedException ex){
            //TODO: log this excpetion
        }
        setChanged();
        notifyObservers();
    }
    void changeTable(int selectedTable) {
        shownTable = selectedTable;
        refresh();
        //updatePaid() TODO: implement this
    }
    int getShownTable() {
        return shownTable;
    }
    int getSelectedSeat() {
        return selectedSeat;
    }
    public void paySelectedItems() {
        PayPopup payPopup = new PayPopup((StackPane)billViewRoot.getParent().getParent().getParent(), Restaurant.getInstance().getTable(shownTable).getSeat(selectedSeat));
        //ability to select items from the bill similar to menulist
    }

    public void refresh(){
        tableItems.clear();
        billHeader.setText("BILL FOR TABLE " + shownTable);
        Table selectedTable = Restaurant.getInstance().getTable(shownTable);
        List<List<Order>> allTableOrders = selectedTable.getAllOrders();
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
                tableItems.add(generateSubtotalBox(seatSubtotal));
            }

            tableItems.add(generateSpacerBox());
            tableItems.add(generateSubtotalBox(selectedTable.getBill().getSubtotal()));
            tableItems.add(generateTaxBox(selectedTable.getBill().getTaxAmount()));
            if (selectedTable.getAutogratuityAmount() > 0) {
                tableItems.add(generateGratuityBox(selectedTable.getAutogratuityAmount()));
            }
            tableItems.add(generateTotalBox(selectedTable.getBill().getTotal() + selectedTable.getAutogratuityAmount()));
        } else {
            HBox noOrderFoundBox = new HBox();
            noOrderFoundBox.getChildren().add(new Label("No orders found for Table #" + shownTable));
            tableItems.add(noOrderFoundBox);
        }
        itemList.setItems(tableItems);
    }
}