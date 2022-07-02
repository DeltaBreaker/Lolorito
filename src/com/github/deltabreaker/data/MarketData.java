package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class MarketData {

	private static HashMap<Integer, MarketData> marketData = new HashMap<>();

	// Used to automatically sort market data based on the key given
	private static TreeMap<Double, MarketData> searchFilter = new TreeMap<>();

	private int id;
	private byte category;
	private long[] pricesPerUnit;
	private int[] quantities;
	private long[] listingPrices;
	private boolean[] isHQ;

	private double averageGilPerUnit;
	private double averageGilPerSale;
	private double averageStackSize;

	private double totalProfit = 0;
	private long totalSold;

	public MarketData(int id, byte category, long[] pricesPerUnit, int[] quantities, long[] listingPrices,
			boolean[] isHQ) {
		updateData(id, category, pricesPerUnit, quantities, listingPrices, isHQ);
	}

	public void updateData(int id, byte category, long[] pricesPerUnit, int[] quantities, long[] listingPrices,
			boolean[] isHQ) {
		this.id = id;
		this.category = category;
		this.pricesPerUnit = pricesPerUnit;
		this.quantities = quantities;
		this.listingPrices = listingPrices;
		this.isHQ = isHQ;

		averageGilPerUnit = 0;
		totalProfit = 0;
		averageStackSize = 0;
		totalSold = 0;

		for (int i = 0; i < pricesPerUnit.length; i++) {
			averageGilPerUnit += pricesPerUnit[i];
			totalProfit += pricesPerUnit[i] * quantities[i];

			averageStackSize += quantities[i];

			totalSold += quantities[i];
		}

		averageGilPerUnit /= Math.max(pricesPerUnit.length, 1);
		averageGilPerSale = totalProfit / Math.max(pricesPerUnit.length, 1);
		averageStackSize /= Math.max(quantities.length, 1);
	}

	public int getID() {
		return id;
	}

	public String getName() {
		return Item.getItem(id).getName();
	}

	public double getTotalProfit() {
		return totalProfit;
	}

	public double getAverageStackSize() {
		return averageStackSize;
	}

	public long getTotalSold() {
		return totalSold;
	}

	public double getAverageGilPerUnit() {
		return averageGilPerUnit;
	}

	public static boolean containsDataForID(int id) {
		return marketData.containsKey(id);
	}

	public static MarketData getHistory(int id) {
		return marketData.get(id);
	}

	public static void addMarketData(MarketData data) {
		marketData.put(data.id, data);
	}

	public String getListingAmount() {
		int nq = 0;
		int hq = 0;
		for (boolean b : isHQ) {
			@SuppressWarnings("unused")
			int i = (b) ? hq++ : nq++;
		}
		return "NQ: " + nq + " | HQ: " + hq;
	}

	public String getLowestListedNQPrice() {
		ArrayList<Long> items = new ArrayList<>();
		for (int i = 0; i < listingPrices.length; i++) {
			if (!isHQ[i]) {
				items.add(listingPrices[i]);
			}
		}

		Long[] results = items.toArray(new Long[items.size()]);
		Arrays.sort(results);

		return (items.size() > 0) ? "" + results[0] : "N/A";
	}

	public String getLowestListedHQPrice() {
		ArrayList<Long> items = new ArrayList<>();
		for (int i = 0; i < listingPrices.length; i++) {
			if (isHQ[i]) {
				items.add(listingPrices[i]);
			}
		}

		Long[] results = items.toArray(new Long[items.size()]);
		Arrays.sort(results);

		return (items.size() > 0) ? "" + results[0] : "N/A";
	}

	public double getLowestListedNQPriceValue() {
		ArrayList<Long> items = new ArrayList<>();
		for (int i = 0; i < listingPrices.length; i++) {
			if (!isHQ[i]) {
				items.add(listingPrices[i]);
			}
		}

		Long[] results = items.toArray(new Long[items.size()]);
		Arrays.sort(results);

		return (items.size() > 0) ? results[0].longValue() : 0;
	}

	public double getLowestListedHQPriceValue() {
		ArrayList<Long> items = new ArrayList<>();
		for (int i = 0; i < listingPrices.length; i++) {
			if (isHQ[i]) {
				items.add(listingPrices[i]);
			}
		}

		Long[] results = items.toArray(new Long[items.size()]);
		Arrays.sort(results);

		return (items.size() > 0) ? results[0] : 0;
	}

	public static MarketData[] getSearchResults(String[] name, String type, long[] categories, int start, int end,
			boolean filterUnsold) {
		searchFilter.clear();

		for (MarketData m : marketData.values()) {
			if ((filterUnsold && m.totalSold > 0) || !filterUnsold) {
				switch (type) {

				case "Total Sold":
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if ((matchesCat || categories.length == 0)) {
								searchFilter.put((double) m.totalSold + new Random().nextFloat(), m);
								break;
							}
						}
					}
					break;

				case "Avg. Price":
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if (matchesCat || categories.length == 0) {
								searchFilter.put((double) m.getAverageGilPerUnit() + new Random().nextFloat(), m);
								break;
							}
						}
					}
					break;

				case "Listed NQ Price":
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if (matchesCat || categories.length == 0) {
								searchFilter.put(m.getLowestListedNQPriceValue() + new Random().nextFloat(), m);
								break;
							}
						}
					}
					break;

				case "Listed HQ Price":
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if (matchesCat || categories.length == 0) {
								searchFilter.put(m.getLowestListedHQPriceValue() + new Random().nextFloat(), m);
								break;
							}
						}
					}
					break;

				case "Crafting Profit":
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if (matchesCat || categories.length == 0) {
								String profit = getCraftingProfit(m.getID());
								if (!profit.equals("N/A")) {
									searchFilter.put((double) (Long.parseLong(profit) + new Random().nextFloat()), m);
								}
								break;
							}
						}
					}
					break;

				default:
					for (String s : name) {
						if (m.getName().toLowerCase().contains(s.toLowerCase())) {
							boolean matchesCat = false;
							for (long i : categories) {
								if (i == m.category) {
									matchesCat = true;
									break;
								}
							}
							if (matchesCat || categories.length == 0) {
								searchFilter.put((double) m.totalProfit + new Random().nextFloat(), m);
								break;
							}
						}
					}
					break;
					
				}
			}
		}

		ArrayList<MarketData> results = new ArrayList<>();
		for (MarketData m : searchFilter.values()) {
			results.add(0, m);
		}

		return results.toArray(new MarketData[results.size()]);
	}

	public static void update(int itemID, byte category, long[] pricesPerUnit, int[] quantities, long[] listingPrices,
			boolean[] isHQ) {
		if (marketData.containsKey(itemID)) {
			marketData.get(itemID).updateData(itemID, category, pricesPerUnit, quantities, listingPrices, isHQ);
		} else {
			marketData.put(itemID, new MarketData(itemID, category, pricesPerUnit, quantities, listingPrices, isHQ));
		}
	}

	public static String getLowestMarketPrice(int i, int count) {
		if (marketData.containsKey(i)) {
			MarketData item = marketData.get(i);

			int lowestNQ = (int) item.getLowestListedNQPriceValue();
			int lowestHQ = (int) item.getLowestListedHQPriceValue();
			int lowestMarketPrice = lowestNQ;
			if (lowestHQ > 0 && (lowestHQ < lowestNQ || lowestMarketPrice == 0)) {
				lowestMarketPrice = lowestHQ;
			}

			if (lowestMarketPrice > 0) {
				return "" + lowestMarketPrice * count;
			}
		}
		return "N/A";
	}

	public static String getHighestLowestMarketPrice(int i, int count) {
		if (marketData.containsKey(i)) {
			MarketData item = marketData.get(i);

			int lowestNQ = (int) item.getLowestListedNQPriceValue();
			int lowestHQ = (int) item.getLowestListedHQPriceValue();
			int highestMarketPrice = lowestNQ;
			if (lowestHQ > 0 && (lowestHQ > lowestNQ || highestMarketPrice == 0)) {
				highestMarketPrice = lowestHQ;
			}

			if (highestMarketPrice > 0) {
				return "" + highestMarketPrice * count;
			}
		}
		return "N/A";
	}

	public static String getCostOfComponents(int item, int amount) {
		if (Recipe.hasRecipe(item)) {
			long totalCost = 0;
			int[] materials = Recipe.getRecipe(item).getMaterials();
			int[] amounts = Recipe.getRecipe(item).getAmounts();
			for (int i = 0; i < materials.length; i++) {
				String marketCost = getLowestMarketPrice(materials[i], amounts[i]);
				String craftingCost = "N/A";

				if (Recipe.hasRecipe(materials[i])) {
					craftingCost = getCostOfComponents(materials[i], amounts[i]);
				}

				String lowestCost = marketCost;
				if (!craftingCost.equals("N/A")
						&& (marketCost.equals("N/A") || Long.parseLong(craftingCost) < Long.parseLong(marketCost))) {
					lowestCost = craftingCost;
				}

				if (!lowestCost.equals("N/A")) {
					totalCost += Long.parseLong(lowestCost);
				}
			}
			long resultCost = totalCost * amount;
			return (resultCost > 0) ? resultCost + "" : "N/A";
		}
		return "N/A";
	}

	public static String getCraftingProfit(int id) {
		if (Recipe.hasRecipe(id)) {
			String costOfMaterials = getCostOfComponents(id, 1);
			String lowestPrice = MarketData.getHighestLowestMarketPrice(id, Recipe.getRecipe(id).getAmount());

			if (!lowestPrice.contentEquals("N/A")) {
				long cost = Long.parseLong(lowestPrice);
				if (!costOfMaterials.equals("N/A")) {
					cost -= Long.parseLong(costOfMaterials);
				}
				return "" + cost;
			}
		}

		return "N/A";
	}

}