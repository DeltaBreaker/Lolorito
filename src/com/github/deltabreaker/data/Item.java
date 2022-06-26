package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class Item {

	private static HashMap<Long, Item> itemTable = new HashMap<>();
	private static Map<String, Long> categories = new LinkedHashMap<>();

	private long id;
	private String name;
	private long category;

	public Item(long id, String name, long category) {
		this.id = id;
		this.name = name;
		this.category = category;
	}

	public long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public long getCategory() {
		return category;
	}

	public static Item getItem(long id) {
		return itemTable.get(id);
	}

	public static int getItemListSize() {
		return itemTable.size();
	}

	public static void loadItem(Item item) {
		itemTable.put(item.id, item);
	}

	public static ArrayList<Long> getIDList() {
		ArrayList<Long> ids = new ArrayList<>();
		for (Long l : itemTable.keySet()) {
			ids.add(l);
		}
		return ids;
	}

	public static void clearItemData() {
		itemTable.clear();
	}

	public static void createCategory(String name, long id) {
		categories.put(name, id);
	}

	public static int getCategoryAmount() {
		return categories.size();
	}

	public static String[] getCategories() {
		return categories.keySet().toArray(new String[categories.size()]);
	}

	public static long getCategoryID(String string) {
		return categories.get(string);
	}

}
