package de.soniro.detrin.model;

/**
 * A nominal attribute is an attribute which handels string-values.
 * 
 * @author Nina Rothenberg
 */
public class NominalAttribute extends Attribute<String> {

	public NominalAttribute(String name) {
		super(name);
	}	
	
	@Override
	public Object clone() {
		NominalAttribute clone = new NominalAttribute(name);
		clone.possibleValues.addAll(possibleValues);
		for (Group<String> group : groups) {
			clone.addGroup((NominalGroup)group.clone());
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
