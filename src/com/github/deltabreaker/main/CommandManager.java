package com.github.deltabreaker.main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.time.LocalDateTime;

import com.github.deltabreaker.data.Item;

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
			if (args.length > 1) {
				for (Command c : Command.values()) {
					String text = c.getDescription();
					System.out.println(text + "\n");
				}
			} else {
				System.out.println(Command.valueOf(args[1]).getDescription());
			}
		}

		@Override
		public String getDescription() {
			return "help 'command (optional)' - Lists commands and relevent arguments.";
		}

	},

	loadItems {

		@Override
		public void run(String[] args) {
			FileManager.loadCSVItemData(args[1]);
		}

		@Override
		public String getDescription() {
			return "load 'file' - Loads the specified JSON for item data.";
		}

	},

	server {

		@Override
		public void run(String[] args) {
			WebManager.setServer(args[1]);
			System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: Target server has been updated.");
		}

		@Override
		public String getDescription() {
			return "server 'target' - Updates the target server to pull data from.";
		}

	},

	update {

		@Override
		public void run(String[] args) {
			try {
				long resultLimit = WebManager.DEFAULT_RESULTS_SIZE;
				long recentcy = WebManager.DEFAULT_RECENTCY;
				for (String s : args) {
					if (s.startsWith("-result_limit")) {
						resultLimit = Integer.parseInt(s.split("=")[1]);
						continue;
					}
					if (s.startsWith("-recentcy")) {
						recentcy = Integer.parseInt(s.split("=")[1]);
						continue;
					}
				}

				WebManager.updateResults(Item.getMarketableIDList(), resultLimit, recentcy);
				System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: Market data updated.");
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println(LocalDateTime.now() + " [MatketUpdateThread]: Error updating market data.");
			}
		}

		@Override
		public String getDescription() {
			return "update - Updates the market data. Args: -results_limit=(int), -recentcy_limit=(int)";
		}

	};

	public abstract void run(String[] args);

	public abstract String getDescription();

}