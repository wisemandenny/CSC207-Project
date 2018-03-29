package main;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import menu.MenuItem;
import restaurant.*;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class BillView extends Observable implements Initializable, Observer {
    @FXML private VBox billViewRoot;
    @FXML private Label billHeader;
    @FXML private JFXListView<HBox> itemList;
    @FXML private JFXButton changeTableButton;
    @FXML private JFXButton clearTableButton;
    @FXML private JFXButton subtotalButton;
    @FXML private JFXButton totalButton;
    @FXML private JFXButton payButton;
    @FXML private JFXButton returnButton;
    @FXML private JFXButton addSeat;
    @FXML private JFXButton removeSeat;
    @FXML private JFXButton joinButton;


    final ObservableList<HBox> tableItems = FXCollections.observableArrayList();
    private int shownTable = 1;
    private int selectedSeat = 0;
    private int selectedOrderId = -1;

    private final JFXPopup chooseTablePopup = new JFXPopup();
    private PayPopup payPopup = new PayPopup();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payPopup.addObserver(this);
        initChooseTablePopup(Restaurant.getInstance().getNumOfTables());
        itemList.setItems(tableItems);
        changeTable(shownTable);
    }
    private HBox generateSpacerBox(){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(filler);
        box.setBackground(Backgrounds.BLUE_GREY_BACKGROUND);
        return box;
    }
    private HBox generateSeatHeader(int seatNumber){
        HBox seatHeaderBox = new HBox();
        if (seatNumber == 0) { //"shared" seat (items that belong to the whole table
            seatHeaderBox.getChildren().add(new JFXButton("Shared Items"));
        } else {
            seatHeaderBox.getChildren().add(new JFXButton("Seat " + seatNumber));
        }
        seatHeaderBox.setBackground(Backgrounds.GREY_BACKGROUND);
        seatHeaderBox.setOnMouseClicked(e -> selectSeat(seatNumber));
        return seatHeaderBox;
    }
    private HBox generateOrderHeader(Order order, int orderNumber) {
        HBox orderHeaderBox = new HBox();
        orderHeaderBox.getChildren().add(new JFXButton("Order " + orderNumber + " (id: " + order.getId() + ")"));
        orderHeaderBox.setBackground(Backgrounds.LIGHT_GREY_BACKGROUND);
        orderHeaderBox.setOnMouseClicked(e -> {
            if(orderHeaderBox.getBackground().equals(Backgrounds.LIGHT_GREY_BACKGROUND)){ //click on an order box that is not already selected and no other order has been clicked on previously
                if(selectedOrderId == -1){
                    selectedOrderId = order.getId();
                    orderHeaderBox.setBackground(Backgrounds.SELECTED_BACKGROUND);
                }
            } else {
                selectedOrderId = -1;
                orderHeaderBox.setBackground(Backgrounds.LIGHT_GREY_BACKGROUND);
            }
        });
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
    private HBox generateBalanceBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label(" Balance due:"));
        box.getChildren().add(filler);
        box.getChildren().add(new Label(String.format("%.2f", amount)));
        if(amount > 0){ //positive balance means that there is still money owed.
            box.setBackground(Backgrounds.RED_BACKGROUND);
        } else {
            box.setBackground(Backgrounds.GREEN_BACKGROUND);
        }
        return box;
    }
    private HBox generateTipBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Tip: "));
        box.getChildren().add(filler);
        box.getChildren().add(new Label(String.format("%.2f", amount)));
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
    @FXML private void addSeat(){
        Restaurant.getInstance().newEvent("addseat | table " + shownTable);
        letBackendCatchUp();
    }
    @FXML private void removeSeat(){
        Restaurant.getInstance().newEvent("removeseat | table " + shownTable + " | " + selectedSeat);
        letBackendCatchUp();
    }
    @FXML private void clearTable(){
        Restaurant.getInstance().newEvent("clearTable | table " + shownTable);
        letBackendCatchUp();
    }
    @FXML private void joinCheques(){
        Restaurant.getInstance().newEvent("join | table "+ shownTable);
        letBackendCatchUp();
    }
    @FXML private void sendBackOrder(){
        Order selectedOrder;
        for(Order o: Restaurant.getInstance().getDeliveredOrders()){
            if(o.getId() == selectedOrderId){
                selectedOrder = o;
                System.out.println("match found");
                ReturnPopup returnPopup = new ReturnPopup(((StackPane)billViewRoot.getParent().getParent().getParent()), selectedOrder);
            }
        }

    }

    private void letBackendCatchUp(){
        try{
            Thread.sleep(300);
        } catch (InterruptedException ex){
            RestaurantLogger.log(Level.SEVERE, ex.toString());
        }
        setChanged();
        notifyObservers();
    }
    private void changeTable(int selectedTable) {
        shownTable = selectedTable;
        refresh();

    }
    int getShownTable() {
        return shownTable;
    }
    int getSelectedSeat() {
        return selectedSeat;
    }
    public void paySelectedItems() {
        Table selectedTable = Restaurant.getInstance().getTable(shownTable);
        Table currentlySelectedSeat = selectedTable.getSeat(selectedSeat);
        payPopup = new PayPopup();
        payPopup = payPopup.loadPayPopup((StackPane)billViewRoot.getParent().getParent().getParent(), this, selectedTable, currentlySelectedSeat);
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
            Bill bill = selectedTable.getBill();
            tableItems.add(generateSpacerBox());
            tableItems.add(generateSubtotalBox(bill.getSubtotal()));
            tableItems.add(generateTaxBox(bill.getTaxAmount()));
            if (selectedTable.getAutogratuityAmount() > 0) {
                tableItems.add(generateGratuityBox(selectedTable.getAutogratuityAmount()));
            }
            tableItems.add(generateTotalBox(bill.getTotal() + selectedTable.getAutogratuityAmount()));
            tableItems.add(generateBalanceBox(bill.getTotal() + selectedTable.getAutogratuityAmount() - bill.getPaidAmount()));
            tableItems.add(generateTipBox(bill.getTipAmount()));
        } else {
            HBox noOrderFoundBox = new HBox();
            noOrderFoundBox.getChildren().add(new Label("No orders found for Table #" + shownTable));
            tableItems.add(noOrderFoundBox);
        }
        itemList.setItems(tableItems);
    }

    /**
     * This method is called whenever the observed object is changed. An
     * application calls an <tt>Observable</tt> object's
     * <code>notifyObservers</code> method to have all the object's
     * observers notified of the change.
     *
     * @param o   the observable object.
     * @param arg an argument passed to the <code>notifyObservers</code>
     */
    @Override
    public void update(Observable o, Object arg) {
        letBackendCatchUp();
    }

    private class PayPopup extends Observable {
        StackPane parent;
        Table selectedTable;
        Table selectedSeat;
        TextField inputAmountField;

        PayPopup(){};

        private PayPopup(StackPane parent, BillView bv, Table selectedTable, Table selectedSeat){
            this.parent = parent;
            this.addObserver(bv);
            this.selectedTable = selectedTable;
            this.selectedSeat = selectedSeat;
            loadPayPopup(selectedSeat);
        }
        private PayPopup loadPayPopup(StackPane parent, BillView bv, Table selectedTable, Table selectedSeat){
            return new PayPopup(parent, bv, selectedTable, selectedSeat);
        }

        private void loadPayPopup(Table selectedSeat){
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label("Pay for order"));

            VBox container = new VBox();
            inputAmountField = new TextField();
            inputAmountField.setPromptText("Enter amount");
            container.getChildren().addAll(inputAmountField, buildNumPad());
            content.setBody(container);

            JFXButton cancelButton = new JFXButton("Cancel");
            JFXButton okButton = new JFXButton("OK");
            content.setActions(cancelButton, okButton);
            content.autosize();

            JFXDialog payPopup = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
            payPopup.setMaxWidth(150);

            cancelButton.setOnAction(e -> payPopup.close());
            okButton.setOnAction(e -> {
                pay();
                payPopup.close();
            });
            payPopup.show();
        }

        private GridPane buildNumPad(){
            GridPane gridPane = new GridPane();

            for (int i = 1; i <= 3 ; i++) {
                final int j = i;
                JFXButton numberButton = new JFXButton(String.valueOf(j));
                numberButton.setOnAction(e -> appendText(String.valueOf(j)));
                GridPane.setRowIndex(numberButton, 1);
                GridPane.setColumnIndex(numberButton, j);
                gridPane.getChildren().add(numberButton);
            }
            for (int i = 4; i <= 6 ; i++) {
                final int j = i;
                JFXButton numberButton = new JFXButton(String.valueOf(i));
                numberButton.setOnAction(e -> appendText(String.valueOf(j)));
                GridPane.setRowIndex(numberButton, 2);
                GridPane.setColumnIndex(numberButton, i-3);
                gridPane.getChildren().add(numberButton);

            }  for (int i = 7; i <= 9 ; i++) {
                final int j = i;
                JFXButton numberButton = new JFXButton(String.valueOf(i));
                numberButton.setOnAction(e -> appendText(String.valueOf(j)));
                GridPane.setRowIndex(numberButton, 3);
                GridPane.setColumnIndex(numberButton, i-6);
                gridPane.getChildren().add(numberButton);
            }
            JFXButton dotButton = new JFXButton(".");
            dotButton.setOnAction(e -> appendText("."));
            gridPane.add(dotButton, 1, 4); // column=1 row=0
            JFXButton zeroButton = new JFXButton("0");
            zeroButton.setOnAction(e -> appendText(String.valueOf(0)));
            gridPane.add(zeroButton, 2, 4);
            JFXButton clearButton = new JFXButton("C");
            clearButton.setOnAction(e -> inputAmountField.clear());
            gridPane.add(clearButton, 3, 4);
            return gridPane;
        }

        private void appendText(String appendChar){
            StringBuilder sb = new StringBuilder(inputAmountField.getText());
            if(!(appendChar.equals(".") && sb.toString().contains("."))){
                sb.append(appendChar);
            }
            inputAmountField.setText(sb.toString());
        }
        private void pay(){
            String payEventString = "pay | table " + selectedTable.getId() + " > " + selectedSeat.getId() + " | " +  inputAmountField.getText(); // pay | table 1 > 1 | 45.54
            Restaurant.getInstance().newEvent(payEventString);
            setChanged();
            notifyObservers();
        }
    }
    private class ReturnPopup {
        StackPane parent;
        Order order;
        List<TextField> commentList = new ArrayList<>();

        ReturnPopup(StackPane parent, Order order){
            this.parent = parent;
            this.order = order;
            loadReturnPopup(order);
        }

        private void loadReturnPopup(Order order){
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label("Return items from Order " + order.getId()));

            JFXButton cancelButton = new JFXButton("Cancel");
            JFXButton okButton = new JFXButton("OK");
            content.setActions(cancelButton, okButton);

            JFXListView<HBox> orderItemsListView = new JFXListView<>();
            loadOrderItemList(orderItemsListView);
            content.setBody(orderItemsListView);

            JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
            cancelButton.setOnAction(e -> dialog.close());
            okButton.setOnAction(e -> {
                confirmReturn(dialog);
            });
            dialog.show();

        }
        private void loadOrderItemList(JFXListView<HBox> orderItemsListView){
            ObservableList<HBox> orderItemsListViewEntries = FXCollections.observableArrayList();

            for(MenuItem item : order.getItems()){
                Region filler = new Region();
                Region filler2 = new Region();
                HBox.setHgrow(filler, Priority.ALWAYS);
                HBox.setHgrow(filler2, Priority.ALWAYS);
                HBox entryBox = new HBox();
                entryBox.getChildren().addAll(makeQuantitySelector(item), filler, new Label(item.getName()), filler2);
                TextField commentField = new TextField("Return reason");
                commentList.add(commentField);
                entryBox.getChildren().add(commentField);
                orderItemsListViewEntries.add(entryBox);
            }
            orderItemsListView.setItems(orderItemsListViewEntries);
        }
        private MenuButton makeQuantitySelector(menu.MenuItem menuItem){
            MenuButton quantitySelector = new MenuButton();
            for (int i = 0; i <= menuItem.getQuantity() ; i++) {
                final int j = i;
                javafx.scene.control.MenuItem quantity = new javafx.scene.control.MenuItem(String.valueOf(j));
                quantity.setOnAction(e -> {
                    changeQuantitySelector(quantitySelector, j, menuItem);
                    //refresh?
                });
                quantitySelector.getItems().add(quantity);
            }
            changeQuantitySelector(quantitySelector, menuItem.getQuantity(), menuItem);
            return quantitySelector;
        }
        private void changeQuantitySelector(MenuButton quantitySelector, int newQuantity, MenuItem changedItem){
            quantitySelector.setText(String.valueOf(newQuantity));
            order.getItems().get(order.getItems().indexOf(changedItem)).setQuantity(newQuantity);
        }
        private String  getReturnString(){
            StringBuilder sb = new StringBuilder("serverReturned | table "+order.getTableId()+ " > " + order.getSeatId() + " | ");
            for(MenuItem item : order.getItems()){
                if(item.getQuantity() > 0){
                    sb.append(item.getQuantity()).append(" ").append(item.getName()).append(", "); //1 Hamburg
                }
            }
            sb.delete(sb.length()-2, sb.length()); //remove trailing commas
            sb.append(" | ");
            for(TextField commentField : commentList){
                String reason = commentField.getText();
                if(reason.isEmpty()){
                    commentField.setBackground(Backgrounds.RED_BACKGROUND);
                }
                sb.append(reason).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()); //remove trailing commas
            return sb.toString();
        }
        private void confirmReturn(JFXDialog dialog){
            boolean flag = true;
            for(TextField field : commentList){
                if(!field.getText().isEmpty() && !field.getText().equals("Return reason")){
                    field.setBackground(Background.EMPTY);
                } else {
                    field.setBackground(Backgrounds.RED_BACKGROUND);
                    flag = false;
                }
            }
            if(flag){
                Restaurant.getInstance().newEvent(getReturnString());
                dialog.close();
                letBackendCatchUp();
            }else{
                Alert noReason = new Alert(Alert.AlertType.INFORMATION);
                noReason.setTitle("Error");
                noReason.setHeaderText(null);
                noReason.setContentText("Please enter a reason for all returns.");
                noReason.showAndWait();
            }
        }
    }

}