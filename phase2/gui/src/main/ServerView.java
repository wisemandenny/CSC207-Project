package main;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import menu.Ingredient;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.net.URL;
import java.util.*;
import java.util.logging.Level;

public class ServerView extends Observable implements Initializable, Observer{
    @FXML private VBox tableViewBox;
    @FXML private VBox menuVbox;
    @FXML private StackPane serverViewStackPane;
    @FXML private JFXButton FAB;

    private BillView billView;
    private DeliverableOrdersView deliverableOrdersView;
    private OrderDetailsPopup orderDetailsPopup;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader menuListFXMLLoader = getFXMLLoader("MenuList.fxml");
            StackPane stackPane = menuListFXMLLoader.load();
            MenuList menuList = menuListFXMLLoader.getController();
            menuVbox.getChildren().add(stackPane);

            FXMLLoader dovLoader = getFXMLLoader("DeliverableOrdersView.fxml");
            StackPane dovList = dovLoader.load();
            deliverableOrdersView = dovLoader.getController();
            deliverableOrdersView.addObserver(this);
            menuVbox.getChildren().add(dovList);

            FXMLLoader billViewFXMLLoader = getFXMLLoader("BillView.fxml");
            tableViewBox.getChildren().add( billViewFXMLLoader.load());
            billView = billViewFXMLLoader.getController();
            billView.addObserver(this);

            List<JFXButton> selectedItemButtons = menuList.getSelectedItems();
            FAB.setOnAction(e -> {
                if(deliverableOrdersView.hasOrders()) mustDeliverOrderWarning();
                else if(Restaurant.getInstance().getTable(billView.getShownTable()).isJoined()){
                    noOrdersForJoinedTableWarning();
                }
                else if(!selectedItemButtons.isEmpty()) loadAddDialog(serverViewStackPane, menuList);
            });
            } catch (Exception ex) {
            RestaurantLogger.log(Level.WARNING, ex.toString());
        }
    }
    @Override
    public void update(Observable o, Object arg) {
        setChanged();
        notifyObservers();
    }
    void refresh(){
        deliverableOrdersView.refresh();
        billView.refresh();
    }
    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }
    private void loadAddDialog(StackPane parent, MenuList menuList){
        List<JFXButton> selectedItemButtons = menuList.getSelectedItems();

        orderDetailsPopup = new OrderDetailsPopup(parent, selectedItemButtons, billView.getShownTable(), billView.getSelectedSeat());
        orderDetailsPopup.addObserver(this);
        orderDetailsPopup.loadAddDialog();
        menuList.clearSelected(); //deselect all menuitems when you make a new order.
    }
    private void mustDeliverOrderWarning(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("You must deliver all ready orders before placing new orders.");
        alert.showAndWait();
    }
    private void noOrdersForJoinedTableWarning(){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText("After a table has been joined, no new orders can be added.");
        alert.showAndWait();
    }

    private class OrderDetailsPopup extends Observable {
        private StackPane parent;
        private int tableId;
        private int seatId;

        private JFXButton cancelButton = new JFXButton("Cancel");
        private JFXButton confirmButton = new JFXButton("Confirm Order");

        List<JFXButton> selectedItemButtons;
        private List<menu.MenuItem> orderItems = new ArrayList<>();
        private menu.MenuItem currentlySelectedItem;

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

        private void changeItemQuantity(MenuButton dropdownMenu, int quantity, String itemName){
            dropdownMenu.setText(String.valueOf(quantity));
            menu.MenuItem updateItem = Restaurant.getInstance().getMenu().getMenuItem(itemName);
            orderItems.get(orderItems.indexOf(updateItem)).setQuantity(quantity);
            if(Restaurant.getInstance().checkInventory(orderItems)){
                confirmButton.setDisable(false);
            } else {
                confirmButton.setDisable(true);
            }
        }

        private HBox makeItemHBox(JFXButton button, JFXListView<HBox> extras, JFXListView<HBox> removed){
            HBox itemBox = new HBox();
            Region filler = new Region();
            HBox.setHgrow(filler, Priority.ALWAYS);
            MenuButton quantitySelector = new MenuButton(String.valueOf(1));
            for (int i = 1; i < 11; i++) {
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
        private void selectEvent(HBox clickedHbox, boolean isExtra){
            JFXListCell<HBox> selectedCell = (JFXListCell<HBox>) clickedHbox.getParent();
            if(isExtra) { //add the extra modifier
                if (selectedCell.getBackground().equals(Backgrounds.GREEN_BACKGROUND)) { //if the item is already clicked
                    selectedCell.setBackground(Background.EMPTY);
                    clickedHbox.setBackground(Background.EMPTY);
                    //remove the ingredient from the menu item's modifiers
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                } else {
                    selectedCell.setBackground(Backgrounds.GREEN_BACKGROUND);
                    clickedHbox.setBackground(Backgrounds.GREEN_BACKGROUND);
                    //add the ingredient from the menu item's modifiers
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                }
            } else { //add the removal modifier
                if (selectedCell.getBackground().equals(Backgrounds.RED_BACKGROUND)){ //item is already clicked
                    selectedCell.setBackground(Background.EMPTY);
                    clickedHbox.setBackground(Background.EMPTY);
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                } else { //item is not clicked
                    selectedCell.setBackground(Backgrounds.RED_BACKGROUND);
                    clickedHbox.setBackground(Backgrounds.RED_BACKGROUND);
                    orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(((Text)clickedHbox.getChildren().get(0)).getText()));
                }
            }
        }
        private void loadModLists(menu.MenuItem item, JFXListView<HBox> extras, JFXListView<HBox> removed){
            currentlySelectedItem = item;

            List<Ingredient> allIngredients = new ArrayList<>(Restaurant.getInstance().getMenu().getAllIngredients());
            allIngredients.removeAll(item.getIngredients());
            ObservableList<HBox> addableIngredients = FXCollections.observableArrayList();
            for(Ingredient i : allIngredients){
                HBox addIngredient = buildHBox(i, true);
                addableIngredients.add(addIngredient);
            }
            extras.setItems(addableIngredients);

            ObservableList<HBox> removeableIngredients = FXCollections.observableArrayList();
            for (Ingredient i : item.getIngredients()){
                HBox removeIngredient = buildHBox(i, false);
                removeableIngredients.add(removeIngredient);
            }
            removed.setPrefHeight(extras.getHeight());
            removed.setItems(removeableIngredients);

        }
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

            //INGREDIENTS TO REMOVE
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
            dialog.setMaxHeight(parent.getHeight()*0.8);

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

        private String buildOrderString(){
            StringBuilder sb = new StringBuilder("order | table " + tableId + " > " + seatId + " | ");
            for(menu.MenuItem item : orderItems){
                sb.append(item.getQuantity() + " " + item.getName());
                if(!item.getExtraIngredients().isEmpty() || !item.getRemovedIngredients().isEmpty()){ //if there are mods
                    sb.append(" / ");
                    boolean flag = false;

                    if(!item.getExtraIngredients().isEmpty()){ //if there are extras, append them to the string
                        for(Ingredient extra : item.getExtraIngredients()){
                            sb.append("+" + extra.getName() + " ");
                        }
                        flag = true;
                    }

                    if(!item.getRemovedIngredients().isEmpty()){ //if there are removals, append them to the string
                        for(Ingredient remove : item.getRemovedIngredients()){
                            sb.append("-" + remove.getName() + " ");
                        }
                        flag = true;
                    }
                    if (flag)
                        sb.deleteCharAt(sb.lastIndexOf(" "));
                }
                sb.append(", ");
            }
            sb.delete(sb.length()-2, sb.length()); //delete trailing comma
            return sb.toString();
        }

        private HBox buildHBox(Ingredient i, boolean isExtra){
            HBox hbox = new HBox();
            hbox.getChildren().add(new Text(i.getName()));
            hbox.setOnMouseClicked(e -> selectEvent(hbox, isExtra));
            return hbox;
        }
    }
}
