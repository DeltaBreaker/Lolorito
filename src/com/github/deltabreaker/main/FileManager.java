package com.github.deltabreaker.main;

import java.io.FileReader;
import java.time.LocalDateTime;
import java.util.ArrayList;

import com.github.deltabreaker.data.Item;
import com.opencsv.CSVReader;

public class FileManager {

	public static final int ID_LOCATION = 0;
	public static final int NAME_LOCATION = 10;
	public static final int CATEGORY_LOCATION = 17;

	public static void loadCSVData(String file) {
		try {
			ArrayList<String[]> lines = new ArrayList<>();
			try (CSVReader csvReader = new CSVReader(new FileReader(file));) {
				String[] values = null;
				while ((values = csvReader.readNext()) != null) {
					lines.add(values);
				}
			}

			for (String[] s : lines) {
				if (isNumeric(s[0]) && !s[NAME_LOCATION].trim().equals("")) {
					Item.loadItem(new Item(Long.parseLong(s[ID_LOCATION]), s[NAME_LOCATION],
							Long.parseLong(s[CATEGORY_LOCATION])));
				}
			}

			System.out.println(LocalDateTime.now() + " [FileManager]: " + Item.getItemListSize() + " items loaded");
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(LocalDateTime.now() + " [FileManager]: Error reading CSV data.");
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

}
