package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.controls.JFXScrollPane;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServerView implements Initializable {

    private final JFXPopup newOrderPopup = new JFXPopup();
    @FXML
    private JFXHamburger menuHamburger;
    @FXML
    private JFXScrollPane menuScrollPane;
    @FXML
    private VBox tableViewBox;
    @FXML
    private JFXButton FAB;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(("MenuList.fxml")));
            VBox box = fxmlLoader.load();
            MenuList menuList = fxmlLoader.getController();
            menuScrollPane.setContent(box);

            fxmlLoader.setLocation(getClass().getResource("TableView.fxml"));
            TableView tableView = fxmlLoader.getController();


            List<JFXButton> selectedItemButtons = menuList.getSelectedItems();
            initNewOrderPopup(selectedItemButtons, tableView.getShownTable());

            FAB.setOnAction(e -> newOrder(menuList.getSelectedItems(), tableView.getShownTable()));
            //FAB.setOnAction(e -> newOrderPopup.show(FAB, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));


        } catch (Exception ex) {
        }

    }

    private void newOrder(List<JFXButton> selectedItemButtons, int tableId) {
        StringBuilder sb = new StringBuilder("order | table " + tableId + " | ");
        for (JFXButton button : selectedItemButtons) {
            sb.append("1 " + button.getText() + ", ");
        }
        System.out.println(sb.toString());
        Restaurant.newEvent(sb.toString());

    }

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
    }
}
