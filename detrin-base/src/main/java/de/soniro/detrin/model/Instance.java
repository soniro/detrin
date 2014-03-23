package de.soniro.detrin.model;

import java.util.HashMap;
import java.util.Set;

import org.apache.commons.lang.builder.HashCodeBuilder;

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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Instance) {
			Instance other = (Instance) o;
			if (this.size() != other.size()) return false;
			boolean equals = true;
			for (Entry<Attribute<?>, Object> entry : entrySet()) {
				if (other.containsKey(entry.getKey())) {
					equals &= other.get(entry.getKey()).equals(entry.getValue());
				} else {
					return false;
				}
			}
			return equals;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	     return new HashCodeBuilder(17, 37).append(entrySet()).toHashCode();
	}
}