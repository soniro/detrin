package de.soniro.detrin;

import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.DecisionTree;

/**
 * Input-Object for the Stop-Criterion.
 * 
 * @author Nina Rothenberg
 *
 */
public class StopInput {
	
	private Dataset trainingsset;
	
	private Attribute<?> targetAttribute;
	
	private DecisionTree tree;
	
	public StopInput(Dataset trainingsset, Attribute<?> targetAttribute,
			DecisionTree tree) {
		this.trainingsset = trainingsset;
		this.targetAttribute = targetAttribute;
		this.tree = tree;
	}

	public Dataset getTrainingsset() {
		return trainingsset;
	}

	public Attribute<?> getTargetAttribute() {
		return targetAttribute;
	}

	public DecisionTree getTree() {
		return tree;
	}
	
}