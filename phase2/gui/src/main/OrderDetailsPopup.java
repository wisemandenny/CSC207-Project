package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.*;
import menu.Ingredient;
import restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

class OrderDetailsPopup {
    StackPane parent;
    List<menu.MenuItem> orderItems = new ArrayList<>();
    menu.MenuItem currentlySelectedItem;
    List<Ingredient> currentlySelectedItemExtras;
    List<Ingredient> currentlySelectedItemRemoves;

    //there is a cuurrent selected item. that is the one with the loaded  mod lists.
    //set the items quantity every time you change the dropdown menu

    //every time an ingredient is selected in the left list, add it to the item
    //every time an ingredient is selected in the right list, remove it from the item

    OrderDetailsPopup(StackPane parent, List<JFXButton> selectedItemButtons){
        this.parent = parent;
        loadAddDialog(selectedItemButtons);
        for(JFXButton itemButton : selectedItemButtons){
            orderItems.add(Restaurant.getInstance().getMenu().getMenuItem(itemButton.getText()));
        }
    }

    private void changeItemQuantity(MenuButton dropdownMenu, int quantity, String itemName){
        dropdownMenu.setText(String.valueOf(quantity));
        menu.MenuItem updateItem = Restaurant.getInstance().getMenu().getMenuItem(itemName);
        orderItems.get(orderItems.indexOf(updateItem)).setQuantity(quantity);
    }

    //private void changeItemExtras(menu.MenuItem item, )

    private HBox makeItemHBox(JFXButton button, JFXListView extras, JFXListView removed){
        HBox itemBox = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        MenuButton quantitySelector = new MenuButton("Select Quantity");
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

    private void loadModLists(menu.MenuItem item, JFXListView<JFXButton> extras, JFXListView<JFXButton> removed){
        currentlySelectedItem = item;

        List<Ingredient> allIngredients = Restaurant.getInstance().getMenu().getAllIngredients();
        allIngredients.removeAll(item.getIngredients());
        ObservableList<JFXButton> addableIngredients = FXCollections.observableArrayList();
        for(Ingredient i : allIngredients){
            addableIngredients.add(new JFXButton(i.getName()));
        }
        extras.setItems(addableIngredients);

        ObservableList<JFXButton> removeableIngredients = FXCollections.observableArrayList();
        for (Ingredient i : item.getIngredients()){
            removeableIngredients.add(new JFXButton(i.getName()));
        }
        removed.setPrefHeight(extras.getHeight());
        removed.setItems(removeableIngredients);

        extras.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println("ListView selection changed from oldValue = "
                    + oldValue + " to newValue = " + newValue);
        });
        removed.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            ObservableList<JFXButton> selectedItems = removed.getSelectionModel().getSelectedItems();
            for(JFXButton button : selectedItems){
                System.out.println("selected item: " + button);
            }
        });
    }
    private void loadAddDialog(List<JFXButton> selectedItemButtons){
        //HEADER
        JFXDialogLayout content = new JFXDialogLayout();
        Label orderDetailsDialogHeader = new Label("Order Details");

        //PARENT AND LIST COMPONENTS
        VBox dialogRoot = new VBox(5);
        dialogRoot.setMinHeight(700);
        JFXListView<JFXButton> extraIngredientsListView = new JFXListView<>();
        extraIngredientsListView.setMinHeight(300);
        JFXListView<JFXButton> removedIngredientsListView = new JFXListView<>();
        removedIngredientsListView.setMinHeight(300);
        extraIngredientsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        removedIngredientsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);


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
        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton confirmButton = new JFXButton("Confirm Order");
        content.setHeading(orderDetailsDialogHeader);
        content.setBody(dialogRoot);
        content.setActions(cancelButton, confirmButton);

        //BUILD THE DIALOG AND SET THE ACTIONS
        JFXDialog dialog = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
        cancelButton.setOnAction(e -> dialog.close());
        confirmButton.setOnAction(e -> {
            //newOrder
            dialog.close();
        });
        dialog.show();
    }
}
