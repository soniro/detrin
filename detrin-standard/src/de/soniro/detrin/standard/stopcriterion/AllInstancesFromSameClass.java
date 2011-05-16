package de.soniro.detrin.standard.stopcriterion;

import java.util.Locale;

import de.soniro.detrin.StopCriterion;
import de.soniro.detrin.StopInput;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.Leaf;
import de.soniro.detrin.standard.Messages;

public class AllInstancesFromSameClass implements StopCriterion {
	
	public static final String LABEL = "All Instances are in the same class";
	
	private Leaf leaf;
	private String explanation;
	
	@Override
	public boolean fulfillStopCriterion(StopInput input) {
		String value = null;
		for (Instance instance : input.getTrainingsset().getInstances()) {
			if (value == null) {
				value = instance.getValueForAttribute(input.getTargetAttribute()).toString();
			} else if (!value.equals(instance.getValueForAttribute(input.getTargetAttribute()).toString())) {
				return false;
			}
		}
		if (value != null) leaf = new Leaf(input.getTargetAttribute(), value);
		explanation = Messages.getString("explanation.leaf.explain", value);
		return true;
	}

	@Override
	public String getLabel(Locale locale) {
		return LABEL;
	}

	@Override
	public String getExplanation(Locale locale) {
		return explanation;
	}

	@Override
	public Leaf getLeafForStopCriterion(StopInput input) {
		leaf = null;
		fulfillStopCriterion(input);
		Leaf result = leaf;
		leaf = null;
		return result;
	}

}
