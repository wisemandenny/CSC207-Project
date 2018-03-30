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

/**
 * The main container class for the GUI. The GUI also runs initializes the backend of the program.
 */
public class Main extends Application implements Observer {
    //THE THREE TABS OF THE GUI
    ServerView serverView;
    CookOrderView cookOrderView;
    ManagerView managerView;

    /**
     * {@inheritDoc}
     */
    @Override
    public void start(Stage primaryStage) throws Exception{
        //DECLARE AND INITIALIZE TABS
        JFXTabPane tabPane = new JFXTabPane();
        Tab serverTab= new Tab();
        Tab cookTab = new Tab();
        Tab managerTab = new Tab();

        //LOAD THE SERVER VIEW & ITS CONTROLLER FROM FXML
        FXMLLoader svLoader = new FXMLLoader();
        serverTab.setContent(svLoader.load(getClass().getResource("ServerView.fxml").openStream()));
        serverView = svLoader.getController();
        serverView.addObserver(this);
        serverTab.setText("Server");

        //LOAD THE COOK ORDER VIEW & ITS CONTROLLER FROM FXML
        FXMLLoader covLoader = new FXMLLoader();
        cookTab.setContent(covLoader.load(getClass().getResource("CookOrderView.fxml").openStream()));
        cookOrderView = covLoader.getController();
        cookOrderView.addObserver(this);
        cookTab.setText("Cook");

        //LOAD THE MANAGER VIEW & ITS CONTROLLER FROM FXML
        FXMLLoader mvLoader = new FXMLLoader();
        managerTab.setContent(mvLoader.load(getClass().getResource("ManagerView.fxml").openStream()));
        managerView = mvLoader.getController();
        managerView.addObserver(this);
        managerTab.setText("Manager");

        //ADD THE TABS
        tabPane.getTabs().addAll(serverTab, cookTab, managerTab);
        Parent root = tabPane;
        root.prefHeight(800);
        root.prefWidth(800);

        //MATERIAL UI WINDOW DECORATOR
        JFXDecorator decorator = new JFXDecorator(primaryStage, root);
        decorator.setCustomMaximize(true);

        //SET THE SCENE AND DISPLAY IT
        Scene scene = new Scene(decorator, 800, 800);
        scene.getStylesheets().add("main/style.css"); //set the style sheet
        primaryStage.setScene(scene);
        primaryStage.setTitle("Restaurant Manager");
        primaryStage.show();
    }

    /**
     * Stops the backend thread when the program terminates by user action.
     */
    @Override
    public void stop(){
        Restaurant.end();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void update(Observable o, Object arg) {
        cookOrderView.refresh();
        serverView.refresh();
        managerView.refresh();
    }

    /**
     * {@inheritDoc}
     */
    public static void main(String[] args) {
        //INSTANTIATE THE BACKEND AND START THE BACKEND THREAD
        Restaurant restaurant = Restaurant.getInstance(10, 0.13);
        restaurant.start();
        //LAUNCH THE GUI
        launch(args);
    }
}
