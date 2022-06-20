package com.github.deltabreaker.data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.TreeMap;

public class MarketData {

	private static HashMap<Long, MarketData> marketData = new HashMap<>();

	private static TreeMap<Double, MarketData> profitabilityFilter = new TreeMap<>();
	private static TreeMap<Long, MarketData> frequencyFilter = new TreeMap<>();
	private static ArrayList<MarketData> profitabilityList = new ArrayList<>();
	private static ArrayList<MarketData> frequencyList = new ArrayList<>();

	private static TreeMap<Double, MarketData> searchFilter = new TreeMap<>();
	private static ArrayList<MarketData> searchList = new ArrayList<>();

	private long id;
	private long category;
	@SuppressWarnings("unused")
	private long[] pricesPerUnit;
	@SuppressWarnings("unused")
	private long[] quantities;
	private long[] saleTimes;

	private double totalProfit = 0;

	// This just represents the average amount gained per sale
	private double averageGilPerUnit;
	@SuppressWarnings("unused")
	private double averageGilPerSale;
	private double averageStackSize;
	private double averageSaleTime;
	private long totalSold;
	private double profitability;

	public MarketData(long id, long category, long[] pricesPerUnit, long[] quantities, long[] saleTimes) {
		updateData(id, category, pricesPerUnit, quantities, saleTimes);
	}

	public void updateData(long id, long category, long[] pricesPerUnit, long[] quantities, long[] saleTimes) {
		this.id = id;
		this.category = category;
		this.pricesPerUnit = pricesPerUnit;
		this.quantities = quantities;
		this.saleTimes = saleTimes;

		averageGilPerUnit = 0;
		totalProfit = 0;
		averageStackSize = 0;
		averageSaleTime = 0;
		totalSold = 0;

		for (int i = 0; i < pricesPerUnit.length; i++) {
			averageGilPerUnit += pricesPerUnit[i];
			totalProfit += pricesPerUnit[i] * quantities[i];

			averageStackSize += quantities[i];
			averageSaleTime += saleTimes[i];

			totalSold += quantities[i];
		}

		averageGilPerUnit /= Math.max(pricesPerUnit.length, 1);
		averageGilPerSale = totalProfit / Math.max(pricesPerUnit.length, 1);
		averageStackSize /= Math.max(quantities.length, 1);
		averageSaleTime /= Math.max(quantities.length, 1);
		profitability = averageGilPerUnit * averageStackSize * pricesPerUnit.length;
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

	public double getProfitability() {
		return profitability;
	}

	public double getAverageStackSize() {
		return averageStackSize;
	}

	public long getLatestSale() {
		return saleTimes[0];
	}

	public double getAverageSaleTime() {
		return averageSaleTime;
	}

	public long getTotalSold() {
		return totalSold;
	}

	public double getAverageGilPerUnit() {
		return averageGilPerUnit;
	}

	public static double getHighestProfitability() {
		return profitabilityList.get(0).profitability;
	}

	public static double getHighestSearchProfitability() {
		return searchList.get(0).profitability;
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

	public static void updateLists() {
		profitabilityList.clear();
		frequencyList.clear();

		for (MarketData m : marketData.values()) {
			profitabilityFilter.put(m.profitability, m);
		}
		for (MarketData m : profitabilityFilter.values()) {
			profitabilityList.add(0, m);
		}
		profitabilityFilter.clear();

		for (MarketData m : marketData.values()) {
			frequencyFilter.put(m.totalSold, m);
		}
		for (MarketData m : frequencyFilter.values()) {
			frequencyList.add(0, m);
		}
		frequencyFilter.clear();

		System.out.println(LocalDateTime.now() + " [MarketData]: Profitability list updated");
	}

	public static MarketData[] getProfitabilityResults(int start, int end) {
		ArrayList<MarketData> results = new ArrayList<>();

		for (int i = start; i < end; i++) {
			if (i < profitabilityList.size()) {
				results.add(profitabilityList.get(i));
			}
		}

		return results.toArray(new MarketData[results.size()]);
	}

	public static MarketData[] getSearchResults(String[] name, String type, int category, int start, int end) {
		searchList.clear();

		for (MarketData m : marketData.values()) {
			switch (type) {

			case "sales":
				for (String s : name) {
					if (m.getName().toLowerCase().contains(s.toLowerCase())
							&& (category == 0 || m.category == category)) {
						searchFilter.put((double) m.totalSold, m);
						break;
					}
				}
				break;

			default:
				for (String s : name) {
					if (m.getName().toLowerCase().contains(s.toLowerCase())
							&& (category == 0 || m.category == category)) {
						searchFilter.put(m.profitability, m);
						break;
					}
				}
				break;

			}
		}

		for (MarketData m : searchFilter.values()) {
			searchList.add(0, m);
		}

		ArrayList<MarketData> results = new ArrayList<>();
		for (int i = start; i < end; i++) {
			if (i < searchList.size()) {
				results.add(searchList.get(i));
			}
		}
		searchFilter.clear();

		return results.toArray(new MarketData[results.size()]);
	}

	public static MarketData[] getFrequencyResults(int start, int end) {
		ArrayList<MarketData> results = new ArrayList<>();

		synchronized (frequencyList) {
			for (int i = start; i < end; i++) {
				if (i < frequencyList.size()) {
					results.add(frequencyList.get(i));
				}
			}
		}

		return results.toArray(new MarketData[results.size()]);
	}

	public static long getProfitabilitySize() {
		return profitabilityList.size();
	}

	public static long getFrequencySize() {
		return frequencyList.size();
	}

	public static long getSearchSize() {
		return searchList.size();
	}

	public static void update(long itemID, long category, long[] pricesPerUnit, long[] quantities, long[] saleTimes) {
		if (marketData.containsKey(itemID)) {
			marketData.get(itemID).updateData(itemID, category, pricesPerUnit, quantities, saleTimes);
		} else {
			marketData.put(itemID, new MarketData(itemID, category, pricesPerUnit, quantities, saleTimes));
		}
	}
}