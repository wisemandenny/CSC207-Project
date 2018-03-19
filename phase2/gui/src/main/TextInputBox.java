package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ResourceBundle;

public class TextInputBox implements Initializable{
    @FXML
    private JFXButton submitOrderButton;

    @FXML
    private JFXTextField orderStringBox;

    private String orderString;

    @Override
    public void initialize(URL url, ResourceBundle rb){

    }

    public void updateOrderString(){
        orderString = orderStringBox.getText();
        System.out.println("inside update orderstring " + orderString);
    }

    public void submitOrder(){
        String s = orderStringBox.getText();
        System.out.println("inside submit order: "+ s);
        Restaurant.newEvent(s);
    }

    public void clearTextBox(){
        orderStringBox.clear();
    }




}
