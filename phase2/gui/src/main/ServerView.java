package main;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

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
            FAB.setOnAction(e -> loadAddDialog(serverViewStackPane, menuList));

            //FAB.setOnAction(e -> loadAddDialog(serverViewStackPane, selectedItemButtons));
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

    javafx.scene.layout.StackPane getServerViewStackPane() {
        return serverViewStackPane;
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


}
