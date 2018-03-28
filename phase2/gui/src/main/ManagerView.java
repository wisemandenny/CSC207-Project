package main;

import com.jfoenix.controls.JFXListView;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.StackPane;
import menu.Ingredient;
import menu.MenuItem;
import restaurant.Order;
import restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class ManagerView extends Observable implements Initializable {
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
        refresh();
        //setActionListeners();
    }

    private void loadOrdersLists(){
        loadOrderList(Restaurant.getInstance().getPlacedOrders(), placedList);
        loadOrderList(Restaurant.getInstance().getReceivedOrders(), receivedList);
        loadOrderList(Restaurant.getInstance().getCookingOrders(), firedList);
        loadOrderList(Restaurant.getInstance().getReadyOrders(), readyList);
        loadOrderList(Restaurant.getInstance().getDeliveredOrders(), deliveredList);
    }

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

    private Label generateOrderHeader(Order o){
        Label header = new Label("Order " + o.getId());
        header.setBackground(Backgrounds.LIGHT_GREY_BACKGROUND);
        return header;
    }

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

    private void loadIncomeLabels(){
        incomeLabel.setText("Today's income: $" + String.format("%.2f", Restaurant.getInstance().getDailyIncomeTotal()));
        tipLabel.setText("Today's tip total: $" + String.format("%.2f", Restaurant.getInstance().getTipTotal()));
    }

    void refresh(){
        loadOrdersLists();
        loadIncomeLabels();
    }

}
