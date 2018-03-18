package main;

import com.jfoenix.controls.*;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

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
        initNewOrderPopup();
        try{
            VBox box= FXMLLoader.load(getClass().getResource("MenuList.fxml"));
            menuDrawer.setSidePane(box);
            menuDrawer.open();

            //VBox tableViewBoxVBox = FXMLLoader.load(tableViewBox.getChildren().getClass().getResource("TableView.fxml"));
            //JFXPopup newOrderPopup = tableViewBoxVBox.getChildren()
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



        FAB.setOnAction(e -> newOrderPopup.show(FAB, JFXPopup.PopupVPosition.TOP, JFXPopup.PopupHPosition.RIGHT));

    }

    public void newOrder(){

        //Table currentTable = Restaurant.getTable(shownTable);
        //when the user clicks on a menu item from the drawer, then
    }



    private void initNewOrderPopup(){
        JFXListView<Label> orderItems = new JFXListView<>();
        orderItems.getItems().add(new Label("Test Label"));
        VBox box = new VBox(orderItems);
        newOrderPopup.setPopupContent(box);
    }
}
