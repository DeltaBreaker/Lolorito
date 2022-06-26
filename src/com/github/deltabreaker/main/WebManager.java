package com.github.deltabreaker.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.gui.GUIMain;

public class WebManager {

	public static final String BASE_URL = "https://universalis.app/api/";
	public static final long DEFAULT_RESULTS_SIZE = 200;
	public static final long DEFAULT_RECENTCY = 86400;
	public static final long URL_ID_LIMIT = 100;
	public static final long REQUEST_INTERVAL = 100L;

	private static String server = GUIMain.SERVER_LIST[0];
	private static double updateProgress = 0;
	private static boolean isUpdating = false;

	private static String get(String url, long timeout) throws Exception {
		URL website = new URL(url);
		HttpURLConnection connection = (HttpURLConnection) website.openConnection();
		connection.setRequestProperty("User-Agent",
				"Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");

		StringBuilder response = new StringBuilder();
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
			for (String line; (line = reader.readLine()) != null;) {
				response.append(line);
			}
		}
		return response.toString();
	}

	public static void updateResults(ArrayList<Long> idArray, long resultLimit, long recentcy) throws Exception {
		if (server != null) {
			System.out.println(LocalDateTime.now() + " [WebHandler]: Updating market data. Result limit: " + resultLimit
					+ " | Recentcy: " + recentcy + " | URL Limit: " + URL_ID_LIMIT);
			isUpdating = true;
			while (idArray.size() > 0) {
				try {

					// Build the URL from the base URL and id list
					StringBuilder idList = new StringBuilder();
					int arraySize = idArray.size();
					idList.append(idArray.get(0));
					idArray.remove(0);
					for (int i = 1; i < Math.min(arraySize, URL_ID_LIMIT); i++) {
						idList.append("%2C%20");
						idList.append(idArray.get(0));
						idArray.remove(0);
					}
					String url = BASE_URL + server.toLowerCase() + "/" + idList.toString() + "?entries=" + resultLimit
							+ "&entriesWithin=" + recentcy;

					// Parses the JSON data received and updates the market data
					JSONObject response = (JSONObject) new JSONParser().parse(get(url, URL_ID_LIMIT * 1000));
					JSONArray itemList = (JSONArray) response.get("items");
					if (itemList != null) {
						for (int i = 0; i < itemList.size(); i++) {
							try {
								JSONObject item = (JSONObject) itemList.get(i);

								long itemID = (long) item.get("itemID");

								JSONArray listings = (JSONArray) item.get("listings");
								long[] listingPrices = new long[listings.size()];
								for (int e = 0; e < listings.size(); e++) {
									JSONObject listing = (JSONObject) listings.get(e);

									listingPrices[e] = (long) listing.get("pricePerUnit");
								}

								JSONArray entries = (JSONArray) item.get("recentHistory");
								long[] pricesPerUnit = new long[entries.size()];
								long[] quantities = new long[entries.size()];
								for (int e = 0; e < entries.size(); e++) {
									JSONObject entry = (JSONObject) entries.get(e);

									pricesPerUnit[e] = (long) entry.get("pricePerUnit");
									quantities[e] = (long) entry.get("quantity");
								}

								MarketData.update(itemID, Item.getItem(itemID).getCategory(), pricesPerUnit, quantities,
										listingPrices);
							} catch (Exception e) {
								System.out.println(LocalDateTime.now() + " [WebHandler]: Null data. Skipping item");
							}
						}
					}
					System.out.println(
							LocalDateTime.now() + " [MatketUpdateThread]: Block updated. " + idArray.size() + " left");
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println(LocalDateTime.now()
							+ " [MatketUpdateThread]: Error updating this block of items. Continuing");
				}

				// Prevents the loop from requesting data too often
				updateProgress = 1 - ((double) idArray.size() / Item.getItemListSize());
				Thread.sleep(REQUEST_INTERVAL);
			}
			isUpdating = false;
		} else {
			System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: The target server is null.");
		}
	}

	public static void setServer(String server) {
		WebManager.server = server;
	}

	public static double getUpdateProgress() {
		return updateProgress;
	}

	public static boolean isUpdating() {
		return isUpdating;
	}

}
