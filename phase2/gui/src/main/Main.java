package main;

import com.jfoenix.controls.JFXDecorator;
import com.jfoenix.controls.JFXTabPane;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.stage.Stage;
import restaurant.Restaurant;

import java.util.Observable;
import java.util.Observer;

public class Main extends Application implements Observer {
    ServerView serverView;
    CookOrderView cookOrderView;
    ManagerView managerView;

    @Override
    public void start(Stage primaryStage) throws Exception{
        //Parent root = FXMLLoader.load(getClass().getResource("Main.fxml"));
        //Main.fxml is unused
        JFXTabPane tabPane = new JFXTabPane();
        Tab serverTab= new Tab();
        Tab cookTab = new Tab();
        Tab managerTab = new Tab();

        FXMLLoader svLoader = new FXMLLoader();
        serverTab.setContent(svLoader.load(getClass().getResource("ServerView.fxml").openStream()));
        serverView = svLoader.getController();
        serverView.addObserver(this);
        serverTab.setText("Server");

        FXMLLoader covLoader = new FXMLLoader();
        cookTab.setContent(covLoader.load(getClass().getResource("CookOrderView.fxml").openStream()));
        cookOrderView = covLoader.getController();
        cookOrderView.addObserver(this);
        cookTab.setText("Cook");

        FXMLLoader mvLoader = new FXMLLoader();
        managerTab.setContent(mvLoader.load(getClass().getResource("ManagerView.fxml").openStream()));
        managerView = mvLoader.getController();
        managerView.addObserver(this);
        managerTab.setText("Manager");

        tabPane.getTabs().addAll(serverTab, cookTab, managerTab);
        Parent root = tabPane;
        root.prefHeight(800);
        root.prefWidth(800);

        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);
        Scene scene = new Scene(decorator, 800, 800);
        scene.getStylesheets().add("main/style.css");
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Manager");
        primaryStage.show();
    }

    @Override
    public void stop(){
        Restaurant.end();
    }

    @Override
    public void update(Observable o, Object arg) {
        cookOrderView.refresh();
        serverView.refresh();
        managerView.refresh();
    }

    public static void main(String[] args) {
        Restaurant restaurant = Restaurant.getInstance(10, 0.13);
        restaurant.start();
        launch(args);
    }
}
