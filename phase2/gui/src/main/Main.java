package main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import restaurant.Restaurant;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

    @Override
    public void stop(){
        Restaurant.end();
    }


    public static void main(String[] args) {
        Restaurant r = Restaurant.getInstance(10, 0.13);
        r.start();
        launch(args);
    }
}
