package com.github.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.Border;
import javax.swing.text.AbstractDocument;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.main.Startup;

public class GUIMaterials extends JFrame {

	private static final long serialVersionUID = 720612920061634644L;
	private static final String WINDOW_TITLE = "Lolorito - Material List";
	private static final Border BORDER = BorderFactory.createLineBorder(Color.BLACK);

	private static int uiWidth = 275;
	private static Dimension windowSize = new Dimension(uiWidth * 3 + 60, 720);

	private JCheckBox[] materials;
	private int[] ids;
	private HashMap<Integer, JCheckBox> search = new HashMap<>();
	private JPanel materialPane;
	private JPanel searchPane;
	private JScrollPane searchScrollPane;
	private JScrollPane materialScrollPane;

	public GUIMaterials(GUIMain parent) {
		parent.setEnabled(false);

		setTitle(WINDOW_TITLE);
		setIconImage(Startup.ICON.getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowSize);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.setEnabled(true);
				parent.refreshResults();
			}
		});

		JLabel searchLabel = new JLabel("Search Term");
		searchLabel.setBounds(10, 5, uiWidth, 20);
		searchLabel.setHorizontalAlignment(JLabel.LEFT);
		add(searchLabel);

		JTextArea search = new JTextArea("");
		search.setBounds(10, 30, uiWidth, 18);
		search.setBorder(BORDER);
		((AbstractDocument) search.getDocument()).setDocumentFilter(new CustomDocumentFilter());
		add(search);

		JButton searchButton = new JButton("Search");
		searchButton.setBounds(10, 60, uiWidth, 20);
		searchButton.setFocusable(false);
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				updateMaterialList(search.getText());
			}
		});
		add(searchButton);

		JLabel itemsLabel = new JLabel("Materials");
		itemsLabel.setBounds(20 + uiWidth, 5, uiWidth, 20);
		itemsLabel.setHorizontalAlignment(JLabel.LEFT);
		add(itemsLabel);

		searchScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		searchScrollPane.setBounds(uiWidth + 20, 30, uiWidth, windowSize.height - 108);
		searchScrollPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(searchScrollPane);

		searchPane = new JPanel();
		searchPane.setLayout(null);
		searchScrollPane.setViewportView(searchPane);

		JLabel obtainedLabel = new JLabel("Obtained Materials");
		obtainedLabel.setBounds(30 + uiWidth * 2, 5, uiWidth, 20);
		obtainedLabel.setHorizontalAlignment(JLabel.LEFT);
		add(obtainedLabel);

		materialScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		materialScrollPane.setBounds(uiWidth * 2 + 30, 30, uiWidth, windowSize.height - 108);
		materialScrollPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(materialScrollPane);

		materialPane = new JPanel();
		materialPane.setLayout(null);
		materialScrollPane.setViewportView(materialPane);
		updateObtainedMaterials();

		setVisible(true);
	}

	private void updateMaterialList(String term) {
		searchPane.removeAll();
		search.clear();

		ArrayList<String> names = new ArrayList<>();
		for (int i : Item.getMarketableIDList()) {
			if (Item.getItem(i).getName().toLowerCase().contains(term.toLowerCase())) {
				names.add(Item.getItem(i).getName());
			}
		}

		searchPane.setPreferredSize(new Dimension(uiWidth, names.size() * 30));

		for (int i = 0; i < names.size(); i++) {
			int id = Item.getIDFromName(names.get(i));

			JLabel label = new JLabel(names.get(i));
			label.setBounds(10, 5 + 30 * i, uiWidth - uiWidth / 3, 20);
			label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			searchPane.add(label);

			JCheckBox box = new JCheckBox();
			search.put(id, box);
			box.setBounds(uiWidth - 45, 10 + i * 30, 20, 20);
			box.setSelected(MarketData.materials.contains(id));
			box.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					if (box.isSelected()) {
						MarketData.materials.add(id);
					} else {
						MarketData.materials.remove((Object) id);
					}
					updateObtainedMaterials();
					repaint();
				}
			});
			searchPane.add(box);
		}

		repaint();
		setVisible(true);
	}

	private void updateObtainedMaterials() {
		materialPane.removeAll();
		String[] names = new String[MarketData.materials.size()];
		for (int i = 0; i < names.length; i++) {
			names[i] = Item.getItem(MarketData.materials.get(i)).getName();
		}
		materialPane.setPreferredSize(new Dimension(uiWidth, names.length * 30));

		ids = new int[names.length];
		materials = new JCheckBox[names.length];
		for (int i = 0; i < materials.length; i++) {
			JLabel label = new JLabel(names[i]);
			label.setBounds(10, 5 + 30 * i, uiWidth - uiWidth / 3, 20);
			label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			materialPane.add(label);

			ids[i] = MarketData.materials.get(i);
			materials[i] = new JCheckBox();
			materials[i].setBounds(uiWidth - 45, 10 + i * 30, 20, 20);
			materials[i].setSelected(true);
			int carry = i;
			materials[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					MarketData.materials.remove(carry);
					if (search.containsKey(ids[carry])) {
						search.get(ids[carry]).setSelected(false);
					}
					updateObtainedMaterials();
					repaint();
				}
			});
			materialPane.add(materials[i]);
		}

		repaint();
		setVisible(true);
	}

}
