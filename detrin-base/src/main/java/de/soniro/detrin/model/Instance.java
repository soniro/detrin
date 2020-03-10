package de.soniro.detrin.model;

import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.HashMap;
import java.util.Set;

public class Instance extends HashMap<String, Object> {

	private static final long serialVersionUID = -8984097859628159102L;

	@SuppressWarnings("unchecked")
	public <T>T getValueForAttribute(Attribute<T> attribute) {
		return (T) get(attribute.getName());
	}
	
	public Set<String> getAttributes() {
		return keySet();
	}
	
	@Override
	public String toString() {
		StringBuilder output = new StringBuilder();
		for (Entry<String, Object> entry : entrySet()) {
			output.append(entry.getKey()).append(" = ").append(entry.getValue()).append(", ");
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
			for (Entry<String, Object> entry : entrySet()) {
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