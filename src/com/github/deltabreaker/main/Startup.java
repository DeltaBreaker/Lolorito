package com.github.deltabreaker.main;

import java.net.URL;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JOptionPane;

import com.github.deltabreaker.data.Recipe;
import com.github.deltabreaker.gui.GUICraftingOptions;
import com.github.deltabreaker.gui.GUIMain;

public class Startup {
	
	public static final String VERSION = "v6.0";
	
	private static final String ITEM_URL = "https://raw.githubusercontent.com/xivapi/ffxiv-datamining/master/csv/Item.csv";
	private static final String RECIPE_URL = "https://raw.githubusercontent.com/xivapi/ffxiv-datamining/master/csv/Recipe.csv";
	private static final String VENTURE_URL = "https://raw.githubusercontent.com/xivapi/ffxiv-datamining/master/csv/RetainerTaskNormal.csv";
	private static final String ITEM_DATA = "Item.txt";
	private static final String VENTURE_DATA = "Ventures.txt";
	private static final String RECIPE_DATA = "Recipe.txt";
	private static final String CATEGORY_DATA = "Categories.json";
	
	public static final ImageIcon ICON = new ImageIcon(Startup.class.getResource("/icon.png"));
	
	public static void main(String[] args) {
		try {
			FileManager.loadCSVItemData(new URL(ITEM_URL).openStream(), new URL(VENTURE_URL).openStream());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error loading item data\nUsing local backup but may be out of date");
			FileManager.loadCSVItemData(Startup.class.getClassLoader().getResourceAsStream(ITEM_DATA), Startup.class.getClassLoader().getResourceAsStream(VENTURE_DATA));
		}

		try {
			FileManager.loadCSVRecipeData(new URL(RECIPE_URL).openStream());
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Error loading recipe data\nUsing local backup but may be out of date");
			FileManager.loadCSVRecipeData(Startup.class.getClassLoader().getResourceAsStream(RECIPE_DATA));
		}

		FileManager.loadCategories(Startup.class.getClassLoader().getResourceAsStream(CATEGORY_DATA));
		
		GUICraftingOptions.enabledCrafters = new boolean[Recipe.CRAFT_TYPES.length];
		Arrays.fill(GUICraftingOptions.enabledCrafters, true);
		new GUIMain();
	}

}
