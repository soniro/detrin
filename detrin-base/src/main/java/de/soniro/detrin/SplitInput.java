package de.soniro.detrin;

import java.util.List;

import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;

/**
 * A Split-Input is the input object providing all information needed 
 * to find the best split.
 * 
 * @author Nina Rothenberg
 */
public class SplitInput {

	private Dataset trainingsset;
	private List<Attribute<?>> attributes;
	private Attribute<?> targetAttribute;
	
	public SplitInput(Dataset trainingsset, List<Attribute<?>> attributes, Attribute<?> targetAttribute) {
		this.trainingsset = trainingsset;
		this.attributes = attributes;
		this.targetAttribute = targetAttribute;
	}
	
	public Dataset getTrainingsset() {
		return trainingsset;
	}
	
	public List<Attribute<?>> getAttributes() {
		return attributes;
	}
	
	public Attribute<?> getTargetAttribute() {
		return targetAttribute;
	}
}
