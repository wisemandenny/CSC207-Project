import java.util.List;

public interface MenuItem {
    String getName();
    double getPrice();
    List<Ingredient> getIngredients();
    //boolean isModdableWithTheseIngredients(Mod[] mods); //TODO: add this with mod support
}
