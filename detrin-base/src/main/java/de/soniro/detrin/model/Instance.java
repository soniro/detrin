package de.soniro.detrin.model;

import java.util.HashMap;
import java.util.Set;
import java.util.Map.Entry;

/**
 * An instance maps the values to the attributes.
 * 
 * @author Nina Rothenberg
 *
 */
public class Instance extends HashMap<Attribute<?>, Object> {

	private static final long serialVersionUID = -8984097859628159102L;

	@SuppressWarnings("unchecked")
	public <T>T getValueForAttribute(Attribute<T> attribute) {
		return (T) get(attribute);
	}
	
	public Set<Attribute<?>> getAttributes() {
		return keySet();
	}
	
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		for (Entry<Attribute<?>, Object> entry : entrySet()) {
			output.append(entry.getKey().getName() + " = " + entry.getValue() + ", ");
		}
		// Cut last comma.
		return output.substring(0, output.length() - 2);
	}
}