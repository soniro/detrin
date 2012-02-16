package de.soniro.detrin.model;

/**
 * The abstract DecisionTree.
 * 
 * @author Nina Rothenberg
 */
public abstract class DecisionTree extends Explainable {
	
	protected Attribute<?> attribute;
	
	public DecisionTree(Attribute<?> attribute) {
		this.attribute = attribute;
	}
	
	public abstract int getDepth();
	
	public abstract String getLabel();
	
	public abstract void accept(DecisionTreeVisitor visitor);
	
	public abstract String classify(Instance instance);
	
	public Attribute<?> getAttribute() {
		return attribute;
	}
	
	@Override
	public String toString() {
		return getLabel();
	}
	
}
