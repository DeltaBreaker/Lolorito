package com.github.deltabreaker.main;

import com.github.deltabreaker.gui.GUIMain;

public class Startup {

	private static final String ITEM_DATA = "Item.txt";
	private static final String RECIPE_DATA = "Recipe.txt";
	public static final String CATEGORY_DATA = "Categories.json";

	public static void main(String[] args) {
		new Thread(new CommandManager(System.in)).start();
		FileManager.loadCSVItemData(Startup.class.getClassLoader().getResourceAsStream(ITEM_DATA));
		FileManager.loadCSVRecipeData(Startup.class.getClassLoader().getResourceAsStream(RECIPE_DATA));
		FileManager.loadCategories(Startup.class.getClassLoader().getResourceAsStream(CATEGORY_DATA));
		new GUIMain();
	}

}
