package com.github.deltabreaker.data;

import java.util.HashMap;

public class Recipe {

	public static final String[] CRAFT_TYPES = { "Woodworking", "Smithing", "Armorcraft", "Goldsmithing",
			"Leatherworking", "Clothcraft", "Alchemy", "Cooking" };

	private static HashMap<Integer, Recipe> recipes = new HashMap<>();

	private int result;
	private int amount;
	private int[] materials;
	private int[] amounts;
	private boolean isExpert;
	private boolean isSpecialist;
	private byte crafterType;

	public Recipe(int result, int amount, int[] materials, int[] amounts, boolean isExpert, boolean isSpecialist,
			byte crafterType) {
		this.result = result;
		this.amount = amount;
		this.materials = materials;
		this.amounts = amounts;
		this.isExpert = isExpert;
		this.isSpecialist = isSpecialist;
		this.crafterType = crafterType;
	}

	public int getResult() {
		return result;
	}

	public int getAmount() {
		return amount;
	}

	public int[] getMaterials() {
		return materials;
	}

	public int[] getAmounts() {
		return amounts;
	}

	public boolean isExpert() {
		return isExpert;
	}

	public boolean isSpecialist() {
		return isSpecialist;
	}

	public int getCrafterType() {
		return crafterType;
	}

	public static void loadRecipe(int result, int amount, int[] materials, int[] amounts, boolean isExpert,
			boolean isSpecialist, byte crafterType) {
		recipes.put(result, new Recipe(result, amount, materials, amounts, isExpert, isSpecialist, crafterType));
	}

	public static int getRecipeCount() {
		return recipes.size();
	}

	public static void clearRecipeData() {
		recipes.clear();
	}

	public static Recipe getRecipe(int id) {
		return recipes.get(id);
	}

	public static boolean hasRecipe(int id) {
		return recipes.containsKey(id);
	}
	
}
