package com.github.deltabreaker.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.data.WebHandler;

public class CommandManager implements Runnable {

	private InputStream in;
	private boolean acceptingCommands = true;

	public CommandManager(InputStream in) {
		this.in = in;
	}

	@Override
	public void run() {
		while (acceptingCommands) {
			try {

				// Read input from console and run specified command
				BufferedReader reader = new BufferedReader(new InputStreamReader(in));
				String input = reader.readLine().toLowerCase();

				String[] args = input.split(" ");
				Command.valueOf(args[0]).run(args);

			} catch (Exception e) {
				System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: Error running command.");
			}
		}
	}

}

enum Command {

	help {

		@Override
		public void run(String[] args) {
			for (Command c : Command.values()) {
				System.out.println(c.getDescription() + "\n");
			}
		}

		@Override
		public String getDescription() {
			return "help - Lists commands and relevent arguments.";
		}

	},

	server() {

		@Override
		public void run(String[] args) {
			WebHandler.setServer(args[1]);
			System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: Target server has been updated.");
		}

		@Override
		public String getDescription() {
			return "server 'target' - Updates the target server to pull data from.";
		}

	},

	update() {

		@Override
		public void run(String[] args) {
			try {
				long resultLimit = WebHandler.DEFAULT_RESULTS_SIZE;
				long recentcy = WebHandler.DEFAULT_RECENTCY;
				for (String s : args) {
					if (s.startsWith("-result_limit")) {
						resultLimit = Integer.parseInt(s.split("=")[1]);
						break;
					}
					if (s.startsWith("-recentcy")) {
						resultLimit = Integer.parseInt(s.split("=")[1]);
						break;
					}
				}

				WebHandler.updateResults(MarketData.getIDs(), resultLimit, recentcy);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@Override
		public String getDescription() {
			return "update - Updates the market data. Args: results_limit=(int), recentcy_limit=(int)";
		}

	},

	profit() {

		@Override
		public void run(String[] args) {
			String searchTerm = "";
			int offset = 0;
			for (String s : args) {
				if (s.startsWith("-search_term")) {
					searchTerm = s.split("=")[1];
					break;
				}
				if (s.startsWith("-offset")) {
					offset = Integer.parseInt(s.split("=")[1]);
					break;
				}
			}

			MarketData[] results = MarketData.getSearchResults(searchTerm.split(","), "profit", offset,
					offset + Integer.parseInt(args[1]));

			System.out.println();
			System.out.println("Results: ");
			for (int i = 0; i < results.length; i++) {
				double profitability = (long) ((results[i].getProfitability()
						/ MarketData.getHighestSearchProfitability()) * 10000) / 100.0;
				System.out.println(results[i].getName() + " - " + profitability + "% - "
						+ (int) results[i].getAverageGilPerUnit() + " x " + (int) results[i].getAverageStackSize());
			}
		}

		@Override
		public String getDescription() {
			return "profit 'result size' - Displays a list of the most profitable items currently sold. Args: search_term=(string,string...), -offset=(int)";
		}

	};

	public abstract void run(String[] args);

	public abstract String getDescription();

}