package com.github.deltabreaker.main;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.gui.GUIMain;

public class WebManager {

	public static final String BASE_URL = "https://universalis.app/api/history/";
	public static final long DEFAULT_RESULTS_SIZE = 200;
	public static final long DEFAULT_RECENTCY = 86400;
	public static final long URL_ID_LIMIT = 100;
	public static final long REQUEST_INTERVAL = 100L;

	private static String server = GUIMain.SERVER_LIST[0];
	private static double updateProgress = 0;
	private static boolean isUpdating = false;

	private static String get(String url, long timeout) throws Exception {
		URL website = new URL(url);
		URLConnection connection = website.openConnection();
		BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
		StringBuilder response = new StringBuilder();
		String inputLine;
		while ((inputLine = in.readLine()) != null)
			response.append(inputLine);
		in.close();
		return response.toString();
	}

	public static void updateResults(ArrayList<Long> idArray, long resultLimit, long recentcy) throws Exception {
		if (server != null) {
			System.out.println(LocalDateTime.now() + " [WebHandler]: Updating market data. Result limit: " + resultLimit
					+ " | Recentcy: " + recentcy);
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

								JSONArray entries = (JSONArray) item.get("entries");
								long[] pricesPerUnit = new long[entries.size()];
								long[] quantities = new long[entries.size()];
								long[] saleTimes = new long[entries.size()];
								for (int e = 0; e < entries.size(); e++) {
									JSONObject entry = (JSONObject) entries.get(e);

									pricesPerUnit[e] = (long) entry.get("pricePerUnit");
									quantities[e] = (long) entry.get("quantity");
									saleTimes[e] = (long) entry.get("timestamp");
								}

								MarketData.update(itemID, Item.getItem(itemID).getCategory(), pricesPerUnit, quantities,
										saleTimes);
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
