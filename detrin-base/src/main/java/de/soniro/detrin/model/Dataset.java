package de.soniro.detrin.model;

import de.soniro.detrin.exception.InvalidInstanceException;
import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;

import static java.lang.String.format;

public class Dataset implements Cloneable {
	
	private List<Instance> instances = new ArrayList<>();
	
	private List<Attribute<?>> attributes = new ArrayList<>();
	
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
		throw new IllegalArgumentException(format("There is no attribute with the name '%s'.", attributeName));
	}
	
	public int size() {
		return instances.size();
	}
	
	private void add(Instance instance) {
		instances.add(instance);
	}
	
	public Dataset getSubsetForAttributeValue(Attribute<?> attribute, String value) {
		Dataset subset = new Dataset();
		instances.stream().filter(instance -> instance.getValueForAttribute(attribute).equals(value)).forEach(subset::add);
		return subset;
	}
	
	public <T>Dataset getSubsetForAttributeGroup(Attribute<T> attribute, Group<T> group) {
		Dataset subset = new Dataset();
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
			}
		}
		return subset;
	}
	
	public <T>Set<T> getCurrentValuesForAttribute(Attribute<T> attribute) {
		Set<T> currentValues = new HashSet<>();
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
	
	private void validateInstance(Instance instance) throws InvalidInstanceException {
		if (instance.isEmpty()) {
			throw new InvalidInstanceException("Instance may not be empty. And should at least provide one attribute.");
		}
		for (String attribute : instance.getAttributes()) {
			attributes.stream()
					.filter(a -> attribute.equals(a.getName()))
					.findAny()
					.orElseThrow(() -> new InvalidInstanceException(format("Attribute '%s' is not known in the Dataset.", attribute)));
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
		StringBuilder output = new StringBuilder();
		for (Instance instance : instances) {
			output.append("[").append(instance).append("]\n");
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
			for (Entry<String, Object> value : instance.entrySet()) {
				instanceClone.put(value.getKey(), value.getValue());
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
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Dataset) {
			Dataset other = (Dataset) o;
			return ListUtils.isEqualList(instances, other.getInstances()) && ListUtils.isEqualList(attributes, other.getAttributes());
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		return new HashCodeBuilder(383, 9481).append(instances).append(attributes).toHashCode();
	}
}
