package main;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.transitions.hamburger.HamburgerBackArrowBasicTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import menu.MenuItem;
import restaurant.Restaurant;

import javax.annotation.Resources;
import java.awt.event.MouseEvent;
import java.io.FileInputStream;
import java.net.URL;
import java.nio.file.Paths;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Controller implements Initializable{

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private JFXButton expandButton;

    @FXML
    private JFXHamburger menuHamburger;

    @FXML
    private JFXDrawer menuDrawer;

    @FXML
    private JFXListView<Label> listView;





    @Override
    public void initialize(URL url, ResourceBundle rb){
        List<MenuItem> menuItems = Restaurant.getMenu().getMenu();
        for(MenuItem item : menuItems){
            try {
                Label lbl = new Label(item.getName());
                lbl.setGraphic(new ImageView(new Image(new FileInputStream("./phase2/gui/src/resources/icons/hand.png"))));
                listView.getItems().add(lbl);
            } catch (Exception ex){
                Logger.getLogger(Controller.class.getName()).log(Level.SEVERE, null, ex);
            }
        }



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

    @FXML
    private void load(ActionEvent event){
        if(listView.isExpanded()){
            listView.setExpanded(false);
            listView.depthProperty().set(0);
        } else {
            listView.setExpanded(true);
            listView.depthProperty().set(1);
        }
    }

}
