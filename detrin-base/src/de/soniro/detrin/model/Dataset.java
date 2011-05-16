package de.soniro.detrin.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Map.Entry;

import de.soniro.detrin.exception.InvalidInstanceException;

/**
 * The Dataset is a collection of instances. 
 * 
 * @author Nina Rothenberg
 */
public class Dataset implements Cloneable {
	
	private List<Instance> instances = new ArrayList<Instance>();
	
	private List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();
	
	public List<Instance> getInstances() {
		return instances;
	}
	
	public void addInstance(Instance instance) throws InvalidInstanceException {
		validateInstance(instance);
		instances.add(instance);
	}

	public List<Attribute<?>> getAttributes() {
		return attributes;
	}
	
	public void addAttribute(Attribute<?> attribute) {
		attributes.add(attribute);
	}
	
	public Attribute<?> getAttributeByName(String attributeName) {
		for (Attribute<?> attribute : attributes) {
			if (attribute.getName().equals(attributeName)) {
				return attribute;
			}
		}
		throw new IllegalArgumentException(String.format("There is no attribute with the name '%s'.", attributeName));
	}
	
	public int size() {
		return instances.size();
	}
	
	private void add(Instance instance) {
		instances.add(instance);
	}
	
	public Dataset getSubsetForAttributeValue(Attribute<?> attribute, String value) {
		Dataset subset = new Dataset();
		for (Instance instance : instances) {
			if (instance.getValueForAttribute(attribute).equals(value)) {
				subset.add(instance);
			}
		}
		return subset;
	}
	
	public <T>Dataset getSubsetForAttributeGroup(Attribute<T> attribute, Group<T> group) {
		Dataset subset = new Dataset();
		String values = "[";
		for (Instance instance : instances) {
			T value;
			if (attribute instanceof NumericAttribute) {
				// FIXME Unnecessary object and cast. Should work as the else-part. What's wrong?
				value = (T) new Double(instance.getValueForAttribute(attribute).toString());
			} else {
				value = instance.getValueForAttribute(attribute);
			}
			if (group.isValueInGroup(value)) {
				subset.add(instance);
				values += value + ", ";
			}
		}
		values += "]";
		return subset;
	}
	
	public <T>Set<T> getCurrentValuesForAttribute(Attribute<T> attribute) {
		Set<T> currentValues = new HashSet<T>();
		for (Instance instance : instances) {
			T value = instance.getValueForAttribute(attribute);
			if (value instanceof Number) {
				if (((Double) value) >= 0) {
					currentValues.add(instance.getValueForAttribute(attribute));
				}
			} else {
				currentValues.add(instance.getValueForAttribute(attribute));
			}
		}
		return currentValues;
	}
	
	public void validateInstance(Instance instance) throws InvalidInstanceException {
		for (Attribute<?> attribute : instance.getAttributes()) {
			if (!attributes.contains(attribute)) {
				throw new InvalidInstanceException(String.format("Attribute '%s' is not known in the Dataset.", attribute.getName()));
			}
		}
	}
	
	public String getMostProperValueForAttribute(NominalAttribute nominalAttribute) {
		String mostProperValueForAttribute = null;
		Long bestValueCount = null;
		for (String possibleValue : nominalAttribute.getPossibleValues()) {
			Long currentCount = getValueCount(nominalAttribute, possibleValue);
			if (bestValueCount == null || currentCount > bestValueCount) {
				bestValueCount = currentCount;
				mostProperValueForAttribute = possibleValue;
			}
		}
		return mostProperValueForAttribute;
	}
	
	public Long getValueCount(NominalAttribute attribute, String value) {
		Long count = 0L;
		for (Instance instance : instances) {
			if (instance.getValueForAttribute(attribute).equals(value)) {
				count++;
			}
		}
		return count;
	}
	
	@Override
	public String toString() {
		StringBuffer output = new StringBuffer();
		for (Instance instance : instances) {
			output.append("[" + instance + "]\n");
		}
		return output.toString();
	}
	
	@Override
	public Object clone() {
		Dataset clone = new Dataset();
		for (Attribute<?> attribute : attributes) {
			clone.addAttribute(attribute.getClass().cast(attribute.clone()));
		}
		for (Instance instance : instances) {
			Instance instanceClone = new Instance();
			for (Entry<Attribute<?>, Object> value : instance.entrySet()) {
				instanceClone.put(clone.getAttributeByName(value.getKey().getName()), value.getValue());
			}
			try {
				clone.addInstance(instanceClone);
			} catch (InvalidInstanceException e) {
				e.printStackTrace();
				throw new RuntimeException("This should never happen!", e);
			}
		}
		return clone; 
	}
	
}
