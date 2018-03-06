public class IngredientImpl implements Ingredient {
	private final String name;
	private final int threshold = 10;
	private int reorderAmount = 20;

	IngredientImpl(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public int getThreshold() {
		return threshold;
	}

	@Override
	public int getReorderAmount() {
		return reorderAmount;
	}

	@Override
	public void setReorderAmount(int amount) {
		reorderAmount = amount;
	}
}
