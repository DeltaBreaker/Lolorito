package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

import com.github.deltabreaker.gui.GUICraftingOptions;

public class MarketData {

	private static HashMap<Integer, MarketData> marketData = new HashMap<>();

	// Used to automatically sort market data based on the key given
	private static TreeMap<Double, MarketData> searchFilter = new TreeMap<>();

	public static ArrayList<Integer> materials = new ArrayList<>();

	private int id;
	private byte category;
	private long[] listingPrices;
	private boolean[] isHQ;

	private double averageGilPerUnit;
	private double averageStackSize;

	private double totalProfit = 0;
	private long totalSold;

	public static boolean filterOutliers = false;
	public static int outlierTolerance = 10;
	public static int outlierCutoff = 5;

	public MarketData(int id, byte category, long[] pricesPerUnit, int[] quantities, long[] listingPrices,
			boolean[] isHQ) {
		updateData(id, category, pricesPerUnit, quantities, listingPrices, isHQ);
	}

	public void updateData(int id, byte category, long[] pricesPerUnit, int[] quantities, long[] listingPrices,
			boolean[] isHQ) {
		if (filterOutliers) {
			if (pricesPerUnit.length > outlierCutoff) {
				long[] pricesCopy = pricesPerUnit.clone();
				Arrays.sort(pricesCopy);

				long medianPrice = (pricesCopy.length % 2 == 0)
						? (pricesCopy[pricesCopy.length / 2] + pricesCopy[pricesCopy.length / 2 - 1]) / 2
						: pricesCopy[pricesCopy.length / 2];

				// Removes the outlier prices while keeping the quantities matched
				ArrayList<Long> filteredPrices = new ArrayList<>();
				ArrayList<Integer> filteredQuantities = new ArrayList<>();
				for (int i = 0; i < pricesPerUnit.length; i++) {
					if (pricesPerUnit[i] < medianPrice * outlierTolerance) {
						filteredPrices.add(pricesPerUnit[i]);
						filteredQuantities.add(quantities[i]);
					}
				}

				pricesPerUnit = filteredPrices.stream().mapToLong(i -> i).toArray();
				quantities = filteredQuantities.stream().mapToInt(i -> i).toArray();
			}

			if (pricesPerUnit.length > 0) {
				long[] pricesCopy = pricesPerUnit.clone();
				Arrays.sort(pricesCopy);

				// Removes the outlier listings while keeping the qualities matched
				long medianPrice = (pricesCopy.length % 2 == 0)
						? (pricesCopy[pricesCopy.length / 2] + pricesCopy[pricesCopy.length / 2 - 1]) / 2
						: pricesCopy[pricesCopy.length / 2];
				ArrayList<Long> filteredListings = new ArrayList<>();
				ArrayList<Boolean> filteredQualities = new ArrayList<>();
				for (int i = 0; i < listingPrices.length; i++) {
					if (listingPrices[i] < medianPrice * outlierTolerance) {
						filteredListings.add(listingPrices[i]);
						filteredQualities.add(isHQ[i]);
					}
				}

				listingPrices = filteredListings.stream().mapToLong(i -> i).toArray();
				isHQ = new boolean[filteredQualities.size()];
				for (int i = 0; i < isHQ.length; i++) {
					isHQ[i] = filteredQualities.get(i);
				}
			}
		}

		this.id = id;
		this.category = category;
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
//		averageGilPerSale = totalProfit / Math.max(pricesPerUnit.length, 1);
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
			boolean filterUnsold, boolean removeNQ, boolean removeHQ) {
		searchFilter.clear();

		for (MarketData m : marketData.values()) {
			if (((filterUnsold && m.totalSold > 0) || !filterUnsold)
					&& (m.totalSold > 0 || m.listingPrices.length > 0)) {

				for (String s : name) {
					if (m.getName().toLowerCase().contains(s.toLowerCase())) {
						boolean matchesCat = false;
						for (long i : categories) {
							if (i == m.category) {
								matchesCat = true;
								break;
							}
						}
						
						boolean checkCraftingOptions = Recipe.hasRecipe(m.id);
						boolean passesCraftCheck = true;
						if(checkCraftingOptions) {
							Recipe r = Recipe.getRecipe(m.id);
							if(!GUICraftingOptions.includeExpert && r.isExpert()) {
								passesCraftCheck = false;
							}
							if(!GUICraftingOptions.includeSpecialist && r.isSpecialist()) {
								passesCraftCheck = false;
							}
						}
						
						if ((matchesCat || categories.length == 0) && (!checkCraftingOptions || passesCraftCheck)) {
							switch (type) {

							case "Total Sold":
								searchFilter.put((double) m.totalSold + new Random().nextFloat(), m);
								break;

							case "Avg. Price":
								searchFilter.put((double) m.getAverageGilPerUnit() + new Random().nextFloat(), m);
								break;

							case "Listed NQ Price":
								searchFilter.put(m.getLowestListedNQPriceValue() + new Random().nextFloat(), m);
								break;

							case "Listed HQ Price":
								searchFilter.put(m.getLowestListedHQPriceValue() + new Random().nextFloat(), m);
								break;

							case "Crafting Profit":
								String profit = getCraftingProfit(m.getID(), removeNQ, removeHQ);
								if (!profit.equals("N/A")) {
									searchFilter.put((double) (Long.parseLong(profit) + new Random().nextFloat()), m);
								}
								break;

							case "Venture Rewards":
								if(Item.getItem(m.id).hasVenture()) {
									double potentialGil = m.averageGilPerUnit * Math.min((Item.getItem(m.id).getVentureAmount() * 24), m.totalSold);
									searchFilter.put(potentialGil, m);
								}
								break;
								
							default:
								searchFilter.put((double) m.totalProfit + new Random().nextFloat(), m);
								break;

							}
							break;
						}
					}
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
				return (materials.contains(i)) ? "" + 0 : "" + lowestMarketPrice * count;
			}
		}
		return "N/A";
	}

	public static String getHighestLowestMarketPrice(int i, int count, boolean removeNQ, boolean removeHQ) {
		if (marketData.containsKey(i)) {
			MarketData item = marketData.get(i);

			int lowestNQ = (removeNQ) ? 0 : (int) item.getLowestListedNQPriceValue();
			int lowestHQ = (removeHQ) ? 0 : (int) item.getLowestListedHQPriceValue();
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

	public static String getCraftingProfit(int id, boolean removeNQ, boolean removeHQ) {
		if (Recipe.hasRecipe(id)) {
			String costOfMaterials = getCostOfComponents(id, 1);
			String lowestPrice = MarketData.getHighestLowestMarketPrice(id, Recipe.getRecipe(id).getAmount(), removeNQ,
					removeHQ);

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