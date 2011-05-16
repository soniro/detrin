package de.soniro.detrin.standard.stopcriterion;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.soniro.detrin.StopCriterion;
import de.soniro.detrin.StopInput;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.Leaf;

public class MaximumTreeDepth implements StopCriterion {
	
	public static final int DEFAULT_MAXIMUM_TREE_DEPTH = 5;

	public static final String LABEL = "Maximum Tree Depth";
	
	private int maximumTreeDepth;
	
	public MaximumTreeDepth() {
		maximumTreeDepth = DEFAULT_MAXIMUM_TREE_DEPTH;
	}
	
	public MaximumTreeDepth(int maximumTreeDepth) {
		this.maximumTreeDepth = maximumTreeDepth;
	}
	
	@Override
	public boolean fulfillStopCriterion(StopInput input) {
		boolean fulfillFirstCriterion = new AllInstancesFromSameClass().fulfillStopCriterion(input);
		if (fulfillFirstCriterion) {
			return fulfillFirstCriterion;
		} else {
			/*
			 * maximumTreeDepth - 1, because of the Leaf wich is append when stopCriterium is reached.
			 */
			return input.getTree() != null && input.getTree().getDepth() >= (maximumTreeDepth - 1);
		}
	}

	@Override
	public String getLabel(Locale locale) {
		return LABEL;
	}

	@Override
	public String getExplanation(Locale locale) {
		return "";
	}

	@Override
	public Leaf getLeafForStopCriterion(StopInput input) {
		Map<String, Integer> targetAttributeValueCount = new HashMap<String, Integer>();
		for (Instance instance : input.getTrainingsset().getInstances()) {
			String valueForAttribute = instance.getValueForAttribute(input.getTargetAttribute()).toString();
			Integer currentCount = 0;
			if (targetAttributeValueCount.containsKey(valueForAttribute)) {
				currentCount = targetAttributeValueCount.get(valueForAttribute);
			}
			targetAttributeValueCount.put(valueForAttribute, ++currentCount);
		}
		Integer highestCount = 0;
		String bestTargetAttributeValue = null;
		for (Entry<String, Integer> targetAttributeValue : targetAttributeValueCount.entrySet()) { 
			if (targetAttributeValue.getValue() > highestCount) {
				bestTargetAttributeValue = targetAttributeValue.getKey();
			}
		}
		return new Leaf(input.getTargetAttribute(), bestTargetAttributeValue);
	}

}
