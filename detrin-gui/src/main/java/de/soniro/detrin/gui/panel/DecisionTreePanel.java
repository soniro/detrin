package de.soniro.detrin.gui.panel;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JScrollPane;

import de.soniro.detrin.gui.elements.ScreenImage;
import de.soniro.detrin.gui.visualisation.TreeVisualisation;
import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.DecisionTree;
import edu.uci.ics.jung.graph.Tree;

public final class DecisionTreePanel extends JScrollPane {

	private static final long serialVersionUID = 534929344708807450L;

	private final TreeVisualisation visualization;
	private final JLabel loadingImage = new JLabel(new ImageIcon(getClass().getResource("/images/loading.gif")));

	private static DecisionTreePanel instance;

	public static DecisionTreePanel getInstance() {
		if (instance == null) {
			instance = new DecisionTreePanel();
		}
		return instance;
	}

	private DecisionTreePanel() {
		super(new TreeVisualisation());
		setPreferredSize(getDreiViertelWindow());
		visualization = (TreeVisualisation) getViewport().getView();
	}

	public void updateTree(Tree<DecisionTree, Branch> tree) {
		setViewportView(visualization);
		visualization.updateTree(tree);
	}

	private Dimension getDreiViertelWindow() {
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		screenSize.setSize(screenSize.getWidth() * 0.75, screenSize.getHeight() * 0.75);
		return screenSize;
	}

	public void showLoading() {
		setViewportView(loadingImage);
	}

	public void hideLoading() {
		setViewportView(null);
	}

	public BufferedImage getImageForExport() {
		   BufferedImage bufImage = ScreenImage.createImage(visualization);
		   return bufImage;
		}

}
