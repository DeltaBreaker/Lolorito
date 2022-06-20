package com.github.deltabreaker.main;

public class Startup {

	public static void main(String[] args) {
		try {
			new Thread(new CommandManager(System.in)).start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
