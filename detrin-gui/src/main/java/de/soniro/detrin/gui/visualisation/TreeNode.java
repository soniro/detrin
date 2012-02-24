package de.soniro.detrin.gui.visualisation;

import de.soniro.detrin.model.DecisionTree;

public class TreeNode {

	DecisionTree tree;
	String label;
	
	public TreeNode(DecisionTree tree) {
		this.tree = tree;
	}
	
	public String toString() {
		return tree.getLabel();
	}
	
}
