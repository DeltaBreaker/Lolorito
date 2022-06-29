package com.github.deltabreaker.data;

public class Recipe {

	public static final String[] CRAFT_TYPES = { "Woodworking", "Smithing", "Armorcraft", "Goldsmithing",
			"Leatherworking", "Clothcraft", "Alchemy", "Cooking" };

	private long result;
	private long amount;
	private long[] materials;
	private long[] amounts;
	private boolean isExpert;
	private boolean isSpecialist;
	private long crafterType;

	public Recipe(long result, long amount, long[] materials, long[] amounts, boolean isExpert, boolean isSpecialist,
			long crafterType) {
		this.result = result;
		this.amount = amount;
		this.materials = materials;
		this.amounts = amounts;
		this.isExpert = isExpert;
		this.isSpecialist = isSpecialist;
		this.crafterType = crafterType;
	}

	public long getResult() {
		return result;
	}

	public long getAmount() {
		return amount;
	}

	public long[] getMaterials() {
		return materials;
	}

	public long[] getAmounts() {
		return amounts;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public boolean isSpecialist() {
		return isSpecialist;
	}

	public long getCrafterType() {
		return crafterType;
	}

}
