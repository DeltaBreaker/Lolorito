package com.github.deltabreaker.main;

import javax.swing.ImageIcon;

import com.github.deltabreaker.gui.GUIMain;

public class Startup {

	public static final ImageIcon ICON = new ImageIcon(Startup.class.getResource("/icon.png"));
	
	private static final String ITEM_DATA = "Item.txt";
	private static final String VENTURE_DATA = "Ventures.txt";
	private static final String RECIPE_DATA = "Recipe.txt";
	private static final String CATEGORY_DATA = "Categories.json";

	public static void main(String[] args) {
		FileManager.loadCSVItemData(Startup.class.getClassLoader().getResourceAsStream(ITEM_DATA), Startup.class.getClassLoader().getResourceAsStream(VENTURE_DATA));
		FileManager.loadCSVRecipeData(Startup.class.getClassLoader().getResourceAsStream(RECIPE_DATA));
		FileManager.loadCategories(Startup.class.getClassLoader().getResourceAsStream(CATEGORY_DATA));
		new GUIMain();
	}

}
