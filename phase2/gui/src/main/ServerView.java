package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import menu.Ingredient;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

/**
 * This class contains all the views of the server: the bill view, the deliverable orders view, and the menu list.
 * The ServerView and its subcomponents allow the server to examine the menu (as well as items' prices and ingredients),
 * make orders with modifications, deliver items to tables, return items from tables to the kitchen, accept payment for
 * orders, receive tips, and join cheques. It also allows the addition and subtraction of seats at tables, as well as
 * resetting tables after the bill has been paid.
 */
public class ServerView extends Observable implements Initializable, Observer{
    @FXML private VBox tableViewBox;
    @FXML private VBox menuVbox;
    @FXML private StackPane serverViewStackPane;
    @FXML private JFXButton fab; //fab stands for floating action button

    //SUBCOMPONENTS
    private BillView billView;
    private DeliverableOrdersView deliverableOrdersView;
    private OrderDetailsPopup orderDetailsPopup;

    /**
     * {@inheritDoc}
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            //LOAD MENULIST & ITS CONTROLLER FROM FXML
            FXMLLoader menuListFXMLLoader = getFXMLLoader("MenuList.fxml");
            StackPane stackPane = menuListFXMLLoader.load();
            MenuList menuList = menuListFXMLLoader.getController();
            menuVbox.getChildren().add(stackPane);

            //LOAD DELIVERABLE ORDER VIEW & ITS CONTROLLER FROM FXML
            FXMLLoader dovLoader = getFXMLLoader("DeliverableOrdersView.fxml");
            StackPane dovList = dovLoader.load();
            deliverableOrdersView = dovLoader.getController();
            deliverableOrdersView.addObserver(this);
            menuVbox.getChildren().add(dovList);

            //LOAD BILL VIEW & ITS CONTROLLER FROM FXML
            FXMLLoader billViewFXMLLoader = getFXMLLoader("BillView.fxml");
            tableViewBox.getChildren().add( billViewFXMLLoader.load());
            billView = billViewFXMLLoader.getController();
            billView.addObserver(this);

            ObservableList<JFXButton> selectedItemButtons = menuList.getSelectedItems();
            fab.setOnAction(e -> {
                //do not allow a server place an order if there are orders waiting to be delivered
                if(deliverableOrdersView.hasOrders()) mustDeliverOrderWarning();
                //do not allow a server to place orders after the bills have been joined
                else if(Restaurant.getInstance().getTable(billView.getShownTable()).isJoined()){
                    noOrdersForJoinedTableWarning();
                }
                else if(!selectedItemButtons.isEmpty()) loadAddDialog(serverViewStackPane, menuList);
            });
            } catch (Exception ex) {
            RestaurantLogger.log(Level.WARNING, ex.toString());
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }

    /**
     * Refreshes the subcomponents of ServerView.
     */
    void refresh(){
        deliverableOrdersView.refresh();
        billView.refresh();
    }

    /**
     * Returns an FXMLLoader for a given filename.
     * @param source the String filename.
     * @return the FXML loader for that file.
     */
    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }

    /**
     * Loads the order details popup for the selected menuitems.
     * @param parent the StackPane parent the popup will be displayed onto.
     * @param menuList The MenuList object which contains the selected items for the order.
     */
    private void loadAddDialog(StackPane parent, MenuList menuList){
        List<JFXButton> selectedItemButtons = menuList.getSelectedItems();
        orderDetailsPopup = new OrderDetailsPopup(parent, selectedItemButtons, billView.getShownTable(), billView.getSelectedSeat());
        orderDetailsPopup.addObserver(this);
        orderDetailsPopup.loadAddDialog();
        menuList.clearSelected(); //deselect all selected items when you make a new order.
    }

    /**
     * Warns the server if the server tries to make a new order while there are orders waiting to be delivered.
     */
    private void mustDeliverOrderWarning(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("You must deliver all ready orders before placing new orders.");
        alert.showAndWait();
    }

    /**
     * Warns the server if they try to make a new order for a table that has had its bills joined.
     */
    private void noOrdersForJoinedTableWarning(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("After a table has been joined, no new orders can be added.");
        alert.showAndWait();
    }

    /**
     * This internal class contains the Order Details Popup, which appears after the user clicks the floating action
     * button after selecting items from the menu. It is used to make new orders.
     */
    private class OrderDetailsPopup extends Observable {
        private StackPane parent;
        private int tableId;
        private int seatId;

        private JFXButton cancelButton = new JFXButton("Cancel");
        private JFXButton confirmButton = new JFXButton("Confirm Order");

        List<JFXButton> selectedItemButtons;
        private List<menu.MenuItem> orderItems = new ArrayList<>();
        private menu.MenuItem currentlySelectedItem;

        /**
         * Constructs a new OrderDetailsPopup instance.
         * @param parent the StackPane parent element that this popup will be displayed onto.
         * @param selectedItemButtons the selected menuitems (in buttons) which will make up the order's items.
         * @param tableId the id of the table for the order
         * @param seatId the id of the seat for the order (0 is the "shared seat" for family-style items.
         */
        OrderDetailsPopup(StackPane parent, List<JFXButton> selectedItemButtons, int tableId, int seatId){
            this.parent = parent;
            this.tableId = tableId;
            this.seatId = seatId;
            this.selectedItemButtons = selectedItemButtons;
            for(JFXButton itemButton : selectedItemButtons){
                menu.MenuItem item = Restaurant.getInstance().getMenu().getMenuItem(itemButton.getText());
                orderItems.add(item);
            }
        }

        /**
         * Updates the quantity of a MenuItem from the information in a quantity selector.
         * @param dropdownMenu the quantity selector whose value was changed
         * @param quantity the new quantity
         * @param itemName the String of the MenuItem's name
         */
        private void changeItemQuantity(MenuButton dropdownMenu, int quantity, String itemName){
            dropdownMenu.setText(String.valueOf(quantity));
            menu.MenuItem updateItem = Restaurant.getInstance().getMenu().getMenuItem(itemName);
            orderItems.get(orderItems.indexOf(updateItem)).setQuantity(quantity);
            //Do not allow the server to place an order for items that do not have enough ingredients in inventory
            if(Restaurant.getInstance().checkInventory(orderItems)){
                confirmButton.setDisable(false);
            } else {
                confirmButton.setDisable(true);
            }
        }

        /**
         * Returns an HBox containing a quantity selector and the MenuItem's name.
         * @param button the button that was selected in the MenuList before the order details popup was created
         * @param extras the item's extra ingredients (ingredients which can be added to the menuitem)
         * @param removed the item's "remove" ingredients (ingredients which can be removed from the menuitem)
         * @return the HBox containing the item's quantity selector as well as the item's name
         */
        private HBox makeItemHBox(JFXButton button, JFXListView<HBox> extras, JFXListView<HBox> removed){
            HBox itemBox = new HBox();
            Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);
            //create the quantity selector
            MenuButton quantitySelector = new MenuButton(String.valueOf(1));
            for (int i = 1; i <= 10; i++) { //change 10 to whatever maximum number of items you want to do per order
                final int j = i;
                MenuItem quantity = new MenuItem(String.valueOf(j));
                quantity.setOnAction(e -> {
                    changeItemQuantity(quantitySelector, j, button.getText());
                    loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed);
                });
                quantitySelector.getItems().add(quantity);
            }
            itemBox.getChildren().addAll(quantitySelector, filler, new Label(button.getText()));
            itemBox.setOnMouseClicked(e -> loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed));
            return itemBox;
        }

        /**
         * Changes an HBox's color to green, red, or empty, depending on whether the ingredient is an extra, a remove,
         * and whether it is already selected or not.
         * @param clickedHbox the HBox that was clicked
         * @param isExtra true if the ingredient is an extra, false if it is a remove
         */
        private void selectEvent(HBox clickedHbox, boolean isExtra){
            if(isExtra) { //add the extra modifier
                if (clickedHbox.getBackground().equals(Backgrounds.GREEN_BACKGROUND)) { //if the item is already clicked
                    clickedHbox.setBackground(Background.EMPTY);
                    //remove the ingredient from the menu item's modifiers
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                } else if (clickedHbox.getBackground().equals(Background.EMPTY)){
                    clickedHbox.setBackground(Backgrounds.GREEN_BACKGROUND);
                    //add the ingredient from the menu item's modifiers
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                }
            } else { //add the removal modifier
                if (clickedHbox.getBackground().equals(Backgrounds.RED_BACKGROUND)){ //item is already clicked
                    clickedHbox.setBackground(Background.EMPTY);
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                } else if(clickedHbox.getBackground().equals(Background.EMPTY)){ //item is not clicked
                    clickedHbox.setBackground(Backgrounds.RED_BACKGROUND);
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                }
            }
        }

        /**
         * Loads the extras/removes for a given menuItem.
         * @param item the MenuItem to load the extras/removes for
         * @param extras JFXListView component holding extras
         * @param removed JFXListView component holding removes
         */
        private void loadModLists(menu.MenuItem item, JFXListView<HBox> extras, JFXListView<HBox> removed){
            currentlySelectedItem = item;
            List<Ingredient> allIngredients = new ArrayList<>(Restaurant.getInstance().getMenu().getAllIngredients());
            allIngredients.removeAll(item.getIngredients());

            ObservableList<HBox> addableIngredients = FXCollections.observableArrayList();
            extras.setItems(addableIngredients);

            for(Ingredient i : allIngredients){
                HBox addIngredient = buildHBox(i);
                addableIngredients.add(addIngredient);
            }

            ObservableList<HBox> removableIngredients = FXCollections.observableArrayList();
            removed.setItems(removableIngredients);
            for (Ingredient i : item.getIngredients()){
                HBox removeIngredient = buildHBox(i);
                removableIngredients.add(removeIngredient);
            }
            removed.setPrefHeight(extras.getHeight());

            extras.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            extras.setOnMouseClicked(e ->{
                ObservableList<HBox> selectedItems = extras.getSelectionModel().getSelectedItems();
                for(HBox box : selectedItems){
                    selectEvent(box, true);
                }
            });

            removed.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
            removed.setOnMouseClicked(e ->{
                ObservableList<HBox> selectedItems = removed.getSelectionModel().getSelectedItems();
                for(HBox box : selectedItems){
                    selectEvent(box, false);
                }
            });
        }

        /**
         * Loads the components of the Order Details dialog.
         */
        void loadAddDialog(){
            //HEADER
            JFXDialogLayout content = new JFXDialogLayout();
            Label orderDetailsDialogHeader = new Label("Order Details (click item for modifications)");

            //PARENT AND LIST COMPONENTS
            VBox dialogRoot = new VBox(5);
            JFXListView<HBox> extraIngredientsListView = new JFXListView<>();
            extraIngredientsListView.setMinHeight(300);
            JFXListView<HBox> removedIngredientsListView = new JFXListView<>();
            removedIngredientsListView.setMinHeight(300);

            //LIST OF SELECTED ITEMS AND THEIR QUANTITIES
            VBox top = new VBox();
            JFXListView<HBox> orderItemListView = new JFXListView<>();
            for (JFXButton button : selectedItemButtons){
                orderItemListView.getItems().add(makeItemHBox(button, extraIngredientsListView, removedIngredientsListView));
            }
            top.getChildren().add(orderItemListView);

            //MOD SELECTION
            HBox bottom = new HBox();
            bottom.setMinSize(367, 355);

            //EXTRAS
            VBox left = new VBox();
            left.setMinWidth(185);
            left.getChildren().add(new Text("Additions"));
            left.getChildren().add(extraIngredientsListView);

            //"REMOVES"
            VBox right = new VBox();
            right.setMinWidth(185);
            right.getChildren().add(new Text("Subtractions"));
            right.getChildren().add(removedIngredientsListView);

            //JOIN ALL COMPONENTS
            bottom.getChildren().addAll(left, right);
            dialogRoot.getChildren().addAll(top, bottom);
            content.setHeading(orderDetailsDialogHeader);
            content.setBody(dialogRoot);
            content.setActions(cancelButton, confirmButton);

            //BUILD THE DIALOG AND SET THE ACTIONS
            JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
            dialog.setMaxHeight(parent.getHeight()*0.9);
            cancelButton.setOnAction(e -> dialog.close());
            confirmButton.setOnAction(e -> {
                //newOrder
                Restaurant.getInstance().newEvent(buildOrderString());
                try{
                    Thread.sleep(300);
                } catch (InterruptedException ex){
                    RestaurantLogger.log(Level.SEVERE, ex.toString());
                }
                setChanged();
                notifyObservers();
                dialog.close();
            });
            dialog.show();
        }

        /**
         * Builds and returns a String which will represent this order event to be sent to the backend.
         * @return
         */
        private String buildOrderString(){
            StringBuilder sb = new StringBuilder("order | table " + tableId + " > " + seatId + " | ");
            for(menu.MenuItem item : orderItems){
                sb.append(item.getQuantity() + " " + item.getName());
                if(!item.getExtraIngredients().isEmpty() || !item.getRemovedIngredients().isEmpty()){ //if there are mods
                    sb.append(" / ");
                    boolean flag = false;
                    //if there are extras, append them to the string
                    if(!item.getExtraIngredients().isEmpty()){
                        for(Ingredient extra : item.getExtraIngredients()){
                            sb.append("+" + extra.getName() + " ");
                        }
                        flag = true;
                    }
                    //if there are removals, append them to the string
                    if(!item.getRemovedIngredients().isEmpty()){
                        for(Ingredient remove : item.getRemovedIngredients()){
                            sb.append("-" + remove.getName() + " ");
                        }
                        flag = true;
                    }
                    if (flag) //only delete trailing spaces if there were any mods
                        sb.deleteCharAt(sb.lastIndexOf(" "));
                }
                sb.append(", ");
            }
            sb.delete(sb.length()-2, sb.length()); //delete trailing comma
            return sb.toString();
        }

        /**
         * Builds an HBox representing a selectable ingredient in the mods lists.
         * @param i the Ingredient the box will contain
         * @return the selectable HBox representing the ingredient.
         */
        private HBox buildHBox(Ingredient i){
            HBox hbox = new HBox();
            hbox.setBackground(Background.EMPTY);
            hbox.getChildren().add(new Text(i.getName()));
            return hbox;
        }
    }
}
