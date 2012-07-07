package de.soniro.detrin.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * An Interval is a specific group for a nummeric attribute.
 * 
 * @author Nina Rothenberg
 */
public class Interval extends Group<Double> {
	
	private Map<Double, Double> intervals = new HashMap<Double, Double>();
	private Map<Double, BorderType> otherIntervals = new HashMap<Double, BorderType>();
	
	public Interval() { }
	
	public Interval(Double border, BorderType type) {
		otherIntervals.put(border, type);
	}
	
	public Interval(Double lower, Double upper) {
		intervals.put(lower, upper);
	}
	
	public void add(Double border, BorderType type) {
		otherIntervals.put(border, type);		
	}
	
	public void add(Double lower, Double upper) {
		intervals.put(lower, upper);
	}
	
	@Override
	public boolean isValueInGroup(Double value) {
		for (Entry<Double, Double> interval : intervals.entrySet()) {
			if (interval.getKey() <= value && interval.getValue() > value) {
				return true;
			}
		}
		for (Entry<Double, BorderType> interval : otherIntervals.entrySet()) {
			if (interval.getValue().equals(BorderType.UPPER_BORDER) && interval.getKey() > value 
			        || interval.getValue().equals(BorderType.LOWER_BORDER) && interval.getKey() <= value) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString() {
		String result = "";
		for (Entry<Double, Double> interval : intervals.entrySet()) {
			result += "[" + interval.getKey() + ";" + interval.getValue() + "]";
		}
		for (Entry<Double, BorderType> interval : otherIntervals.entrySet()) {
				if (interval.getValue().equals(BorderType.UPPER_BORDER)) {
					result += "[&lt;=" + interval.getKey() + "]";
				} else if (interval.getValue().equals(BorderType.LOWER_BORDER)) {
					result += "[&gt;" + interval.getKey() + "]";
					
				}
		}
		return result.replace("][", "], [");
	}

	public enum BorderType {
		UPPER_BORDER, 
		LOWER_BORDER;
	}

	@Override
	public Object clone() {
		Interval clone = new Interval();
		clone.intervals = new HashMap<Double, Double>(intervals);
		clone.otherIntervals = new HashMap<Double, BorderType>(otherIntervals);
		return clone;
	}
	
}
