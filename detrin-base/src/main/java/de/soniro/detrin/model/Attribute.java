package de.soniro.detrin.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

/**
 * A representation of an Attribute.
 * 
 * @author Nina Rothenberg
 *
 * @param <T> Type of the values
 */
public abstract class Attribute<T> implements Cloneable {

	protected String name;

	protected Set<T> possibleValues = new HashSet<T>();
	
	protected Set<Group<T>> groups = new HashSet<Group<T>>();
	
	protected GroupingType groupingType = GroupingType.NONE;
	
	protected boolean active = true;
	
	public Attribute(String name) {
		this.name = name;
	}

	public abstract void accept(AttributeVisitor visitor);
	
	public abstract Object clone();
	
	public Group<T> getGroupForValue(T value) {
		for (Group<T> group : getGroups()) {
			if (group.isValueInGroup(value)) {
				return group;
			}
		}
		throw new RuntimeException("There is no group available for value '" + value + "'.");
	}
	
	public String getName() {
		return name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void addPossibleValue(T value) {
		possibleValues.add(value);
	}
	
	public void addGroup(Group<T> group) {
		groups.add(group);
	}
	
	public Set<T> getPossibleValues() {
		return possibleValues;
	}
	
	public Set<Group<T>> getGroups() {
		return groups;
	}

	public List<List<T>> getAllPossibleGroups(int numberOfGroups) {
		List<List<T>> groups = new ArrayList<List<T>>();
		for (int i = 0; i < possibleValues.size(); i++) {
			// TODO: Alle möglichen Teilgruppen ermitteln und Eintrag in Liste schreiben.
		}
		return groups;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String toString() {
		return getName();
	}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Attribute) {
			Attribute<?> other = (Attribute<?>) o;
			return new EqualsBuilder().append(name, other.name).append(possibleValues, other.possibleValues)
					.append(groups, other.groups).append(groupingType, other.groupingType).append(active, other.active).isEquals();
		}
		return false;
	}
	
	@Override
	public int hashCode() {
	     return new HashCodeBuilder(57, 7).append(name).append(possibleValues).append(groups)
	    		 .append(groupingType).append(active).toHashCode();
	}
	
	public GroupingType getGroupingType() {
		return groupingType;
	}
	
	public void setGroupingType(GroupingType groupingType) {
		this.groupingType = groupingType;
	}
	
	public enum GroupingType {
		NONE("Keine"),
		MANUAL("Manuell"),
		AUTOMATIC("Automatisch");
		
		private String label;
		
		private GroupingType(String label) {
			this.label = label;
		}
		
		public String getLabel() {
			return label;
		}
		
		@Override
		public String toString() {
			return label;
		}
	}
	
}