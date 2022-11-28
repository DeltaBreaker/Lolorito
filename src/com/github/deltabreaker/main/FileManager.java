package com.github.deltabreaker.main;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.commons.lang3.ArrayUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.Recipe;
import com.opencsv.CSVReader;

public class FileManager {

	public static final byte ITEM_ID_LOCATION = 0;
	public static final byte ITEM_NAME_LOCATION = 10;
	public static final byte ITEM_CATEGORY_LOCATION = 17;
	public static final byte ITEM_TRADABLE_LOCATION = 23;
	public static final byte ITEM_COLLECTABLE_LOCATION = 39;

	public static final byte RECIPE_RESULT_LOCATION = 4;
	public static final byte RECIPE_RESULT_AMOUNT_LOCATION = 5;
	public static final byte[] RECIPE_MATERIAL_POSITIONS = { 6, 8, 10, 12, 14, 16, 18, 20, 22, 24 };
	public static final byte[] RECIPE_MATERIAL_AMOUNT_POSITIONS = { 7, 9, 11, 13, 15, 17, 19, 21, 23, 25 };
	public static final byte RECIPE_IS_EXPERT_LOCATION = 45;
	public static final byte RECIPE_IS_SPECIALIST_LOCATION = 44;
	public static final byte RECIPE_CRAFT_TYPE_LOCATION = 2;

	public static final byte VENTURE_ITEM_LOCATION = 1;
	public static final byte VENTURE_AMT_LOCATION = 6;

	public static void loadCSVItemData(InputStream itemData, InputStream ventureData) {
		Item.clearItemData();
		parseItemData(readCSVData(new InputStreamReader(itemData)));
		parseVentureData(readCSVData(new InputStreamReader(ventureData)));
	}

	public static void loadCSVRecipeData(InputStream in) {
		Recipe.clearRecipeData();
		parseRecipeData(readCSVData(new InputStreamReader(in)));
	}

	private static String[][] readCSVData(Reader reader) {
		try {
			ArrayList<String[]> lines = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(reader)) {
				String[] values = null;
				while ((values = csvReader.readNext()) != null) {
					lines.add(values);
				}
			}

			System.out.println(LocalDateTime.now() + " [FileManager]: CSV data read.");
			return lines.toArray(new String[lines.size()][]);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(LocalDateTime.now() + " [FileManager]: Error reading CSV data.");
			return null;
		}
	}

	private static void parseItemData(String[][] data) {
		for (String[] s : data) {
			if (isNumeric(s[0]) && !s[ITEM_NAME_LOCATION].trim().equals("")
					&& !Boolean.parseBoolean(s[ITEM_COLLECTABLE_LOCATION])) {
				Item.loadItem(
						new Item(Integer.parseInt(s[ITEM_ID_LOCATION]), s[ITEM_NAME_LOCATION],
								Byte.parseByte(s[ITEM_CATEGORY_LOCATION])),
						!Boolean.parseBoolean(s[ITEM_TRADABLE_LOCATION]));
			}
		}

		System.out.println(LocalDateTime.now() + " [FileManager]: " + Item.getCompleteItemListSize() + " items loaded. "
				+ Item.getMarketableItemListSize() + " marketable items.");
	}

	private static void parseRecipeData(String[][] data) {
		for (String[] s : data) {
			try {
				if (isNumeric(s[0]) && Item.hasItemForID(Integer.parseInt(s[RECIPE_RESULT_LOCATION]))) {
					ArrayList<Integer> materials = new ArrayList<>();
					ArrayList<Integer> materialAmounts = new ArrayList<>();

					for (int i = 0; i < RECIPE_MATERIAL_POSITIONS.length; i++) {
						int material = Integer.parseInt(s[RECIPE_MATERIAL_POSITIONS[i]]);
						int materialAmount = Integer.parseInt(s[RECIPE_MATERIAL_AMOUNT_POSITIONS[i]]);

						if (material > 0 && materialAmount > 0) {
							materials.add(material);
							materialAmounts.add(materialAmount);
						}
					}

					Recipe.loadRecipe(Integer.parseInt(s[RECIPE_RESULT_LOCATION]),
							Integer.parseInt(s[RECIPE_RESULT_AMOUNT_LOCATION]),
							ArrayUtils.toPrimitive(materials.toArray(new Integer[materials.size()])),
							ArrayUtils.toPrimitive(materialAmounts.toArray(new Integer[materialAmounts.size()])),
							Boolean.parseBoolean(s[RECIPE_IS_EXPERT_LOCATION]),
							Boolean.parseBoolean(s[RECIPE_IS_SPECIALIST_LOCATION]),
							Byte.parseByte(s[RECIPE_CRAFT_TYPE_LOCATION]));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(LocalDateTime.now() + " [FileManager]: Error loading recipe. Skipping.");
			}
		}
		System.out.println(LocalDateTime.now() + " [FileManager]: " + Recipe.getRecipeCount() + " recipes loaded.");
	}

	private static boolean isNumeric(String str) {
		try {
			Double.parseDouble(str);
			return true;
		} catch (NumberFormatException e) {
			return false;
		}
	}

	public static void loadCategories(InputStream resourceAsStream) {
		try {
			JSONArray file = (JSONArray) new JSONParser().parse(new InputStreamReader(resourceAsStream));
			for (int i = 0; i < file.size(); i++) {
				JSONObject category = (JSONObject) file.get(i);
				Item.createCategory((String) category.get("name"), Integer.parseInt((long) category.get("id") + ""));
			}
			System.out.println(
					LocalDateTime.now() + " [FileManager]: " + Item.getCategoryAmount() + " categories loaded.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(LocalDateTime.now() + " [FileManager]: Error reading category data.");
		}
	}

	public static void parseVentureData(String[][] data) {
		for (String[] s : data) {
			try {
				if (isNumeric(s[0]) && Item.hasItemForID(Integer.parseInt(s[VENTURE_ITEM_LOCATION]))) {
					Item.getItem(Integer.parseInt(s[VENTURE_ITEM_LOCATION]))
							.setVenture(Integer.parseInt(s[VENTURE_AMT_LOCATION]));
				}
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(LocalDateTime.now() + " [FileManager]: Error loading venture. Skipping.");
			}
		}
		System.out.println(LocalDateTime.now() + " [FileManager]: Venture data loaded.");
	}

}
