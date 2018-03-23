package main;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import restaurant.Restaurant;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable{
    private Restaurant restaurant;

    MainController(Restaurant restaurant){
        this.restaurant = restaurant;
    }

    @Override
    public void initialize(URL url, ResourceBundle rb){
        setObservers();
    }

    private void setObservers(){
        try {
            //billview
            System.out.println("inside MC set observers");
            FXMLLoader bvLoader = getFXMLLoader("BillView.fxml");
            bvLoader.load();
            BillView bv = bvLoader.getController();
            restaurant.addObserver(bv);

            //cook order view
            FXMLLoader cvLoader = getFXMLLoader("CookOrderView.fxml");
            cvLoader.load();
            CookOrderView cv = cvLoader.getController();
            restaurant.addObserver(cv);
        }catch(IOException ex){
            //TODO: log "can't find file ex"
        }


        //manager view
    }

    private FXMLLoader getFXMLLoader(String source) {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource(source));
        return loader;
    }
}
