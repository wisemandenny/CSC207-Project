package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MenuList implements Initializable {
    private final List<JFXButton> selectedItems = new ArrayList<>();
    private final List<List<JFXButton>> itemsWithIngredients = new ArrayList<>();
    private boolean toggleShowIngredients = false;


    @FXML
    private JFXListView<JFXButton> listView;
    @FXML
    private JFXToggleButton showIngredientsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addMenuLabels();
    }

    public List<JFXButton> getSelectedItems() {
        return selectedItems;
    }

    private void addMenuLabels() {
        listView.getItems().clear();
        Menu menu = Restaurant.getMenu();
        for (MenuItem i : menu.getMenu()) {
            try {
                List<JFXButton> itemIngredients = new ArrayList<>();
                JFXButton itemButton = new JFXButton(i.getName());
                itemButton.setOnAction(e -> clickOnItem(itemButton));
                listView.getItems().add(itemButton);
                itemIngredients.add(itemButton);

                for (Ingredient ingredient : i.getIngredients()) {
                    JFXButton ingredientButton = new JFXButton("     > " + ingredient.getName());
                    itemIngredients.add(ingredientButton);
                }

                itemsWithIngredients.add(itemIngredients);

                //showIngredientsButton.setPadding(new Insets(24));
            } catch (Exception ex) {
                Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void toggleShowIngredients() {
        toggleShowIngredients = !toggleShowIngredients;
    }


    private void clickOnItem(JFXButton itemButton) {
        if (toggleShowIngredients) {
            showIngredients(itemButton);
        } else {
            selectItems(itemButton);
        }
    }

    private void selectItems(JFXButton itemButton) {
        if (itemButton.getStyle().contains("-fx-background-color: green")) {
            itemButton.setStyle("-fx-background-color: transparent");
            selectedItems.remove(itemButton);
        } else {
            itemButton.setStyle("-fx-background-color: green");
            selectedItems.add(itemButton);
        }
    }

    private void showIngredients(JFXButton itemButton) {
        listView.getItems().clear();

        for (List<JFXButton> itemList : itemsWithIngredients) {
            JFXButton itemNameButton = itemList.get(0);
            if (itemButton.getText().equals(itemNameButton.getText())) {
                if (itemNameButton.getOpacity() == 1.0) {
                    listView.getItems().addAll(itemList);
                    itemNameButton.setOpacity(0.99);
                } else {
                    listView.getItems().add(itemNameButton);
                    itemNameButton.setOpacity(1.0);
                }
            } else {
                listView.getItems().add(itemNameButton);
            }
        }
    }
}
