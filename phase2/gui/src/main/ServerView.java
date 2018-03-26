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
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.*;
import menu.Ingredient;
import restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import java.util.ResourceBundle;

public class ServerView extends Observable implements Initializable, Observer{
    @FXML private VBox tableViewBox;
    @FXML private VBox menuVbox;
    @FXML private StackPane serverViewStackPane;
    @FXML private JFXButton FAB;

    private BillView billView;
    private DeliverableOrdersView deliverableOrdersView;

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

            FAB.setOnAction(e -> loadAddDialog(selectedItemButtons));
        } catch (Exception ex) {
            //TODO: add a logger
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
    private void newOrder(List<JFXButton> selectedItemButtons) {
        StringBuilder sb = new StringBuilder("order | table " + billView.getShownTable() + " > " + billView.getSelectedSeat() + " | ");
        for (JFXButton button : selectedItemButtons) {
            sb.append("1 " + button.getText() + ", ");
        }
        try {
            sb.delete(sb.lastIndexOf(", "), sb.length());
        } catch (StringIndexOutOfBoundsException ex) {
            System.out.println("make sure you select the item properly on the left :)");
        }
        Restaurant.getInstance().newEvent(sb.toString());
        try { //delay this thread to allow the backend to catch up
            Thread.sleep(300);
        }catch (InterruptedException ex){

        }
        setChanged();
        notifyObservers();
    }
    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }
    private HBox makeItemHBox(JFXButton button, JFXListView extras, JFXListView removed){
        HBox itemBox = new HBox();
        Region filler = new Region();
        HBox.setHgrow(filler, Priority.ALWAYS);
        MenuButton quantitySelector = new MenuButton("Select Quantity");
        for (int i = 1; i < 11; i++) {
            final int j = i;
            MenuItem quantity = new MenuItem(String.valueOf(j));
            quantity.setOnAction(e -> quantitySelector.setText(String.valueOf(j))); //will need more here
            quantitySelector.getItems().add(quantity);
        }
        itemBox.getChildren().addAll(quantitySelector, filler, new Label(button.getText()));
        itemBox.setOnMouseClicked(e -> loadModLists(Restaurant.getInstance().getMenu().getMenuItem(button.getText()), extras, removed));
        return itemBox;
    }

    private void loadModLists(menu.MenuItem item, JFXListView<JFXButton> extras, JFXListView<JFXButton> removed){
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
        removed.setItems(removeableIngredients);
    }
    private void loadAddDialog(List<JFXButton> selectedItemButtons){
        //HEADER
        JFXDialogLayout content = new JFXDialogLayout();
        Label orderDetailsDialogHeader = new Label("Order Details");


        VBox dialogRoot = new VBox(5);
        JFXListView<JFXButton> extraIngredientsListView = new JFXListView<>();
        JFXListView<JFXButton> removedIngredientsListView = new JFXListView<>();

        VBox top = new VBox();
        JFXListView<HBox> orderItemListView = new JFXListView<>();
        for (JFXButton button : selectedItemButtons){
            orderItemListView.getItems().add(makeItemHBox(button, extraIngredientsListView, removedIngredientsListView));
        }
        top.getChildren().add(orderItemListView);
        HBox bottom = new HBox();
        bottom.setMinSize(367, 355);
        VBox left = new VBox();
        left.setMinWidth(185);
        left.getChildren().add(extraIngredientsListView);

        VBox right = new VBox();
        right.setMinWidth(185);
        right.getChildren().add(removedIngredientsListView);

        bottom.getChildren().addAll(left, right);




        dialogRoot.getChildren().addAll(top, bottom);
        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton confirmButton = new JFXButton("Confirm Order");

        content.setHeading(orderDetailsDialogHeader);
        content.setBody(dialogRoot);
        content.setActions(cancelButton, confirmButton);

        JFXDialog dialog = new JFXDialog(serverViewStackPane, content, JFXDialog.DialogTransition.CENTER);
        cancelButton.setOnAction(e -> dialog.close());
        confirmButton.setOnAction(e -> {
            //newOrder
            dialog.close();
        });
        dialog.show();


    }

}
