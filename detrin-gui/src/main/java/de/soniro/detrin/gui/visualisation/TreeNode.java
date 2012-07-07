package de.soniro.detrin.gui.visualisation;

import de.soniro.detrin.model.DecisionTree;

public class TreeNode {

	private DecisionTree tree;
	private String label;

	public TreeNode(DecisionTree tree) {
		this.tree = tree;
	}

	@Override
	public String toString() {
		return tree.getLabel();
	}

}
