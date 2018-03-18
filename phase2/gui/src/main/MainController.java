package main;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.VBox;

import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{

    @FXML
    private JFXHamburger menuHamburger;

    @FXML
    private JFXDrawer menuDrawer;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        try{
            VBox box= FXMLLoader.load(getClass().getResource("MenuList.fxml"));
            menuDrawer.setSidePane(box);
        }catch (Exception ex){}

        HamburgerBackArrowBasicTransition transition = new HamburgerBackArrowBasicTransition(menuHamburger);
        transition.setRate(-1);
        menuHamburger.addEventHandler(javafx.scene.input.MouseEvent.MOUSE_PRESSED,(e)->{
            transition.setRate(transition.getRate()*-1);
            transition.play();

            if(menuDrawer.isShown())
            {
                menuDrawer.close();
            }else
                menuDrawer.open();
        });
    }
}
