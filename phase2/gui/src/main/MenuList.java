package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuList implements Initializable{
    @FXML
    private JFXListView<Label> listView;
    @FXML
    private JFXButton showIngredientsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        addMenuLabels(false);
    }

    @FXML
    public void showIngredients(ActionEvent event){
        if(listView.isExpanded()){
            listView.setExpanded(false);
            listView.depthProperty().set(0);
            addMenuLabels(false);
        } else {
            listView.setExpanded(true);
            listView.depthProperty().set(1);
            addMenuLabels(true);
        }
    }

    private void addMenuLabels(boolean flag){
        listView.getItems().clear();
        Menu menu = Restaurant.getMenu();
        if(!flag){
            for (MenuItem i : menu.getMenu()){
                try{
                    Label lbl = new Label(i.getName());
                    listView.getItems().add(lbl);
                } catch (Exception ex){
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        } else {
            for (MenuItem i: menu.getMenu()){
                try{
                    Label itemLabel = new Label(i.getName());
                    listView.getItems().add(itemLabel);
                    for(Ingredient ingredient : i.getAllIngredients()){
                        listView.getItems().add(new Label("     > "+ingredient.getName()));
                    }
                } catch (Exception ex){
                    Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }
}
