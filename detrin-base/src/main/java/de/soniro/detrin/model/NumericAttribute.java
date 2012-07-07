package de.soniro.detrin.model;

/**
 * Nummeric Attribute which handels Double-values.
 * 
 * @author Nina Rothenberg
 */
public class NumericAttribute extends Attribute<Double> {

	public NumericAttribute(String name) {
		super(name);
	}
	
	public NominalAttribute toNominalAttribute() {
		NominalAttribute nominalAttribute = new NominalAttribute(getName());
		for (Double value : getPossibleValues()) {
			nominalAttribute.addPossibleValue(String.valueOf(value));
		}
		/*
		 * Should groups be translated?
		 */
		nominalAttribute.setActive(isActive());
		return nominalAttribute;
	}
	
	@Override
	public Object clone() {
		NumericAttribute clone = new NumericAttribute(name);
		clone.possibleValues.addAll(possibleValues);
		for (Group<Double> group : groups) {
			clone.addGroup((Interval) group.clone());
		}
		clone.setGroupingType(groupingType);
		clone.setActive(active);
		return clone;
	}

	@Override
	public void accept(AttributeVisitor visitor) {
		visitor.visit(this);
	}
	
}
