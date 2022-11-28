package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.HashMap;

public class Item {

	private static HashMap<Integer, Item> marketableItemTable = new HashMap<>();
	private static HashMap<Integer, Item> completeItemTable = new HashMap<>();
	private static HashMap<String, Integer> nameTable = new HashMap<>();
	private static HashMap<String, Integer> categories = new HashMap<>();

	private int id;
	private String name;
	private byte category;
	private boolean hasVenture = false;
	private int ventureAmt;
	
	public Item(int id, String name, byte category) {
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

	public byte getCategory() {
		return category;
	}

	public boolean hasVenture() {
		return hasVenture;
	}
	
	public int getVentureAmount() {
		return ventureAmt;
	}
	
	public void setVenture(int amount) {
		hasVenture = true;
		ventureAmt = amount;
	}
	
	public static Item getItem(int id) {
		return completeItemTable.get(id);
	}

	public static int getMarketableItemListSize() {
		return marketableItemTable.size();
	}

	public static int getCompleteItemListSize() {
		return completeItemTable.size();
	}

	public static void loadItem(Item item, boolean marketable) {
		if (marketable) {
			marketableItemTable.put(item.id, item);
		}
		completeItemTable.put(item.id, item);
		nameTable.put(item.name, item.id);
	}

	public static ArrayList<Integer> getMarketableIDList() {
		ArrayList<Integer> ids = new ArrayList<>();
		for (Integer l : marketableItemTable.keySet()) {
			ids.add(l);
		}
		return ids;
	}

	public static void clearItemData() {
		marketableItemTable.clear();
		completeItemTable.clear();
	}

	public static void createCategory(String name, int id) {
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

	public static boolean hasItemForID(int id) {
		return marketableItemTable.containsKey(id);
	}

	public static int getIDFromName(String name) {
		return nameTable.get(name);
	}

}
