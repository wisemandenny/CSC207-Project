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
import menu.Ingredient;
import menu.MenuItem;
import restaurant.*;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * This class contains the visual elements which show the bill.
 * It also contains buttons and popups which are used to return items and pay for items on the bill.
 */
public class BillView extends Observable implements Initializable, Observer {
    @FXML private VBox billViewRoot;
    @FXML private Label billHeader;
    @FXML private JFXListView<HBox> itemList;
    @FXML private JFXButton changeTableButton;

    private final ObservableList<HBox> tableItems = FXCollections.observableArrayList();
    private int shownTable = 1;
    private int selectedSeat = 0;
    private int selectedOrderId = -1;

    private final JFXPopup chooseTablePopup = new JFXPopup();
    private PayPopup payPopup = new PayPopup();

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        payPopup.addObserver(this);
        initChooseTablePopup(Restaurant.getInstance().getNumOfTables());
        itemList.setItems(tableItems);
        changeTable(shownTable);
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

    /**
     * Generates an empty horizontal box used to create a space between seat bills and information for the whole table.
     * @return the empty HBox element
     */
    private HBox generateSpacerBox(){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(filler);
        box.setBackground(Backgrounds.BLUE_GREY_BACKGROUND);
        return box;
    }

    /**
     * Generates the header for each seat's bill.
     * @param seatNumber the number of the seat
     * @return the HBox element which contains the seat's number.
     */
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

    /**
     * Generates the header for each order on the bill.
     * @param order The Order object
     * @param orderNumber The number of the order (this is a unique identifier)
     * @return the HBox containing the order header.
     */
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

    /**
     * Generates the visual list item for the bill. The item contains the item's name and price.
     * @param item The item to be displayed.
     * @return the HBox containing the item's information.
     */
    private HBox generateItemListEntry(MenuItem item) {
        HBox itemNameAndPrice = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        StringBuilder sb = new StringBuilder(item.getQuantity() + " " + item.getName());
        if(!item.getExtraIngredients().isEmpty()) {
            sb.append(" with extra ");
            for (Ingredient extra : item.getExtraIngredients()) {
                sb.append(extra.getName()).append(", ");
            }
            sb.delete(sb.length() - 2, sb.length()); //delete trailing commas
        }
        if(!item.getRemovedIngredients().isEmpty()){
            sb.append(" with no ");
            for(Ingredient extra : item.getRemovedIngredients()){
                sb.append(extra.getName()).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()); //delete trailing commas
        }
        JFXButton itemButton = new JFXButton(sb.toString());
        itemButton.setStyle("-fx-max-width: 400px");
        itemButton.setWrapText(true);
        itemNameAndPrice.getChildren().add(itemButton);
        itemNameAndPrice.getChildren().add(filler);
        itemNameAndPrice.getChildren().add(new JFXButton(String.format("%.2f", item.getQuantity() * item.getPrice())));
        return itemNameAndPrice;
    }

    /**
     * Generates the box containing subtotal information for the order.
     * @param amount the amount of the subtotal to be displayed
     * @return the HBox containing the subtotal information.
     */
    private HBox generateSubtotalBox(double amount) {
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Subtotal: "));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    /**
     * Generates the box containing the tax amount of the table's bill.
     * @param amount the tax amount
     * @return the HBox containing the tax amount.
     */
    private HBox generateTaxBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Tax (" + Restaurant.getInstance().getTaxRate()*100 +"%):"));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    /**
     * Generates the box containing the table's automatic gratuity information.
     * @param amount the autogratuity amount.
     * @return the HBox containing the table's autogratuity amount.
     */
    private HBox generateGratuityBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("AN AUTOMATIC GRATUITY OF "+ Restaurant.getInstance().getAutoGratRate() * 100 + "% HAS BEEN ADDED TO YOUR BILL"));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    /**
     * Generates the box containing the table's total.
     * @param amount the table's total.
     * @return the HBox containing the table's total information.
     */
    private HBox generateTotalBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Total: "));
        box.getChildren().add(filler);
        box.getChildren().add(new JFXButton(String.format("%.2f", amount)));
        return box;
    }

    /**
     * Generates the box containing the table's outstanding balance. This will be green if the balance is 0 and red
     * if it is greater than 0.
     * @param amount the table's outstanding balance.
     * @return the HBox containing the table's outstanding balance.
     */
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

    /**
     * Generates a box containing the table's tip amount.
     * @param amount the table's tip amount.
     * @return the HBox containing the table's tip amount.
     */
    private HBox generateTipBox(double amount){
        HBox box = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        box.getChildren().add(new Label("Tip: "));
        box.getChildren().add(filler);
        box.getChildren().add(new Label(String.format("%.2f", amount)));
        return box;
    }

    /**
     * Changes the selectedSeat field to the given seat number.
     * @param seatNumber the newly selected seat number.
     */
    private void selectSeat(int seatNumber){
        selectedSeat = seatNumber;
    }

    /**
     * Loads the popup which is used to change tables.
     * @param numOfTables the number of table's in the restaurant.
     */
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

    /**
     * Pauses the frontend thread for 300ms and then tells observers to refresh the GUI. This is necessary because the
     * backend does not process invites instantly.
     */
    private void letBackendCatchUp(){
        try{
            Thread.sleep(300);
        } catch (InterruptedException ex){
            RestaurantLogger.log(Level.SEVERE, ex.toString());
        }
        setChanged();
        notifyObservers();
    }

    /**
     * Changes the displayed table to the given number.
     * @param selectedTable the newly selected table.
     */
    private void changeTable(int selectedTable) {
        shownTable = selectedTable;
        refresh();

    }

    /**
     * Makes an alert if the user tries to submit a payment after the bill has been fully paid.
     */
    private void noPaymentAfterPaymentWarning(){
        Alert noReason = new Alert(Alert.AlertType.INFORMATION);
        noReason.setTitle("Error");
        noReason.setHeaderText(null);
        noReason.setContentText("Additional payment forbidden after payment has been accepted.");
        noReason.showAndWait();
    }

    /**
     * Makes an alert if the user tries to return an item from the bill after the bill has been fully paid.
     */
    private void noReturnsAfterPaymentWarning(){
        Alert noReason = new Alert(Alert.AlertType.INFORMATION);
        noReason.setTitle("Error");
        noReason.setHeaderText(null);
        noReason.setContentText("Returns forbidden after payment has been accepted.");
        noReason.showAndWait();
    }

    /**
     * Adds a seat to the currently displayed table.
     */
    @FXML private void addSeat(){
        Restaurant.getInstance().newEvent("addseat | table " + shownTable);
        letBackendCatchUp();
    }

    /**
     * Removes the selected seat from the currently selected table.
     * Will not remove the seat if there are unpaid items on it.
     */
    @FXML private void removeSeat(){
        Restaurant.getInstance().newEvent("removeseat | table " + shownTable + " | " + selectedSeat);
        letBackendCatchUp();
    }

    /**
     * Resets the displayed table if the bill has been fully paid.
     */
    @FXML private void clearTable(){
        Restaurant.getInstance().newEvent("clearTable | table " + shownTable);
        letBackendCatchUp();
    }

    /**
     * Joins all individual seats' bills on the table into one shared bill.
     */
    @FXML private void joinCheques(){
        Restaurant.getInstance().newEvent("join | table "+ shownTable);
        letBackendCatchUp();
    }

    /**
     * Opens the returns dialog in order to return an item(s) from the currently selected order.
     */
    @FXML private void sendBackOrder(){
        Order selectedOrder;
        if(Restaurant.getInstance().getTable(shownTable).getBill().getUnpaidAmount() == 0){
            noReturnsAfterPaymentWarning();
        } else{
            for(Order o: Restaurant.getInstance().getDeliveredOrders()){
                if(o.getId() == selectedOrderId){
                    selectedOrder = o;
                    ReturnPopup returnPopup = new ReturnPopup(((StackPane)billViewRoot.getParent().getParent().getParent()), selectedOrder);
                }
            }
        }
    }

    /**
     * Opens the payment dialog in order to pay for a selected bill.
     */
    @FXML private void paySelectedItems() {
        Table selectedTable = Restaurant.getInstance().getTable(shownTable);
        Table currentlySelectedSeat = selectedTable.getSeat(selectedSeat);
        if(selectedTable.getBill().getUnpaidAmount() == 0){
            noPaymentAfterPaymentWarning();
        } else {
            payPopup = new PayPopup();
            payPopup = payPopup.loadPayPopup((StackPane) billViewRoot.getParent().getParent().getParent(), this, selectedTable, currentlySelectedSeat);
        }
    }

    /**
     * Returns the currently displayed table ID.
     * @return the currently displayed table ID.
     */
    int getShownTable() {
        return shownTable;
    }

    /**
     * Returns the currently selected seat.
     * @return the currently selected seat (0 by default).
     */
    int getSelectedSeat() {
        return selectedSeat;
    }

    /**
     * Refreshes the BillView element. Redraws the bill and reloads all quantitative information into sub-components.
     */
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
     * This popup is used to take payment information. It has a textfield where the user can manually enter payment amount
     * or they can also use the mouse to enter with the number pad.
     */
    private class PayPopup extends Observable {
        StackPane parent;
        Table selectedTable;
        Table selectedSeat;
        TextField inputAmountField;

        PayPopup(){}

        /**
         * Returns a new instance of PayPopup.
         * @param parent the StackPane root which this dialog will display on.
         * @param bv the BillView that is the direct parent of this PayPopup
         * @param selectedTable The Table object which will receive the payment.
         * @param selectedSeat The Table object representing the seat which will receive the payment.
         */
        private PayPopup(StackPane parent, BillView bv, Table selectedTable, Table selectedSeat){
            this.parent = parent;
            this.addObserver(bv);
            this.selectedTable = selectedTable;
            this.selectedSeat = selectedSeat;
            loadPayPopup();
        }

        /**
         * Loads a PayPopup.
         * @param parent the StackPane root which this dialog will display on.
         * @param bv the BillView that is the direct parent of this PayPopup
         * @param selectedTable The Table object which will receive the payment.
         * @param selectedSeat The Table object representing the seat which will receive the payment.
         */
        private PayPopup loadPayPopup(StackPane parent, BillView bv, Table selectedTable, Table selectedSeat){
            return new PayPopup(parent, bv, selectedTable, selectedSeat);
        }

        /**
         * Loads the already instantiated PayPopup.
         */
        private void loadPayPopup(){
            JFXDialogLayout content = new JFXDialogLayout();
            content.setHeading(new Label("Pay"));

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

        /**
         * Builds the number pad the server will use to input their payment information.
         * @return The GridPane containing the number pad.
         */
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

        /**
         * Appends a digit to the entry text field. Will not append more than one decimal point.
         * @param appendChar
         */
        private void appendText(String appendChar){
            StringBuilder sb = new StringBuilder(inputAmountField.getText());
            if(!(appendChar.equals(".") && sb.toString().contains("."))){
                sb.append(appendChar);
            }
            inputAmountField.setText(sb.toString());
        }

        /**
         * Pays the selected amount for the selected order.
         */
        private void pay(){
            String payEventString = "pay | table " + selectedTable.getId() + " > " + selectedSeat.getId() + " | " +  inputAmountField.getText();
            Restaurant.getInstance().newEvent(payEventString);
            setChanged();
            notifyObservers();
        }
    }
    private class ReturnPopup {
        StackPane parent;
        Order order;
        List<TextField> commentList = new ArrayList<>();

        /**
         * Makes a new instance of ReturnPopup
         * @param parent the StackPane parent this popup will be displayed on.
         * @param order the order from which items will be returned.
         */
        ReturnPopup(StackPane parent, Order order){
            this.parent = parent;
            this.order = order;
            loadReturnPopup(order);
        }

        /**
         * Sets the header, body, and buttons of the popup.
         * @param order the Order from which items will be returned.
         */
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

        /**
         * Loads the MenuItems from the Order into the JFXListView which will display them
         * @param orderItemsListView the JFXListView which will display the MenuItems
         */
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

        /**
         * Makes the choice button (quantity selector) for a given MenuItem.
         * @param menuItem
         * @return the MenuButton quantity selector
         */
        private MenuButton makeQuantitySelector(menu.MenuItem menuItem){
            MenuButton quantitySelector = new MenuButton();
            for (int i = 0; i <= menuItem.getQuantity() ; i++) {
                final int j = i;
                javafx.scene.control.MenuItem quantity = new javafx.scene.control.MenuItem(String.valueOf(j));
                quantity.setOnAction(e -> {
                    changeQuantitySelector(quantitySelector, j, menuItem);
                });
                quantitySelector.getItems().add(quantity);
            }
            changeQuantitySelector(quantitySelector, menuItem.getQuantity(), menuItem);
            return quantitySelector;
        }

        /**
         * Changes the displayed number of the Quantity Selector and updates the Order with this new quantity.
         * @param quantitySelector the Quantity Selector that will be updated
         * @param newQuantity the new quantity which will be displayed on the quantity selector
         * @param changedItem the MenuItem whose quantity will be updated.
         */
        private void changeQuantitySelector(MenuButton quantitySelector, int newQuantity, MenuItem changedItem){
            quantitySelector.setText(String.valueOf(newQuantity));
            order.getItems().get(order.getItems().indexOf(changedItem)).setQuantity(newQuantity);
        }

        /**
         * Builds the String that will be sent to the backend.
         * @return the string that will be sent to the backend.
         */
        private String getReturnString(){
            StringBuilder sb = new StringBuilder("serverReturned | table "+order.getTableId()+ " > " + order.getSeatId() + " | ");
            for(MenuItem item : order.getItems()){
                if(item.getQuantity() > 0){
                    sb.append(item.getQuantity()).append(" ").append(item.getName()).append(", ");
                }
            }
            sb.delete(sb.length()-2, sb.length()); //remove trailing commas
            sb.append(" | ");
            for(TextField commentField : commentList){
                String reason = commentField.getText();
                if(reason.isEmpty()){ //if the user didn't input a reason
                    commentField.setBackground(Backgrounds.RED_BACKGROUND);
                }
                sb.append(reason).append(", ");
            }
            sb.delete(sb.length()-2, sb.length()); //remove trailing commas
            return sb.toString();
        }

        /**
         * Makes a popup if the user has not entered a reason for why they are returning the item.
         */
        private void missingReturnReasonWarning(){
            Alert noReason = new Alert(Alert.AlertType.INFORMATION);
            noReason.setTitle("Error");
            noReason.setHeaderText(null);
            noReason.setContentText("Please enter a reason for all returns.");
            noReason.showAndWait();
        }

        /**
         * When the user presses the OK button in the Return dialog, this method is called.
         * It validates the input of the dialog box.
         * @param dialog the dialog which the user has input information into. The information in this dialog
         *               will be validated.
         */
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
                missingReturnReasonWarning();
            }
        }
    }

}