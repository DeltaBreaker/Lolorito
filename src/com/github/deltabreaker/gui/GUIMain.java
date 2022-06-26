package com.github.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.main.GUIMarketUpdateThread;
import com.github.deltabreaker.main.WebManager;

public class GUIMain extends JFrame {

	private static final String WINDOW_TITLE = "Lolorito";
	private static final long serialVersionUID = -468143660454460863L;

	public static final String[] SERVER_LIST = { "Adamantoise", "Cactuar", "Faerie", "Gilgamesh", "Jenova",
			"Midgardsormr", "Sargatanas", "Siren", "Behemoth", "Excalibur", "Exodus", "Famfrit", "Hyperion", "Lamia",
			"Leviathan", "Ultros", "Balmung", "Brynhildr", "Coeurl", "Diabolos", "Goblin", "Malboro", "Mateus",
			"Zalera", "Cerberus", "Louisoix", "Moogle", "Omega", "Ragnarok", "Spriggan", "Lich", "Odin", "Phoenix",
			"Shiva", "Zodiark", "Twintania", "Aegis", "Atomos", "Carbuncle", "Garuda", "Gungnir", "Kujata", "Ramuh",
			"Tonberry", "Typhon", "Unicorn", "Alexander", "Bahamut", "Durandal", "Fenrir", "Ifrit", "Ridill", "Tiamat",
			"Ultima", "Valefor", "Yojimbo", "Zeromus", "Anima", "Asura", "Belias", "Chocobo", "Hades", "Ixion",
			"Mandragora", "Masamune", "Pandaemonium", "Shinryu", "Titan", "Bismarck", "Ravana", "Sephirot", "Sophia",
			"Zurvan" };

	public static final String[] RESULTS_TABLE_COLUMNS = { "Name", "Avg. Price", "Total Sold", "# of Listings",
			"Lowest Listed Price" };

	public static final String[] SORT_TYPES = { "Total Profit", "Avg. Price", "Total Sold", "Listed Price" };

	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK);

	private Dimension windowSize = new Dimension(1280, 720);
	private int uiWidth = 225;

	private JCheckBox[] categories;

	public GUIMain() {
		setTitle(WINDOW_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowSize);
		setResizable(false);
		setLayout(null);

		JLabel serverLabel = new JLabel("Server");
		serverLabel.setBounds(10, 5, uiWidth, 20);
		serverLabel.setHorizontalAlignment(JLabel.LEFT);
		add(serverLabel);

		JComboBox<String> servers = new JComboBox<>(SERVER_LIST);
		servers.setBounds(10, 30, uiWidth, 20);
		servers.setFocusable(false);
		servers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WebManager.setServer((String) servers.getSelectedItem());
			}

		});
		add(servers);

		JScrollPane resultsPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultsPane.setBounds(windowSize.width / 2, 0, windowSize.width / 2 - 15, windowSize.height - 38);
		resultsPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(resultsPane);

		JTable results = new JTable(new DefaultTableModel(new String[][] {}, RESULTS_TABLE_COLUMNS));
		results.setBounds(0, 0, resultsPane.getWidth(), resultsPane.getHeight());
		results.setEnabled(false);
		resultsPane.setViewportView(results);

		JLabel categoriesLabel = new JLabel("Categories");
		categoriesLabel.setBounds(uiWidth + 20, 5, uiWidth, 20);
		categoriesLabel.setHorizontalAlignment(JLabel.LEFT);
		add(categoriesLabel);

		JLabel includeLabel = new JLabel("Included");
		includeLabel.setBounds(uiWidth + 20 + uiWidth - 70, 5, uiWidth, 20);
		includeLabel.setHorizontalAlignment(JLabel.LEFT);
		add(includeLabel);

		JScrollPane categoryScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		categoryScrollPane.setBounds(uiWidth + 20, 30, uiWidth, windowSize.height - 108);
		categoryScrollPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(categoryScrollPane);

		JPanel categoryPane = new JPanel();
		categoryPane.setPreferredSize(new Dimension(uiWidth, Item.getCategoryAmount() * 30));
		categoryPane.setLayout(null);
		categoryScrollPane.setViewportView(categoryPane);

		String[] names = Item.getCategories();
		categories = new JCheckBox[Item.getCategoryAmount()];
		for (int i = 0; i < categories.length; i++) {
			JLabel label = new JLabel(names[i]);
			label.setBounds(10, 5 + 30 * i, uiWidth - uiWidth / 3, 20);
			label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			categoryPane.add(label);

			categories[i] = new JCheckBox();
			categories[i].setBounds(uiWidth - 45, 10 + i * 30, 20, 20);
			categories[i].setSelected(true);
			categoryPane.add(categories[i]);
		}

		JButton all = new JButton("All");
		all.setBounds(uiWidth + 20, (int) windowSize.getHeight() - 69, uiWidth / 2 - 10, 20);
		all.setFocusable(false);
		all.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox j : categories) {
					j.setSelected(true);
				}
			}

		});
		add(all);

		JButton none = new JButton("None");
		none.setBounds(uiWidth + 30 + uiWidth / 2, (int) windowSize.getHeight() - 69, uiWidth / 2 - 10, 20);
		none.setFocusable(false);
		none.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (JCheckBox j : categories) {
					j.setSelected(false);
				}
			}

		});
		add(none);

		JLabel recencyLabel = new JLabel("Recency: 24 hours");
		recencyLabel.setBounds(10, 70, uiWidth, 20);
		recencyLabel.setHorizontalAlignment(JLabel.CENTER);
		add(recencyLabel);

		JSlider recency = new JSlider();
		recency.setBounds(10, 90, uiWidth, 20);
		recency.setMinimum(1);
		recency.setMaximum(168);
		recency.setValue(24);
		recency.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				recencyLabel
						.setText("Recency: " + recency.getValue() + " hour" + ((recency.getValue() > 1) ? "s" : ""));
			}
		});
		add(recency);

		JLabel sortLabel = new JLabel("Sort Type");
		sortLabel.setBounds(10, 165, uiWidth, 20);
		sortLabel.setHorizontalAlignment(JLabel.LEFT);
		add(sortLabel);

		JComboBox<String> sortTypes = new JComboBox<>(SORT_TYPES);
		sortTypes.setBounds(10, 195, uiWidth, 20);
		sortTypes.setFocusable(false);
		sortTypes.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

			}

		});
		add(sortTypes);

		JLabel searchLabel = new JLabel("Search");
		searchLabel.setBounds(10, 225, uiWidth, 20);
		searchLabel.setHorizontalAlignment(JLabel.LEFT);
		add(searchLabel);

		JTextArea search = new JTextArea("");
		search.setBounds(10, 250, uiWidth, 18);
		search.setBorder(BORDER);
		add(search);

		JLabel updateLabel = new JLabel("");
		updateLabel.setBounds(10, 140, uiWidth, 20);
		updateLabel.setHorizontalAlignment(JLabel.CENTER);
		add(updateLabel);

		JButton update = new JButton("Update Market Data");
		update.setBounds(10, 120, uiWidth, 20);
		update.setFocusable(false);
		update.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JOptionPane.showMessageDialog(update, "Updating market data. This may take a while.");
					new Thread(new GUIMarketUpdateThread(recency.getValue() * 3600, update.getParent())).start();
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		});
		add(update);

		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 280, uiWidth, 20);
		searchButton.setFocusable(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				MarketData[] searchResults = MarketData.getSearchResults(search.getText().split(","),
						(String) sortTypes.getSelectedItem(), getCategoryList(), 0, Item.getItemListSize());

				DefaultTableModel model = (DefaultTableModel) results.getModel();
				for (int i = model.getRowCount() - 1; i >= 0; i--) {
					model.removeRow(i);
				}
				for (int i = 0; i < searchResults.length; i++) {
					model.addRow(new String[] { searchResults[i].getName(),
							(int) (searchResults[i].getAverageGilPerUnit() * 100.0) / 100.0 + "g",
							"" + searchResults[i].getTotalSold(), "" + searchResults[i].getListingAmount(),
							"" + searchResults[i].getLowestListedPrice() });
				}
			}

		});
		add(searchButton);

		setLocationRelativeTo(null);
		setVisible(true);
		repaint();

		while (isVisible()) {
			if (WebManager.isUpdating()) {
				updateLabel.setText("Progress: " + (int) (WebManager.getUpdateProgress() * 100) + "%");
			} else if (!updateLabel.getText().equals("")) {
				updateLabel.setText("");
			}

			try {
				Thread.sleep(16L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private long[] getCategoryList() {
		String[] names = Item.getCategories();
		ArrayList<Long> categories = new ArrayList<>();
		for (int i = 0; i < this.categories.length; i++) {
			if (this.categories[i].isSelected()) {
				categories.add(Item.getCategoryID(names[i]));
			}
		}
		long[] results = new long[categories.size()];
		for (int i = 0; i < results.length; i++) {
			results[i] = categories.get(i);
		}
		return results;
	}

}
