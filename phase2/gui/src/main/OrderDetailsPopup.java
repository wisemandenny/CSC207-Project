package main;

import com.jfoenix.controls.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import menu.Ingredient;
import restaurant.Restaurant;
import restaurant.RestaurantLogger;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.logging.Level;

class OrderDetailsPopup extends Observable {
    private StackPane parent;
    private int tableId;
    private int seatId;

    private JFXButton cancelButton = new JFXButton("Cancel");
    private JFXButton confirmButton = new JFXButton("Confirm Order");

    List<JFXButton> selectedItemButtons;
    private List<menu.MenuItem> orderItems = new ArrayList<>();
    private menu.MenuItem currentlySelectedItem;

    //there is a cuurrent selected item. that is the one with the loaded  mod lists.
    //set the items quantity every time you change the dropdown menu

    //every time an ingredient is selected in the left list, add it to the item
    //every time an ingredient is selected in the right list, remove it from the item

    OrderDetailsPopup(StackPane parent, List<JFXButton> selectedItemButtons, int tableId, int seatId){
        this.parent = parent;
        this.tableId = tableId;
        this.seatId = seatId;
        this.selectedItemButtons = selectedItemButtons;
        for(JFXButton itemButton : selectedItemButtons){
            menu.MenuItem item = Restaurant.getInstance().getMenu().getMenuItem(itemButton.getText());
            orderItems.add(item);
           // allSelectedExtras.add(item.g);
        }
    }

    private void changeItemQuantity(MenuButton dropdownMenu, int quantity, String itemName){
        dropdownMenu.setText(String.valueOf(quantity));
        menu.MenuItem updateItem = Restaurant.getInstance().getMenu().getMenuItem(itemName);
        orderItems.get(orderItems.indexOf(updateItem)).setQuantity(quantity);
        if(Restaurant.getInstance().checkInventory(orderItems)){
            confirmButton.setDisable(true);
        } else {
            confirmButton.setDisable(false);
        }
    }

    private HBox makeItemHBox(JFXButton button, JFXListView<Hyperlink> extras, JFXListView<Hyperlink> removed){
        HBox itemBox = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        MenuButton quantitySelector = new MenuButton("1");
        for (int i = 1; i < 11; i++) {
            final int j = i;
            MenuItem quantity = new MenuItem(String.valueOf(j));
            quantity.setOnAction(e -> {
                changeItemQuantity(quantitySelector, j, button.getText());
                loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed);
            }); //will need more here
            quantitySelector.getItems().add(quantity);
        }
        itemBox.getChildren().addAll(quantitySelector, filler, new Label(button.getText()));

        itemBox.setOnMouseClicked(e -> loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed));
        return itemBox;
    }
    private void selectEvent(Hyperlink clickedHyperlink, boolean isExtra){
        JFXListCell<Hyperlink> selectedCell = (JFXListCell<Hyperlink>) clickedHyperlink.getParent();
        if(isExtra) { //add the modifier
            if (selectedCell.getBackground().equals(Backgrounds.GREEN_BACKGROUND)) {
                selectedCell.setBackground(Background.EMPTY);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            } else {
                selectedCell.setBackground(Backgrounds.GREEN_BACKGROUND);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getExtraIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            }
        } else { //remove the modifier
            if (selectedCell.getBackground().equals(Backgrounds.RED_BACKGROUND)){ //item is already clicked
                selectedCell.setBackground(Background.EMPTY);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().remove(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            } else { //item is not clicked
                selectedCell.setBackground(Backgrounds.RED_BACKGROUND);
                clickedHyperlink.setBackground(Background.EMPTY);
                orderItems.get(orderItems.indexOf(currentlySelectedItem)).getRemovedIngredients().add(Restaurant.getInstance().getMenu().getMenuIngredient(clickedHyperlink.getText()));
            }
        }
    }
    private void loadModLists(menu.MenuItem item, JFXListView<Hyperlink> extras, JFXListView<Hyperlink> removed){
        currentlySelectedItem = item;

        List<Ingredient> allIngredients = Restaurant.getInstance().getMenu().getAllIngredients();
        allIngredients.removeAll(item.getIngredients());
        ObservableList<Hyperlink> addableIngredients = FXCollections.observableArrayList();
        for(Ingredient i : allIngredients){
            Hyperlink addIngredient = new Hyperlink(i.getName());
            addIngredient.setOnMousePressed(e -> selectEvent(addIngredient, true));
            addableIngredients.add(addIngredient);
        }
        extras.setItems(addableIngredients);

        ObservableList<Hyperlink> removeableIngredients = FXCollections.observableArrayList();
        for (Ingredient i : item.getIngredients()){
            Hyperlink removeIngredient = new Hyperlink(i.getName());
            removeIngredient.setOnMousePressed(e -> selectEvent(removeIngredient, false));
            removeableIngredients.add(removeIngredient);
        }
        removed.setPrefHeight(extras.getHeight());
        removed.setItems(removeableIngredients);
    }
    void loadAddDialog(){
        //HEADER
        JFXDialogLayout content = new JFXDialogLayout();
        Label orderDetailsDialogHeader = new Label("Order Details");

        //PARENT AND LIST COMPONENTS
        VBox dialogRoot = new VBox(5);
        JFXListView<Hyperlink> extraIngredientsListView = new JFXListView<>();
        extraIngredientsListView.setMinHeight(300);
        JFXListView<Hyperlink> removedIngredientsListView = new JFXListView<>();
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
        left.getChildren().add(extraIngredientsListView);

        //INGREDIENTS TO REMOVE
        VBox right = new VBox();
        right.setMinWidth(185);
        right.getChildren().add(removedIngredientsListView);

        //JOIN ALL COMPONENTS
        bottom.getChildren().addAll(left, right);
        dialogRoot.getChildren().addAll(top, bottom);
        content.setHeading(orderDetailsDialogHeader);
        content.setBody(dialogRoot);
        content.setActions(cancelButton, confirmButton);

        //BUILD THE DIALOG AND SET THE ACTIONS
        JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);

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
            if(!item.getExtraIngredients().isEmpty() || !item.getRemovedIngredients().isEmpty()){
                sb.append(" / ");
                boolean flag = false;
                if(!item.getExtraIngredients().isEmpty()){
                    for(Ingredient extra : item.getExtraIngredients()){
                        sb.append("+" + extra.getName() + " ");
                    }
                    flag = true;
                    //sb.deleteCharAt(sb.lastIndexOf(" "));
                }
                if(!item.getRemovedIngredients().isEmpty()){
                    for(Ingredient remove : item.getRemovedIngredients()){
                        sb.append("-" + remove.getName() + " ");
                    }
                    flag = true;
                    //sb.deleteCharAt(sb.lastIndexOf(" "));
                }
                if (flag)
                    sb.deleteCharAt(sb.lastIndexOf(" "));
            }
            sb.append(", ");
        }
        sb.delete(sb.length()-2, sb.length());
        return sb.toString();
    }
}
