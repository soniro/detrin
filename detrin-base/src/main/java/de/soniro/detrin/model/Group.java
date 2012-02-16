package de.soniro.detrin.model;

/**
 * A group allows you to group the values of an attribute.
 * 
 * @author Nina Rothenberg
 *
 * @param <T> Value-Type of the attribute-values
 */
public abstract class Group<T> implements Cloneable {
	
	public abstract boolean isValueInGroup(T value);

	public abstract Object clone();
	
}
