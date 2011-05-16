package de.soniro.detrin.model;

import java.util.HashSet;
import java.util.Set;

/**
 * A nominal group is a group for grouping values of a NominalAttribute.
 * 
 * @author Nina Rothenberg
 */
public class NominalGroup extends Group<String> {
	
	Set<String> valuesInGroup = new HashSet<String>(); 

	String name;
	
	public NominalGroup(String name) {
		super();
		this.name = name;
	}
	
	@Override
	public boolean isValueInGroup(String value) {
		for (String valueInGroup : valuesInGroup) {
			if (valueInGroup.equals(value)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public Object clone() {
		NominalGroup clone = new NominalGroup(name);
		clone.valuesInGroup = new HashSet<String>(valuesInGroup);
		return clone;
	}
	
	public Set<String> getValues() {
		return valuesInGroup;
	}

	@Override
	public String toString() {
		return name;
	}
}
