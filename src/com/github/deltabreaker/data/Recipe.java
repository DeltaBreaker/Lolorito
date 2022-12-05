package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.HashMap;

import com.github.deltabreaker.gui.GUICraftingOptions;

public class Recipe {

	public static final String[] CRAFT_TYPES = { "Carpenter", "Blacksmith", "Armorer", "Goldsmith", "Leatherworker",
			"Weaver", "Alchemist", "Culinarian" };

	private static HashMap<Integer, Recipe> recipes = new HashMap<>();
	private static int maxLevel = 1;
	
	private int result;
	private int amount;
	private int[] materials;
	private int[] amounts;
	private boolean isExpert;
	private boolean isSpecialist;
	private ArrayList<Byte> crafterType = new ArrayList<>();
	private int level;

	public Recipe(int result, int amount, int[] materials, int[] amounts, boolean isExpert, boolean isSpecialist,
			byte crafterType, int level) {
		this.result = result;
		this.amount = amount;
		this.materials = materials;
		this.amounts = amounts;
		this.isExpert = isExpert;
		this.isSpecialist = isSpecialist;
		this.crafterType.add(crafterType);
		this.level = level;
		
		if(level > maxLevel) {
			maxLevel = level;
			GUICraftingOptions.maxLevel = maxLevel;
		}
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

	public int getLevel() {
		return level;
	}
	
	public byte[] getCrafterTypes() {
		byte[] result = new byte[crafterType.size()];
		for(int i = 0; i < result.length; i++) {
			result[i] = crafterType.get(i);
		}
		return result;
	}

	public static void loadRecipe(int result, int amount, int[] materials, int[] amounts, boolean isExpert,
			boolean isSpecialist, byte crafterType, int level) {
		if (!recipes.containsKey(result)) {
			recipes.put(result, new Recipe(result, amount, materials, amounts, isExpert, isSpecialist, crafterType, level));
		} else {
			recipes.get(result).crafterType.add(crafterType);
		}
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
