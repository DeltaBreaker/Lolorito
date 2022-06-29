package com.github.deltabreaker.main;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.deltabreaker.data.Item;
import com.opencsv.CSVReader;

public class FileManager {

	public static final int ID_LOCATION = 0;
	public static final int NAME_LOCATION = 10;
	public static final int CATEGORY_LOCATION = 17;
	public static final int TRADABLE_LOCATION = 23;

	public static String loadCSVData(String file) {
		try {
			Item.clearItemData();
			return readCSVData(new FileReader(file));
		} catch (FileNotFoundException e) {
			return " [FileManager]: Error reading CSV data.";
		}
	}

	public static String loadCSVData(InputStream in) {
		return readCSVData(new InputStreamReader(in));
	}

	private static String readCSVData(Reader reader) {
		String text;
		try {
			ArrayList<String[]> lines = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(reader)) {
				String[] values = null;
				while ((values = csvReader.readNext()) != null) {
					lines.add(values);
				}
			}

			for (String[] s : lines) {
				if (isNumeric(s[0]) && !s[NAME_LOCATION].trim().equals("")
						&& !Boolean.parseBoolean(s[TRADABLE_LOCATION])) {
					if (Item.getItemListSize() < 2000)
					Item.loadItem(new Item(Long.parseLong(s[ID_LOCATION]), s[NAME_LOCATION],
							Long.parseLong(s[CATEGORY_LOCATION])));
				}
			}

			text = LocalDateTime.now() + " [FileManager]: " + Item.getItemListSize() + " items loaded";
			System.out.println(text);
			return text;
		} catch (Exception e) {
			e.printStackTrace();
			text = LocalDateTime.now() + " [FileManager]: Error reading CSV data.";
			System.out.println(text);
			return text;
		}
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
				Item.createCategory((String) category.get("name"), (long) category.get("id"));
			}
			System.out.println(LocalDateTime.now() + " [FileManager]: " + Item.getCategoryAmount() + " categories loaded.");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(LocalDateTime.now() + " [FileManager]: Error reading category data.");
		}
	}

}
