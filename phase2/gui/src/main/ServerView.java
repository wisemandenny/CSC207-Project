package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXPopup;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ServerView implements Initializable{

    @FXML
    private JFXHamburger menuHamburger;

    @FXML
    private JFXDrawer menuDrawer;

    @FXML
    private VBox tableViewBox;

    @FXML
    private JFXButton FAB;

    private JFXPopup newOrderPopup = new JFXPopup();

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource(("MenuList.fxml")));
            VBox box = fxmlLoader.load();
            MenuList menuList = fxmlLoader.getController();

            List<Label> selectedItemLabels = menuList.getSelectedItems();
            initNewOrderPopup(selectedItemLabels);

            menuDrawer.setSidePane(box);
            menuDrawer.open();

        }catch (Exception ex){}

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuHamburger);
        transition.setRate(-1);
        menuHamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED,( e -> {
            transition.setRate(transition.getRate()*-1);
            transition.play();

            if(menuDrawer.isShown())
            {
                menuDrawer.close();
            }else
                menuDrawer.open();
        }));



        FAB.setOnAction(e -> newOrderPopup.show(FAB, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.LEFT));

    }

    public void newOrder(){

        //Table currentTable = Restaurant.getTable(shownTable);
        //when the user clicks on a menu item from the drawer, then
    }



    private void initNewOrderPopup(List<Label> selectedItemLabels){
        //JFXListView<Label> orderItems = new JFXListView<>();
        //orderItems.getItems().add(new Label("Test Label"));
        //orderItems.getItems().addAll(selectedItemLabels);
        try{
            VBox textInputBox = FXMLLoader.load(getClass().getResource("TextInputBox.fxml"));
            newOrderPopup.setPopupContent(textInputBox);
        } catch (Exception ex){
            //add a logger event here
        }
    }
}
