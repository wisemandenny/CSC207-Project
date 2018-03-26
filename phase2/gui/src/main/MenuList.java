package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListCell;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import menu.Ingredient;
import menu.Menu;
import menu.MenuItem;
import restaurant.Restaurant;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MenuList implements Initializable {
    private static final Background SELECTED_BACKGROUND = new Background(new BackgroundFill(Color.web("#29B6F6"), CornerRadii.EMPTY, Insets.EMPTY));
    private final List<JFXButton> selectedItems = new ArrayList<>();
    private final List<List<JFXButton>> itemsWithIngredients = new ArrayList<>();
    private boolean toggleShowIngredients = false;

    @FXML private JFXListView<JFXButton> menuListView;
    @FXML private JFXToggleButton showIngredientsButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        addMenuLabels();
    }

    public List<JFXButton> getSelectedItems() {
        return selectedItems;
    }

    private void addMenuLabels() {
        menuListView.getItems().clear();
        Menu menu = Restaurant.getInstance().getMenu();
        for (MenuItem i : menu.getMenu()) {
            try {
                List<JFXButton> itemIngredients = new ArrayList<>();
                JFXButton itemButton = new JFXButton(i.getName());
                itemButton.setOnAction(e -> clickOnItem(itemButton));
                menuListView.getItems().add(itemButton);
                itemIngredients.add(itemButton);

                for (Ingredient ingredient : i.getIngredients()) {
                    JFXButton ingredientButton = new JFXButton("     > " + ingredient.getName());
                    itemIngredients.add(ingredientButton);
                }

                itemsWithIngredients.add(itemIngredients);

                //showIngredientsButton.setPadding(new Insets(24));
            } catch (Exception ex) {
                //TODO: log this exception
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
        JFXListCell<JFXButton> selectedCell = (JFXListCell<JFXButton>) itemButton.getParent();
        if(selectedCell.getBackground().equals(SELECTED_BACKGROUND)){
            selectedCell.setBackground(Background.EMPTY);
            selectedItems.remove(itemButton);
        } else {
            selectedCell.setBackground(SELECTED_BACKGROUND);
            selectedItems.add(itemButton);
        }/*
        if (itemButton.getStyle().contains("-fx-background-color: green")) {
            itemButton.setStyle("-fx-background-color: transparent");
            selectedItems.remove(itemButton);
        } else {
            itemButton.setStyle("-fx-background-color: green");
            selectedItems.add(itemButton);
        }*/
    }

    private void showIngredients(JFXButton itemButton) {
        menuListView.getItems().clear();

        for (List<JFXButton> itemList : itemsWithIngredients) {
            JFXButton itemNameButton = itemList.get(0);
            if (itemButton.getText().equals(itemNameButton.getText())) {
                if (itemNameButton.getOpacity() == 1.0) {
                    menuListView.getItems().addAll(itemList);
                    itemNameButton.setOpacity(0.99);
                } else {
                    menuListView.getItems().add(itemNameButton);
                    itemNameButton.setOpacity(1.0);
                }
            } else {
                menuListView.getItems().add(itemNameButton);
            }
        }
    }
}
