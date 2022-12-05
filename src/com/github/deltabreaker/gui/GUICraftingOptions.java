package com.github.deltabreaker.gui;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Arrays;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.deltabreaker.data.Recipe;
import com.github.deltabreaker.main.Startup;

public class GUICraftingOptions extends JFrame {

	private static final long serialVersionUID = 720612920061634644L;
	private static final String WINDOW_TITLE = "Lolorito - Crafting Options";

	public static boolean[] enabledCrafters;
	public static int maxLevel;
	public static boolean includeExpert;
	public static boolean includeSpecialist;

	private static int uiWidth = 275;
	private static Dimension windowSize = new Dimension(uiWidth * 2 + 55, 0);

	public GUICraftingOptions(GUIMain parent) {
		if (enabledCrafters == null) {
			enabledCrafters = new boolean[Recipe.CRAFT_TYPES.length];
			Arrays.fill(enabledCrafters, true);
		}

		parent.setEnabled(false);
		
		setTitle(WINDOW_TITLE);
		setIconImage(Startup.ICON.getImage());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowSize);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);
		setSize(getWidth(), enabledCrafters.length * 30 + 100);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				parent.setEnabled(true);
				parent.refreshResults();
				parent.requestFocus();
			}
		});

		JLabel typesLabel = new JLabel("Crafter Types");
		typesLabel.setBounds(20, 5, uiWidth, 20);
		typesLabel.setHorizontalAlignment(JLabel.LEFT);
		add(typesLabel);

		JScrollPane typeScrollPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		typeScrollPane.setBounds(20, 30, uiWidth, enabledCrafters.length * 30 + 10);
		typeScrollPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(typeScrollPane);

		JPanel typePane = new JPanel();
		typePane.setPreferredSize(new Dimension(uiWidth, enabledCrafters.length * 30));
		typePane.setLayout(null);
		typeScrollPane.setViewportView(typePane);

		JCheckBox[] types = new JCheckBox[enabledCrafters.length];
		for (int i = 0; i < types.length; i++) {
			JLabel label = new JLabel(Recipe.CRAFT_TYPES[i]);
			label.setBounds(10, 5 + 30 * i, uiWidth - uiWidth / 3, 20);
			label.setAlignmentX(JLabel.LEFT_ALIGNMENT);
			typePane.add(label);

			types[i] = new JCheckBox();
			types[i].setBounds(uiWidth - 45, 10 + i * 30, 20, 20);
			types[i].setSelected(true);

			int loc = i;
			types[i].addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					enabledCrafters[loc] = types[loc].isSelected();
				}
			});
			typePane.add(types[i]);
		}

		JLabel levelLabel = new JLabel("Maximum Recipe Level: " + maxLevel);
		levelLabel.setBounds(uiWidth + 20, 5, uiWidth, 20);
		levelLabel.setHorizontalAlignment(JLabel.CENTER);
		add(levelLabel);

		JSlider level = new JSlider();
		level.setBounds(uiWidth + 20, 30, uiWidth, 20);
		level.setMinimum(1);
		level.setMaximum(maxLevel);
		level.setValue(maxLevel);
		level.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				maxLevel = level.getValue();
				levelLabel.setText("Maximum Recipe Level: " + maxLevel);
			}
		});
		add(level);

		JLabel expertLabel = new JLabel("Include Expert Recipes");
		expertLabel.setBounds(uiWidth + 60, 50, uiWidth, 20);
		expertLabel.setHorizontalAlignment(JLabel.CENTER);
		expertLabel.setHorizontalAlignment(JLabel.LEFT);
		add(expertLabel);

		JCheckBox expert = new JCheckBox();
		expert.setBounds(uiWidth + 30, 50, 20, 20);
		expert.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				includeExpert = expert.isSelected();
			}
		});
		add(expert);

		JLabel specialistLabel = new JLabel("Include Specialist Recipes");
		specialistLabel.setBounds(uiWidth + 60, 80, uiWidth, 20);
		specialistLabel.setHorizontalAlignment(JLabel.CENTER);
		specialistLabel.setHorizontalAlignment(JLabel.LEFT);
		add(specialistLabel);

		JCheckBox specialist = new JCheckBox();
		specialist.setBounds(uiWidth + 30, 80, 20, 20);
		specialist.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				includeSpecialist = specialist.isSelected();
			}
		});
		add(specialist);

		setLocationRelativeTo(null);
		setVisible(true);
	}

}
