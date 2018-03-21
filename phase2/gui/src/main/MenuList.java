package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXListView;
import com.jfoenix.controls.JFXToggleButton;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
    @FXML
    private JFXListView<JFXButton> listView;
    @FXML
    private JFXToggleButton showIngredientsButton;
    private boolean toggleShowIngredients = false;

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
                JFXButton itemButton = new JFXButton(i.getName() + "           Price: $" + i.getPrice());
                itemButton.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        clickOnItem(itemButton);
                    }
                });
                listView.getItems().add(itemButton);
                itemIngredients.add(itemButton);


                for (Ingredient ingredient : i.getIngredients()) {
                    JFXButton ingredientButton = new JFXButton("     > " + ingredient.getName());
                    itemIngredients.add(ingredientButton);
                }
                itemsWithIngredients.add(itemIngredients);
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
        //itemswithingredients has lists like this:
        //BURGER, bun, patty, etc
        //hotdog, hotdog, bun, mustard, etc
        //each list is made of jfxbuttons.

        //go through the entire itemswithingredients list
        //in each sublist, look at the text of the first element in the list
        //if it matches the text of itembutton
        //if the opacity is 100
        //add the entire sublist (name and ingredients) to listview
        //change the opacity of the jfxbutton name to 99
        //else
        //add just the name
        //set opacity to 100
        //else
        //add just the first element of the sublist (the name) to list view.
        //TODO: delete this explanation (leave it for now so if you're curious you can read it
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
