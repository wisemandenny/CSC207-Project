package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;
import restaurant.Restaurant;

import java.net.URL;
import java.util.List;
import java.util.Observable;
import java.util.ResourceBundle;

public class ServerView extends Observable implements Initializable {
    @FXML
    private VBox tableViewBox;
    @FXML
    private VBox menuVbox;
    @FXML
    private JFXButton FAB;
    private BillView billView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader menuListFXMLLoader = getFXMLLoader("MenuList.fxml");
            VBox box = menuListFXMLLoader.load();
            MenuList menuList = menuListFXMLLoader.getController();
            menuVbox.getChildren().add(box);

            FXMLLoader billViewFXMLLoader = getFXMLLoader("BillView.fxml");
            tableViewBox.getChildren().add( billViewFXMLLoader.load());
            billView = billViewFXMLLoader.getController();

            List<JFXButton> selectedItemButtons = menuList.getSelectedItems();

            FAB.setOnAction(e -> newOrder(selectedItemButtons));
        } catch (Exception ex) {
            //TODO: add a logger
        }
    }

    private void newOrder(List<JFXButton> selectedItemButtons) {
        StringBuilder sb = new StringBuilder("order | table " + billView.getShownTable() + " | ");
        for (JFXButton button : selectedItemButtons) {
            sb.append("1 " + button.getText() + ", ");
        }
        sb.delete(sb.lastIndexOf(", "), sb.length());
        Restaurant.getInstance().newEvent(sb.toString());
        setChanged();
        notifyObservers();
        //TODO: add a dialog box to confirm the order is made?
    }

    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }

    void refresh(){
        billView.refresh();
    }
}
