package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServerView implements Initializable {

    //private final JFXPopup newOrderPopup = new JFXPopup();

    @FXML
    private VBox menuVbox;

    @FXML
    private JFXButton FAB;

    private int displayedTableId;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(("MenuList.fxml")));
            VBox box = fxmlLoader.load();
            MenuList menuList = fxmlLoader.getController();
            menuVbox.getChildren().add(box);

            FXMLLoader fxmlLoader2 = new FXMLLoader();
            fxmlLoader2.setLocation(getClass().getResource("BillView.fxml"));
            fxmlLoader2.load();
            BillView billView = fxmlLoader2.getController();

            List<JFXButton> selectedItemButtons = menuList.getSelectedItems();
            FAB.setOnAction(e -> newOrder(selectedItemButtons, billView));
            displayedTableId = billView.getShownTable();

            //initNewOrderPopup(selectedItemButtons, tableView.getShownTable());
            //FAB.setOnAction(e -> newOrderPopup.show(FAB, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));


        } catch (Exception ex) {
        }
    }

    int getDisplayedTableId(){
        return  displayedTableId;
    }

    private void newOrder(List<JFXButton> selectedItemButtons, BillView billView) {
        int tableId = billView.getShownTable();

        StringBuilder sb = new StringBuilder("order | table " + tableId + " | ");
        for (JFXButton button : selectedItemButtons) {
            System.out.println(button.getText());
            sb.append("1 " + button.getText() + ", ");
        }
        sb.delete(sb.lastIndexOf(", "), sb.length());
        Restaurant.newEvent(sb.toString());

        //maybe add a dialog box to confirm the order is made?

    }
    /*
    private void initNewOrderPopup(List<JFXButton> selectedItems, int tableId) {
        //JFXListView<Label> orderItems = new JFXListView<>();
        //orderItems.getItems().add(new Label("Test Label"));
        //orderItems.getItems().addAll(selectedItemLabels);
        try {
            VBox textInputBox = FXMLLoader.load(getClass().getResource("TextInputBox.fxml"));
            newOrderPopup.setPopupContent(textInputBox);
        } catch (Exception ex) {
            //add a logger event here
        }
    }*/
}
