package com.github.deltabreaker.main;

import java.awt.Component;

import javax.swing.JOptionPane;

import com.github.deltabreaker.data.Item;

public class GUIMarketUpdateThread implements Runnable {

	private int recentcy;
	private Component parent;

	public GUIMarketUpdateThread(int recentcy, Component parent) {
		this.recentcy = recentcy;
		this.parent = parent;
		parent.setEnabled(false);
	}

	@Override
	public void run() {
		try {
			WebManager.updateResults(Item.getIDList(), WebManager.DEFAULT_RESULTS_SIZE, recentcy);
			JOptionPane.showMessageDialog(parent, "Market data has been updated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
		parent.setEnabled(false);
	}

}
