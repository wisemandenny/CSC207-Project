package main;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialog;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import restaurant.Restaurant;
import restaurant.Table;

class PayPopup {
    StackPane parent;
    Table selectedTable;
    Table selectedSeat;
    TextField inputAmountField;

    PayPopup(StackPane parent, Table selectedTable, Table selectedSeat){
        this.parent = parent;
        this.selectedTable = selectedTable;
        this.selectedSeat = selectedSeat;
        loadPayPopup(selectedSeat);
    }

    private void loadPayPopup(Table selectedSeat){
        JFXDialogLayout content = new JFXDialogLayout();
        content.setHeading(new Label("Pay for order"));

        VBox container = new VBox();
        inputAmountField = new TextField();
        inputAmountField.setPromptText("Enter amount");
        container.getChildren().addAll(inputAmountField, buildNumPad());
        content.setBody(container);

        JFXButton cancelButton = new JFXButton("Cancel");
        JFXButton okButton = new JFXButton("OK");
        content.setActions(cancelButton, okButton);
        content.autosize();

        JFXDialog payPopup = new JFXDialog(parent, content, JFXDialog.DialogTransition.CENTER);
        payPopup.setMaxWidth(150);

        cancelButton.setOnAction(e -> payPopup.close());
        okButton.setOnAction(e -> {
            pay();
            payPopup.close();
        });
        payPopup.show();
    }

    private GridPane buildNumPad(){
        GridPane gridPane = new GridPane();

        for (int i = 1; i <= 3 ; i++) {
            final int j = i;
            JFXButton numberButton = new JFXButton(String.valueOf(j));
            numberButton.setOnAction(e -> appendText(String.valueOf(j)));
            GridPane.setRowIndex(numberButton, 1);
            GridPane.setColumnIndex(numberButton, j);
            gridPane.getChildren().add(numberButton);
        }
        for (int i = 4; i <= 6 ; i++) {
            final int j = i;
            JFXButton numberButton = new JFXButton(String.valueOf(i));
            numberButton.setOnAction(e -> appendText(String.valueOf(j)));
            GridPane.setRowIndex(numberButton, 2);
            GridPane.setColumnIndex(numberButton, i-3);
            gridPane.getChildren().add(numberButton);

        }  for (int i = 7; i <= 9 ; i++) {
            final int j = i;
            JFXButton numberButton = new JFXButton(String.valueOf(i));
            numberButton.setOnAction(e -> appendText(String.valueOf(j)));
            GridPane.setRowIndex(numberButton, 3);
            GridPane.setColumnIndex(numberButton, i-6);
            gridPane.getChildren().add(numberButton);
        }
        JFXButton dotButton = new JFXButton(".");
        dotButton.setOnAction(e -> appendText("."));
        gridPane.add(dotButton, 1, 4); // column=1 row=0
        JFXButton zeroButton = new JFXButton("0");
        zeroButton.setOnAction(e -> appendText(String.valueOf(0)));
        gridPane.add(zeroButton, 2, 4);
        JFXButton clearButton = new JFXButton("C");
        clearButton.setOnAction(e -> inputAmountField.clear());
        gridPane.add(clearButton, 3, 4);
        return gridPane;
    }

    private void appendText(String appendChar){
        StringBuilder sb = new StringBuilder(inputAmountField.getText());
        sb.append(appendChar);
        inputAmountField.setText(sb.toString());
    }
    private void pay(){
        String payEventString = "pay | table " + selectedTable.getId() + " > " + selectedSeat.getId() + " | " +  inputAmountField.getText(); // pay | table 1 > 1 | 45.54
        Restaurant.getInstance().newEvent(payEventString);
    }
}
