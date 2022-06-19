package com.github.deltabreaker.main;

import com.github.deltabreaker.data.MarketData;

public class Startup {

	private static final String ITEM_DATA_LOCATION = "res/data.json";

	public static void main(String[] args) {
		try {
			MarketData.loadNameTable(ITEM_DATA_LOCATION);
			
			new Thread(new CommandManager(System.in)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
