package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;

import java.util.ArrayList;
import java.util.List;

class ReturnPopup {
    private static final Background RED_BACKGROUND = new Background(new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));

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
            confirmReturn();
            dialog.close();
        });
        dialog.show();

    }
    private void loadOrderItemList(JFXListView<HBox> orderItemsListView){
        ObservableList<HBox> orderItemsListViewEntries = FXCollections.observableArrayList();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        for(MenuItem item : order.getItems()){
            HBox entryBox = new HBox();
            entryBox.getChildren().add(makeQuantitySelector(item));
            entryBox.getChildren().add(new Label(item.getName()));//can add the mod info here
            entryBox.getChildren().add(filler);
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
                commentField.setBackground(RED_BACKGROUND);
            }
            sb.append(reason).append(", ");
        }
        sb.delete(sb.length()-2, sb.length()); //remove trailing commas
        return sb.toString();
    }
    private void confirmReturn(){
        boolean flag = true;
        for(TextField field : commentList){
            if(field.getBackground().equals(RED_BACKGROUND)){
                if(!field.getText().isEmpty()){
                    field.setBackground(Background.EMPTY);
                } else {
                    flag = false;
                    Alert noReason = new Alert(Alert.AlertType.INFORMATION);
                    noReason.setTitle("Error");
                    noReason.setHeaderText(null);
                    noReason.setContentText("Please enter a reason for all returns.");
                    noReason.showAndWait();
                }
            }
        }
        if(flag){
            Restaurant.getInstance().newEvent(getReturnString());
        }
    }
}
