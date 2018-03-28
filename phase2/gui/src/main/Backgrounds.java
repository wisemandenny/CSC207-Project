package main;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

class Backgrounds {
    static final Background RED_BACKGROUND = new Background(new BackgroundFill(Color.web("#EF5350"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background YELLOW_BACKGROUND = new Background(new BackgroundFill(Color.web("#FFEE58"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background GREEN_BACKGROUND = new Background(new BackgroundFill(Color.web("#9CCC65"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#BDBDBD"), CornerRadii.EMPTY, Insets.EMPTY));
   static final Background LIGHT_GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#FAFAFA"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background BLUE_GREY_BACKGROUND = new Background(new BackgroundFill(Color.web("#607D8B"), CornerRadii.EMPTY, Insets.EMPTY));
    static final Background SELECTED_BACKGROUND = new Background(new BackgroundFill(Color.web("#29B6F6"), CornerRadii.EMPTY, Insets.EMPTY));
    public Backgrounds(){}
}
