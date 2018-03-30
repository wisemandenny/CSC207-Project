package main;

import com.jfoenix.controls.*;
import com.jfoenix.controls.datamodels.treetable.RecursiveTreeObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.layout.*;
import menu.Ingredient;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * The ManagerView class contains all elements useful for the manager to monitor their restaurant.
 * It also allows them to receive and request inventory shipments, and to view the inventory status at any time.
 * It also allows them to view any order in their restaurant depending on its status.
 * It also allows them to see financial totals (tip + total) for any given day.
 */
public class ManagerView extends Observable implements Initializable {
    //Root element
    @FXML StackPane rootStackPane;

    //Lists
    @FXML JFXListView<Label> placedList;
    @FXML JFXListView<Label> receivedList;
    @FXML JFXListView<Label> firedList;
    @FXML JFXListView<Label> readyList;
    @FXML JFXListView<Label> deliveredList;

    //Labels
    @FXML Label incomeLabel;
    @FXML Label tipLabel;

    //Buttons
    @FXML JFXButton shipmentButton;
    @FXML JFXButton emailButton;

    //Popups
    JFXDialog shipmentDialog;
    JFXDialog emailDialog;
    /**
     * Called to initialize a controller after its root element has been
     * completely processed.
     *
     * @param location  The location used to resolve relative paths for the root object, or
     *                  <tt>null</tt> if the location is not known.
     * @param resources The resources used to localize the root object, or <tt>null</tt> if
     */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        ShipmentPopup shipmentPopup = new ShipmentPopup();
        shipmentDialog = shipmentPopup.loadShipmentDetailsPopup();
        EmailPopup emailPopup = new EmailPopup();
        emailDialog = emailPopup.dialog;
        shipmentButton.setOnAction(e -> shipmentPopup.showPopup());
        emailButton.setOnAction(e -> emailPopup.showPopup());
        refresh();
    }

    /**
     * Refreshes all subcomponents of the manager view.
     */
    void refresh(){
        loadOrdersLists();
        loadIncomeLabels();
    }


    /**
     * Loads all orders from the backend into the manager view.
     */
    private void loadOrdersLists(){
        loadOrderList(Restaurant.getInstance().getPlacedOrders(), placedList);
        loadOrderList(Restaurant.getInstance().getReceivedOrders(), receivedList);
        loadOrderList(Restaurant.getInstance().getCookingOrders(), firedList);
        loadOrderList(Restaurant.getInstance().getReadyOrders(), readyList);
        loadOrderList(Restaurant.getInstance().getDeliveredOrders(), deliveredList);
    }

    /**
     * Loads all orders of a given type into a JFXListView component.
     * @param orderList the list of orders to be added
     * @param listView the JFXListView element to which the orders will be added.
     */
    private void loadOrderList(List<Order> orderList, JFXListView<Label> listView){
        ObservableList<Label> listViewItems = FXCollections.observableArrayList();
        for(Order o: orderList){
            listViewItems.add(generateOrderHeader(o));
            for(MenuItem i: o.getItems()){
                listViewItems.add(generateItemListEntry(i));
            }
        }
        if (listViewItems.isEmpty()) listViewItems.add(new Label("No orders found."));
        listView.setItems(listViewItems);
    }

    /**
     * Generates a  visual header for an order
     * @param o The order for which the header will be generated
     * @return a Label element representing the order.
     */
    private Label generateOrderHeader(Order o){
        Label header = new Label("Order " + o.getId());
        header.setBackground(Backgrounds.LIGHT_GREY_BACKGROUND);
        return header;
    }

    /**
     * Builds a label representing a given MenuItem.
     * @param i the MenuItem for which a label will be generated
     * @return the label containing the MenuItem's quantity, name, and any mods.
     */
    private Label generateItemListEntry(MenuItem i){
        StringBuilder labeltext = new StringBuilder(i.getQuantity() + " " + i.getName());
        if(!i.getExtraIngredients().isEmpty()){
            labeltext.append(" with extra ");
            for(Ingredient extra : i.getExtraIngredients()){
                labeltext.append(extra.getName()).append(", ");
            }
            labeltext.delete(labeltext.length()-2, labeltext.length());
        }
        if(!i.getRemovedIngredients().isEmpty()){
            labeltext.append(" with no ");
            for(Ingredient extra : i.getRemovedIngredients()){
                labeltext.append(extra.getName()).append(", ");
            }
            labeltext.delete(labeltext.length()-2, labeltext.length());
        }
        Label entry = new Label(labeltext.toString());
        return entry;
    }

    /**
     * Loads the total and tip total information into the label elements in the GUI.
     */
    private void loadIncomeLabels(){
        incomeLabel.setText("Today's income: $" + String.format("%.2f", Restaurant.getInstance().getDailyIncomeTotal()));
        tipLabel.setText("Today's tip total: $" + String.format("%.2f", Restaurant.getInstance().getTipTotal()));
    }

    /**
     * Loads the Inventory Viewer popup.
     */
    @FXML private void openInventoryViewer(){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Inventory viewer"));

        JFXButton okButton = new JFXButton("OK");
        content.setActions(okButton);

        InventoryViewer viewer = new InventoryViewer();
        content.setBody(viewer.getFlowPane());

        JFXDialog inventoryViewerPopup = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.CENTER, true);
        okButton.setOnAction(e -> inventoryViewerPopup.close());
        inventoryViewerPopup.show();
    }

    /**
     * This class contains the popup which appears when the manager clicks the "Receive Shipment" button.
     * It is designed to allow the manager of the restaurant to input received shipments into the inventory.
     */
    private class ShipmentPopup{
        JFXDialog dialog;
        private Map<Ingredient, Integer> receivedItems = new HashMap<>();

        /**
         * Loads the shipment dialog.
         */
        private ShipmentPopup(){
            dialog = loadShipmentDetailsPopup();
        }

        /**
         * Makes the popup, popup.
         */
        private void showPopup(){
            dialog.show();
        }

        /**
         * Constructs a shipment dialog object.
         * @return the JFXDialog component which will be displayed in the popup.
         */
        private JFXDialog loadShipmentDetailsPopup(){
            //HEADER
            JFXDialogLayout content = new JFXDialogLayout();
            Label shipmentDialogHeader = new Label("Receive a shipment");
            content.setHeading(shipmentDialogHeader);

            //BUTTONS
            JFXButton okButton = new JFXButton("OK");
            JFXButton cancelButton = new JFXButton("Cancel");
            content.setActions(cancelButton, okButton);

            //BODY
            JFXListView<HBox> shipmentItems = buildList();
            content.setBody(shipmentItems);

            JFXDialog popup = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.CENTER);
            cancelButton.setOnAction(e -> dialog.close());
            okButton.setOnAction(e -> {
                receiveShipment();
                setChanged();
                notifyObservers();
                dialog.close();
            });
            return popup;
        }

        /**
         * Builds the list of quantity selectors, and ingredient names which will be displayed in the popup.
         * @return a JFXListView object containing HBoxes which hold quantity selectors and the names of the ingredients.
         */
        private JFXListView<HBox> buildList(){
            ObservableList<HBox> listItems = FXCollections.observableArrayList();
            for(Ingredient i: Restaurant.getInstance().getMenu().getAllIngredients()){
                listItems.add(buildListEntry(i));
            }
            JFXListView<HBox> listView = new JFXListView<>();
            listView.setItems(listItems);
            return listView;
        }

        /**
         * Builds a list entry containing a quantity selector and the ingredient's name.
         * @param ingredient the Ingredient which will be displayed in this entry
         * @return the HBox containing the quantity selector and the ingredient's name
         */
        private HBox buildListEntry(Ingredient ingredient) {
            HBox entryBox = new HBox();
            Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);
            TextField quantityField = new TextField("0");
            quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
                if (!newValue.matches("\\d+")) { //if the input digit is not a digit
                    quantityField.setText(newValue.replaceAll("\\D+", ""));
                } else { // if it is a digit
                    receivedItems.put(ingredient, Integer.valueOf(newValue));
                }
            });
            quantityField.setMaxWidth(48); //text fields should fit at least 3 digits without text wrapping
            Label ingredientNameLabel = new Label(ingredient.getName());
            entryBox.getChildren().addAll(quantityField, filler, ingredientNameLabel);
            return entryBox;
        }

        /**
         * Sends the shipment event to the backend.
         */
        private void receiveShipment() {
            if(!receivedItems.values().isEmpty()){ //don't make events if no items have been received/input
                String shipmentEventString = buildShipmentEventString();
                Restaurant.getInstance().newEvent(shipmentEventString);
            }
        }

        /**
         * Builds the event string from the information from the dialog box.
         * @return the String which will be sent to the backend as an event.
         */
        private String buildShipmentEventString(){
            StringBuilder sb = new StringBuilder("receivedShipment | ");
            for (Map.Entry<Ingredient, Integer> entry : receivedItems.entrySet()){
                if(entry.getValue() > 0){
                    sb.append(entry.getValue()).append(" ");
                    sb.append(entry.getKey().getName()).append(", ");
                }
            }
            sb.delete(sb.length()-2, sb.length()); //delete trailing comma
            return sb.toString();
        }
    }

    /**
     * This class contains a popup which contains the current contents of "requests.txt", which contains all inventory
     * ingredients which have restock requests. This allows the manager to easily copy/paste the text straight into
     * an email without the cumbersome task of opening up a text file.
     */
    private class EmailPopup{
        JFXDialog dialog;
        TextArea emailTextArea = new TextArea();

        /**
         * Assigns the JFXDialog dialog variable to the result of loadEmailPopup(), which is used to actually build the
         * popup.
         */
        private EmailPopup(){
            dialog = loadEmailPopup();
        }

        /**
         * Shows the Email dialog.
         */
        private void showPopup(){
            updateTextArea();
            dialog.show();
        }

        /**
         * Updates the information in the email text field based on current inventory status.
         */
        private void updateTextArea(){
            try(BufferedReader br = new BufferedReader(new FileReader("requests.txt"))) {
                StringBuilder sb = new StringBuilder("No restocking necessary!");
                String line = br.readLine();
                if (line != null && line.length() != 0){
                    sb.delete(0, sb.length());
                    sb.append("Dear suppliers, \nPlease be advised that:");
                    do{
                        sb.append("\n");
                        sb.append(line);
                        line = br.readLine();
                    } while(br.ready());
                }
                emailTextArea.setText(sb.toString());
            } catch (IOException ex){
                RestaurantLogger.log(Level.WARNING, ex.toString());
            }
        }

        /**
         * Loads the elements of the dialog box into the dialog box.
         * @return the constructed email popup dialog box.
         */
        private JFXDialog loadEmailPopup(){
            //HEADER
            JFXDialogLayout content = new JFXDialogLayout();
            Label emailDialogHeader = new Label("Email to suppliers");
            content.setHeading(emailDialogHeader);

            //BUTTON
            JFXButton okButton = new JFXButton("OK");
            content.setActions(okButton);

            //BODY
            content.setBody(emailTextArea);

            JFXDialog popup = new JFXDialog(rootStackPane, content, JFXDialog.DialogTransition.CENTER, true);
            okButton.setOnAction(e -> popup.close());
            return popup;
        }
    }

    /***
     * This class contains a popup which displays the contents of the inventory.
     */
    private class InventoryViewer{
        private FlowPane flowPane;
        private Map<Ingredient, Integer> inventory;

        /**
         * Constructs a JFXTreeTableView for viewing the contents of the inventory.
         */
        InventoryViewer(){
            //ROOT COMPONENTS FOR THE TREETABLEVIEW
            inventory = Restaurant.getInstance().getInventory();
            flowPane = new FlowPane();
            JFXTreeTableView<InventoryEntry> treeView = new JFXTreeTableView<>();

            //COLUMN WHICH WILL HOLD THE INGREDIENTS' NAMES
            JFXTreeTableColumn<InventoryEntry, String> ingredientName = new JFXTreeTableColumn<>("Ingredient");
            ingredientName.setPrefWidth(150);
            ingredientName.setCellValueFactory(param -> param.getValue().getValue().ingredientName);

            //COLUMN WHICH WILL HOLD THE INGREDIENTS' QUANTITIES
            JFXTreeTableColumn<InventoryEntry, String> ingredientQuantity = new JFXTreeTableColumn<>("Quantity");
            ingredientQuantity.setPrefWidth(100);
            ingredientQuantity.setCellValueFactory(param -> param.getValue().getValue().quantity);

            //ADD THE NAMES/QUANTITIES TO THEIR RESPECTIVE COLUMNS
            ObservableList<InventoryEntry> entries = FXCollections.observableArrayList();
            for(Map.Entry<Ingredient, Integer> inventoryEntry : inventory.entrySet()){
                entries.add(new InventoryEntry(inventoryEntry.getKey().getName(), String.valueOf(inventoryEntry.getValue())));
            }

            //ADD THE COMPONENTS TO THE TREEVIEW AND DISPLAY IT
            final TreeItem<InventoryEntry> root = new RecursiveTreeItem<>(entries, RecursiveTreeObject::getChildren);
            treeView.setRoot(root);
            treeView.setShowRoot(false);
            treeView.getColumns().setAll(ingredientName, ingredientQuantity);
            flowPane.getChildren().add(treeView);
        }

        /**
         * Returns the root element of this popup.
         * @return the FlowPane root element of this popup.
         */
        private FlowPane getFlowPane() {
            return flowPane;
        }

        /**
         * This class represents a name/quantity pair entry in the Tree Table View.
         */
        private class InventoryEntry extends RecursiveTreeObject<InventoryEntry> {
            StringProperty ingredientName;
            StringProperty quantity;

            /**
             * Constructs a new InventoryEntry.
             * @param ingredientName the name of the ingredient
             * @param quantity the quantity of the ingredient.
             */
            private InventoryEntry(String ingredientName, String quantity) {
                this.ingredientName = new SimpleStringProperty(ingredientName);
                this.quantity = new SimpleStringProperty(quantity);
            }
        }
    }
}
