package com.github.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

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

	public static final String[] CATEGORIES = { "All", "Primary Arms", "Primary Tools", "Primary Tools", "Armor",
			"Accessories", "Medicines", "Materials", "Other", "Pugilist's Arms", "Gladiator's Arms", "Marauder's Arms",
			"Archer's Arms", "Lancer's Arms", "Thaumaturge's Arms", "Conjurer's Arms", "Arcanist's Arms", "Shields",
			"Dancer's Arms", "Carpenter's Tools", "Blacksmith's Tools", "Armorer's Tools", "Goldsmith's Tools",
			"Leatherworker's Tools", "Weaver's Tools", "Alchemist's Tools", "Culinarian's Tools", "Miner's Tools",
			"Botanist's Tools", "Fisher's Tools", "Fishing Tackle", "Head", "Undershirts", "Body", "Undergarments",
			"Legs", "Hands", "Feet", "Waist", "Necklaces", "Earrings", "Bracelets", "Rings", "Medicine", "Ingredients",
			"Meals", "Seafood", "Stone", "Metal", "Lumber", "Cloth", "Leather", "Bone", "Reagents", "Dyes",
			"Weapon Parts", "Furnishings", "Materia", "Crystals", "Catalysts", "Miscellany", "Soul Crystals", "Arrows",
			"Quest Items", "Other", "Exterior Fixtures", "Interior Fixtures", "Outdoor Furnishings", "Chairs and Beds",
			"Tables", "Tabletop", "Wall-mounted", "Rugs", "Rogue's Arms", "Seasonal Miscellany", "Minions",
			"Dark Knight's Arms", "Machinist's Arms", "Astrologian's Arms", "Airship/Submersible Components",
			"Orchestrion Components", "Gardening Items", "Paintings", "Samurai's Arms", "Red Mage's Arms",
			"Scholar's Arms", "Gunbreaker's Arms", "Dancer's Arms", "Reaper's Arms", "Sage's Arms" };

	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK);

	private Dimension windowSize = new Dimension(840, 640);
	private int uiWidth = 225;

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

		JTextPane results = new JTextPane();
		results.setBounds(0, 0, resultsPane.getWidth(), resultsPane.getHeight());
		results.setBackground(new Color(238, 238, 238));
		results.setFont(results.getFont().deriveFont(Font.BOLD));
		results.setFocusable(false);
		results.setEditable(false);
		results.setText("Results: ");
		resultsPane.setViewportView(results);

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
				recentcyLabel
						.setText("Recency: " + recentcy.getValue() + " hour" + ((recentcy.getValue() > 1) ? "s" : ""));
			}
		});
		add(recency);

		JLabel categoriesLabel = new JLabel("Categories");
		categoriesLabel.setBounds(10, 170, uiWidth, 20);
		categoriesLabel.setHorizontalAlignment(JLabel.LEFT);
		add(categoriesLabel);

		JComboBox<String> categories = new JComboBox<>(CATEGORIES);
		categories.setBounds(10, 195, uiWidth, 20);
		add(categories);

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
					new Thread(new GUIMarketUpdateThread(recentcy.getValue() * 3600, update.getParent())).start();
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
				MarketData[] searchResults = MarketData.getSearchResults(search.getText().split(","), "profit",
						categories.getSelectedIndex(), 0, Item.getItemListSize());

				StringBuilder builder = new StringBuilder();
				builder.append("Results:\n");

				for (int i = 0; i < searchResults.length; i++) {

					// Calculated the percentage of profit compared to the highest profitability
					double profitability = (long) ((searchResults[i].getProfitability()
							/ MarketData.getHighestSearchProfitability()) * 10000) / 100.0;
					builder.append("\n" + searchResults[i].getName() + " - " + profitability + "% - "
							+ (int) searchResults[i].getAverageGilPerUnit() + "g x "
							+ (int) searchResults[i].getAverageStackSize());
				}

				results.setText(builder.toString());
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

}
