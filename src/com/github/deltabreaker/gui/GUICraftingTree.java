package com.github.deltabreaker.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import com.github.deltabreaker.data.Item;
import com.github.deltabreaker.data.MarketData;
import com.github.deltabreaker.data.Recipe;

public class GUICraftingTree extends JFrame implements Runnable {

	private static final long serialVersionUID = -8559986460937812745L;

	private static Dimension windowSize = new Dimension(720, 720);

	private int spacing = 80;
	private int defaultYPosition = 25;
	private int yPosition = defaultYPosition;

	public GUICraftingTree(Recipe recipe) {
		setTitle(Item.getItem(recipe.getResult()).getName());
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setSize(windowSize);
		setResizable(false);
		setLayout(null);
		setLocationRelativeTo(null);

		JScrollPane resultsPane = new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		resultsPane.setBounds(0, 0, windowSize.width - 15, windowSize.height - 38);
		resultsPane.setAlignmentY(JScrollPane.RIGHT_ALIGNMENT);
		add(resultsPane);

		JPanel tree = new JPanel() {

			private static final long serialVersionUID = 2412963991607063328L;

			public void paintComponent(Graphics g) {
				super.paintComponent(g);

				g.setColor(Color.black);
				g.setFont(g.getFont().deriveFont(Font.BOLD));
				yPosition = defaultYPosition;
				g.drawString(Item.getItem(recipe.getResult()).getName() + " x " + recipe.getAmount(), 20, yPosition);

				String costOfCrafting = MarketData.getCostOfComponents(recipe.getResult(), 1);
				g.drawString("Cost of crafting: " + costOfCrafting, 20 + spacing / 2, yPosition + spacing / 4);

				String nqPrice = MarketData.getHistory(recipe.getResult()).getLowestListedNQPrice();
				String hqPrice = MarketData.getHistory(recipe.getResult()).getLowestListedHQPrice();
				if (!nqPrice.equals("N/A")) {
					nqPrice = "" + Long.parseLong(nqPrice) * recipe.getAmount();
					if (!costOfCrafting.equals("N/A")) {
						nqPrice = "" + (Long.parseLong(nqPrice) - Long.parseLong(costOfCrafting));
					}
				}
				if (!hqPrice.equals("N/A")) {
					hqPrice = "" + Long.parseLong(hqPrice) * recipe.getAmount();
					if (!costOfCrafting.equals("N/A")) {
						hqPrice = "" + (Long.parseLong(hqPrice) - Long.parseLong(costOfCrafting));
					}
				}

				g.drawString("Net profit from selling: NQ: " + nqPrice + " / HQ: " + hqPrice,
						20 + spacing / 2, yPosition + spacing / 4 * 2);

				yPosition += spacing;
				drawRecipeComponents(g, recipe, 20);
			}

			private void drawRecipeComponents(Graphics g, Recipe recipe, int x) {
				int startHeight = yPosition;
				for (int i = 0; i < recipe.getMaterials().length; i++) {
					g.drawString(Item.getItem(recipe.getMaterials()[i]).getName() + " x " + recipe.getAmounts()[i],
							x + spacing, yPosition);
					g.drawString("Market price: " + MarketData.getLowestMarketPrice(recipe.getMaterials()[i], 1),
							x + spacing + spacing / 2, yPosition + spacing / 5);
					g.drawString(
							"Total cost: "
									+ MarketData.getLowestMarketPrice(recipe.getMaterials()[i], recipe.getAmounts()[i]),
							x + spacing + spacing / 2, yPosition + spacing / 5 * 2);
					g.drawLine(x, yPosition - 5, x + spacing - 5, yPosition - 5);
					yPosition += spacing;
					setPreferredSize(new Dimension(x + spacing + 100, yPosition));

					if (Recipe.hasRecipe(recipe.getMaterials()[i])) {
						g.drawString(
								"Cost of crafting: " + MarketData.getCostOfComponents(recipe.getMaterials()[i],
										recipe.getAmounts()[i]),
								x + spacing + spacing / 2, yPosition - spacing + spacing / 5 * 3);
						drawRecipeComponents(g, Recipe.getRecipe(recipe.getMaterials()[i]), x + spacing);
					}
				}
				g.drawLine(x, startHeight - spacing + 5, x, yPosition - spacing - 5);
			}

		};
		tree.setPreferredSize(new Dimension(300, 300));
		resultsPane.setViewportView(tree);

		setVisible(true);
	}

	@Override
	public void run() {
		while (isVisible()) {
			repaint();

			try {
				Thread.sleep(100L);
			} catch (InterruptedException e1) {
				e1.printStackTrace();
			}
		}
	}

}
