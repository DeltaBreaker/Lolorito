package com.github.deltabreaker.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.TreeMap;

public class MarketData {

	private static HashMap<Long, MarketData> marketData = new HashMap<>();

	// Used to automatically sort market data based on the key given
	private static TreeMap<Double, MarketData> searchFilter = new TreeMap<>();

	private long id;
	private long category;
	private long[] pricesPerUnit;
	private long[] quantities;
	private long[] listingPrices;

	private double averageGilPerUnit;
	private double averageGilPerSale;
	private double averageStackSize;

	private double totalProfit = 0;
	private long totalSold;

	public MarketData(long id, long category, long[] pricesPerUnit, long[] quantities, long[] listingPrices) {
		updateData(id, category, pricesPerUnit, quantities, listingPrices);
	}

	public void updateData(long id, long category, long[] pricesPerUnit, long[] quantities, long[] listingPrices) {
		this.id = id;
		this.category = category;
		this.pricesPerUnit = pricesPerUnit;
		this.quantities = quantities;
		this.listingPrices = listingPrices;

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

	public long getID() {
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

	public static boolean containsDataForID(long id) {
		return marketData.containsKey(id);
	}

	public static MarketData getSaleHistory(long id) {
		return marketData.get(id);
	}

	public static void addMarketData(MarketData data) {
		marketData.put(data.id, data);
	}

	public int getListingAmount() {
		return listingPrices.length;
	}

	public String getLowestListedPrice() {
		if (listingPrices.length > 0) {
			long lowestPrice = listingPrices[0];

			for (long l : listingPrices) {
				if (l < lowestPrice) {
					lowestPrice = l;
				}
			}

			return lowestPrice + "g";
		}

		return "N/A";
	}

	public double getLowestListedPriceValue() {
		if (listingPrices.length > 0) {
			long lowestPrice = listingPrices[0];

			for (long l : listingPrices) {
				if (l < lowestPrice) {
					lowestPrice = l;
				}
			}

			return lowestPrice;
		}

		return 0;
	}

	public static MarketData[] getSearchResults(String[] name, String type, long[] categories, int start, int end) {
		searchFilter.clear();

		for (MarketData m : marketData.values()) {
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
						if (matchesCat || categories.length == 0) {
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
							searchFilter.put((double) m.getAverageGilPerUnit(), m);
							break;
						}
					}
				}
				break;

			case "Listed Price":
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
							searchFilter.put(m.getLowestListedPriceValue(), m);
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
							searchFilter.put((double) m.totalProfit, m);
							break;
						}
					}
				}
				break;

			}
		}

		ArrayList<MarketData> results = new ArrayList<>();
		for (MarketData m : searchFilter.values()) {
			results.add(0, m);
		}

		return results.toArray(new MarketData[results.size()]);
	}

	public static void update(long itemID, long category, long[] pricesPerUnit, long[] quantities,
			long[] listingPrices) {
		if (marketData.containsKey(itemID)) {
			marketData.get(itemID).updateData(itemID, category, pricesPerUnit, quantities, listingPrices);
		} else {
			marketData.put(itemID, new MarketData(itemID, category, pricesPerUnit, quantities, listingPrices));
		}
	}

}