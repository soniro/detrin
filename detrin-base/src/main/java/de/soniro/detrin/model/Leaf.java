package de.soniro.detrin.model;

/**
 * A leaf is a special {@link DecisionTree}.
 * 
 * @author Nina Rothenberg
 *
 */
public class Leaf extends DecisionTree {

	private String label;
	
	public Leaf(Attribute<?> attribute, String label) {
		super(attribute);
		verifyLabel(label);
		this.label = label;
	}
	
	private void verifyLabel(String label) {
		if (!attribute.getPossibleValues().contains(label)) {
			throw new RuntimeException("The leaf value is not a possible value of the target attribute: " + label);
		}
	}
	
	@Override
	public int getDepth() {
		return 0;
	}
	
	@Override
	public String getLabel() {
		return label;
	}

	@Override
	public void accept(DecisionTreeVisitor visitor) {
		visitor.visit(this);
	}

	@Override
	public String classify(Instance instance) {
		return label;
	}

}
