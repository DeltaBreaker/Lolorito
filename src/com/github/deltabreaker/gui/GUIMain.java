package com.github.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
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
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.data.Recipe;
import com.github.deltabreaker.main.GUIMarketUpdateThread;
import com.github.deltabreaker.main.WebManager;

public class GUIMain extends JFrame {

	private static final String WINDOW_TITLE = "Lolorito";
	private static final long serialVersionUID = -468143660454460863L;

	public static final String[] DATA_CENTER_LIST = { "Aether", "Primal", "Crystal", "Chaos", "Light", "Elemental",
			"Gaia", "Mana", "Materia" };

	public static final String[][] SERVER_LIST = {
			new String[] { "Adamantoise", "Cactuar", "Faerie", "Gilgamesh", "Jenova", "Midgardsormr", "Sargatanas",
					"Siren" },
			new String[] { "Behemoth", "Excalibur", "Exodus", "Famfrit", "Hyperion", "Lamia", "Leviathan", "Ultros" },
			new String[] { "Balmung", "Brynhildr", "Coeurl", "Diabolos", "Goblin", "Malboro", "Mateus", "Zalera" },
			new String[] { "Cerberus", "Louisoix", "Moogle", "Omega", "Ragnarok", "Spriggan" },
			new String[] { "Lich", "Odin", "Phoenix", "Shiva", "Zodiark", "Twintania" },
			new String[] { "Aegis", "Atomos", "Carbuncle", "Garuda", "Gungnir", "Kujata", "Ramuh", "Tonberry", "Typhon",
					"Unicorn" },
			new String[] { "Alexander", "Bahamut", "Durandal", "Fenrir", "Ifrit", "Ridill", "Tiamat", "Ultima",
					"Valefor", "Yojimbo", "Zeromus" },
			new String[] { "Anima", "Asura", "Belias", "Chocobo", "Hades", "Ixion", "Mandragora", "Masamune",
					"Pandaemonium", "Shinryu", "Titan" },
			new String[] { "Bismarck", "Ravana", "Sephirot", "Sophia", "Zurvan" } };

	public static final String[] RESULTS_TABLE_COLUMNS = { "Name", "Avg. Price", "Sold", "# of Listings",
			"Lowest NQ Price", "Lowest HQ Price", "Crafting Profit" };

	public static final String[] SORT_TYPES = { "Total Gil Made", "Avg. Price", "Total Sold", "Listed NQ Price",
			"Listed HQ Price", "Crafting Profit" };

	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK);

	private Dimension windowSize = new Dimension(1280, 720);
	private int uiWidth = 225;

	private JLabel updateLabel;
	private JCheckBox[] categories;

	public GUIMain() {
		setTitle(WINDOW_TITLE);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setSize(windowSize);
		setResizable(false);
		setLayout(null);

		JLabel serverLabel = new JLabel("Data Center / Server");
		serverLabel.setBounds(10, 5, uiWidth, 20);
		serverLabel.setHorizontalAlignment(JLabel.LEFT);
		add(serverLabel);

		JComboBox<String> dataCenters = new JComboBox<>(DATA_CENTER_LIST);
		dataCenters.setBounds(10, 30, uiWidth, 20);
		dataCenters.setFocusable(false);

		JComboBox<String> servers = new JComboBox<>(SERVER_LIST[dataCenters.getSelectedIndex()]);
		servers.setBounds(10, 60, uiWidth, 20);
		servers.setFocusable(false);
		servers.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				WebManager.setServer((String) servers.getSelectedItem());
			}

		});
		add(servers);
		dataCenters.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultComboBoxModel<String> model = new DefaultComboBoxModel<>(
						SERVER_LIST[dataCenters.getSelectedIndex()]);
				servers.setModel(model);
				servers.setSelectedIndex(0);
			}

		});
		add(dataCenters);

		JScrollPane resultsPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultsPane.setBounds(windowSize.width / 2 - 150, 0, windowSize.width / 2 + 135, windowSize.height - 38);
		resultsPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(resultsPane);

		JTable results = new JTable(new DefaultTableModel(new String[][] {}, RESULTS_TABLE_COLUMNS));
		results.setBounds(0, 0, resultsPane.getWidth(), resultsPane.getHeight());
		results.getColumnModel().getColumn(2).setPreferredWidth(15);
		results.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseClicked(java.awt.event.MouseEvent evt) {
				int row = results.rowAtPoint(evt.getPoint());
				if (row >= 0) {
					int id = Item.getIDFromName((String) results.getValueAt(row, 0));
					if (Recipe.hasRecipe(id)) {
						new GUICraftingTree(Recipe.getRecipe(id));
					}
				}
			}
		});
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
		recencyLabel.setBounds(10, 90, uiWidth, 20);
		recencyLabel.setHorizontalAlignment(JLabel.CENTER);
		add(recencyLabel);

		JSlider recency = new JSlider();
		recency.setBounds(10, 110, uiWidth, 20);
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
		sortLabel.setBounds(10, 185, uiWidth, 20);
		sortLabel.setHorizontalAlignment(JLabel.LEFT);
		add(sortLabel);

		JComboBox<String> sortTypes = new JComboBox<>(SORT_TYPES);
		sortTypes.setBounds(10, 215, uiWidth, 20);
		sortTypes.setFocusable(false);
		add(sortTypes);

		JLabel searchLabel = new JLabel("Search Term");
		searchLabel.setBounds(10, 245, uiWidth, 20);
		searchLabel.setHorizontalAlignment(JLabel.LEFT);
		add(searchLabel);

		JTextArea search = new JTextArea("");
		search.setBounds(10, 270, uiWidth, 18);
		search.setBorder(BORDER);
		((AbstractDocument) search.getDocument()).setDocumentFilter(new CustomDocumentFilter());
		add(search);

		updateLabel = new JLabel("");
		updateLabel.setBounds(10, 160, uiWidth, 20);
		updateLabel.setHorizontalAlignment(JLabel.CENTER);
		add(updateLabel);

		JButton update = new JButton("Update Market Data");
		update.setBounds(10, 140, uiWidth, 20);
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

		JLabel filterUnsoldLabel = new JLabel("Filter Unsold Items");
		filterUnsoldLabel.setBounds(40, 340, uiWidth, 20);
		filterUnsoldLabel.setAlignmentX(LEFT_ALIGNMENT);
		add(filterUnsoldLabel);

		JCheckBox filterUnsold = new JCheckBox();
		filterUnsold.setBounds(10, 340, 20, 20);
		add(filterUnsold);
		
		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 300, uiWidth, 20);
		searchButton.setFocusable(false);
		searchButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateResults(search.getText(), (String) sortTypes.getSelectedItem(), filterUnsold.isSelected(), results);
			}

		});
		add(searchButton);

		setLocationRelativeTo(null);
		setVisible(true);

		while (isVisible()) {
			if (WebManager.isUpdating()) {
				updateLabel.setText("Progress: " + (int) (WebManager.getUpdateProgress() * 100) + "%");
			} else if (!updateLabel.getText().equals("")) {
				updateLabel.setText("");

				updateResults(search.getText(), (String) sortTypes.getSelectedItem(), filterUnsold.isSelected(), results);
			}

			repaint();

			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

	private void updateResults(String search, String sortType, boolean filterUnsold, JTable results) {
		MarketData[] searchResults = MarketData.getSearchResults(search.split(","), sortType, getCategoryList(), 0,
				Item.getMarketableItemListSize(), filterUnsold);

		DefaultTableModel model = (DefaultTableModel) results.getModel();
		for (int i = model.getRowCount() - 1; i >= 0; i--) {
			model.removeRow(i);
		}
		for (int i = 0; i < searchResults.length; i++) {
			model.addRow(new String[] { searchResults[i].getName(),
					(int) (searchResults[i].getAverageGilPerUnit() * 100.0) / 100.0 + "g",
					"" + searchResults[i].getTotalSold(), "" + searchResults[i].getListingAmount(),
					searchResults[i].getLowestListedNQPrice(), searchResults[i].getLowestListedHQPrice(),
					MarketData.getCraftingProfit(searchResults[i].getID()) });
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

class CustomDocumentFilter extends DocumentFilter {

	@Override
	public void replace(FilterBypass fb, int i, int i1, String string, AttributeSet as) throws BadLocationException {
		for (int n = string.length(); n > 0; n--) {
			char c = string.charAt(n - 1);
			if (Character.isAlphabetic(c) || Character.isDigit(c) || c == ' ' || c == '.') {
				super.replace(fb, i, i1, String.valueOf(c), as);
			}
		}
	}

	@Override
	public void remove(FilterBypass fb, int i, int i1) throws BadLocationException {
		super.remove(fb, i, i1);
	}

	@Override
	public void insertString(FilterBypass fb, int i, String string, AttributeSet as) throws BadLocationException {
		super.insertString(fb, i, string, as);

	}
}