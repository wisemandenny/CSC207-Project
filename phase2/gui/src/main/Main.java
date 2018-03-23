package main;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import restaurant.Restaurant;


public class Main extends Application {
    private static Restaurant restaurant;

    @Override
    public void start(Stage primaryStage) throws Exception{
        MainController mc = new MainController(restaurant);

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        fxmlLoader.setController(mc);

        Parent root = fxmlLoader.load();
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 800, 800);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Manager");
        primaryStage.show();

    }

    @Override
    public void stop(){
        Restaurant.end();
    }


    public static void main(String[] args) {
        restaurant = Restaurant.getInstance(10, 0.13);
        restaurant.start();
        launch(args);
    }
}
