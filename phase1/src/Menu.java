import java.util.Set;

public interface Menu {
    FoodMod getFoodMod(FoodMod query);

    MenuItem[] getMenu();

    MenuItem getMenuItem(MenuItem item);

    Set<FoodMod> getModsMenu();
}
