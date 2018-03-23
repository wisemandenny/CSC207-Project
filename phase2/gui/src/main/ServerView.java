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
import java.util.Observer;
import java.util.ResourceBundle;

public class ServerView implements Initializable, Observer {

    //private final JFXPopup newOrderPopup = new JFXPopup();

    @FXML
    private VBox menuVbox;

    @FXML
    private JFXButton FAB;

    private int displayedTableId;

    private BillView currentBillView;
    private CookOrderView currentCookOrderView;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            FXMLLoader menuListFXMLLoader = getFXMLLoader("MenuList.fxml");
            VBox box = menuListFXMLLoader.load();
            MenuList menuList = menuListFXMLLoader.getController();
            menuVbox.getChildren().add(box);


            FXMLLoader cookOrderViewFXMLLoader = getFXMLLoader("CookOrderView.fxml");
            cookOrderViewFXMLLoader.load();
            currentCookOrderView = cookOrderViewFXMLLoader.getController();

            FXMLLoader billViewFXMLLoader = getFXMLLoader("BillView.fxml");
            billViewFXMLLoader.load();
            currentBillView = billViewFXMLLoader.getController();


            List<JFXButton> selectedItemButtons = menuList.getSelectedItems();

            FAB.setOnAction(e -> newOrder(selectedItemButtons, currentBillView));

            //initNewOrderPopup(selectedItemButtons, tableView.getShownTable());
            //FAB.setOnAction(e -> newOrderPopup.show(FAB, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));


        } catch (Exception ex) {
            //add a logger event here
        }
    }

    int getDisplayedTableId() {
        return displayedTableId;
    }




    private void newOrder(List<JFXButton> selectedItemButtons, BillView billView) {
        StringBuilder sb = new StringBuilder("order | table " + billView.getShownTable() + " | ");

        for (JFXButton button : selectedItemButtons) {
            sb.append("1 " + button.getText() + ", ");
        }
        sb.delete(sb.lastIndexOf(", "), sb.length());
        Restaurant.getInstance().newEvent(sb.toString());

        //maybe add a dialog box to confirm the order is made?
    }

    public void update(Observable obs, Object obj){
        System.out.println("inside serverview update");
        currentCookOrderView.updateOrderSet();
    }

    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }
}
