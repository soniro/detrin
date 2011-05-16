package de.soniro.detrin.model;

import java.util.Locale;

/**
 * A branch belongs to a DecisionTree-Node. The binds a parent-node with the client tree.
 * The branch is an extra object because it also handels its own explanations.
 * 
 * @author Nina Rothenberg
 */
public class Branch extends Explainable {

	private final String label;
	
	private DecisionTree child;
	
	private Node parent;
	
	private final int numberOfInstances;
	
	public Branch(String label, DecisionTree child, Node parent, int numberOfInstances) {
		this.label = label;
		this.child = child;
		this.parent = parent;
		this.numberOfInstances = numberOfInstances;
	}
	
	public String getLabel(Locale locale) {
		return label;
	}
	
	@Override
	public String toString() {
		return getLabel(null).replace("&lt;", "<").replace("&gt;", ">");
	}
	
	public DecisionTree getChild() {
		return child;
	}
	
	public void setChild(DecisionTree child) {
		this.child = child;
	}
	
	public Node getParent() {
		return parent;
	}
	
	public void setParent(Node parent) {
		this.parent = parent;
	}
	
	public int getNumberOfInstances() {
		return numberOfInstances;
	}
	
}
