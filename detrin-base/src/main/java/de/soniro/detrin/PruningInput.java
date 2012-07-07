package de.soniro.detrin;

import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.DecisionTree;

/**
 * The Input-Object for the Pruning-Method.
 * This class should contain every information a pruning method could need for prune the tree.
 * 
 * @author Nina Rothenberg
 */
public class PruningInput {
	
	private DecisionTree decisionTree;
	private Dataset trainingsset;
	private Dataset testset;
	private Attribute<?> targetAttribute;
	
	public DecisionTree getDecisionTree() {
		return decisionTree;
	}
	public void setDecisionTree(DecisionTree decisionTree) {
		this.decisionTree = decisionTree;
	}
	public Dataset getTrainingsset() {
		return trainingsset;
	}
	public void setTrainingsset(Dataset trainingsset) {
		this.trainingsset = trainingsset;
	}
	public Dataset getTestset() {
		return testset;
	}
	public void setTestset(Dataset testset) {
		this.testset = testset;
	}
	public Attribute<?> getTargetAttribute() {
		return targetAttribute;
	}
	public void setTargetAttribute(Attribute<?> targetAttribute) {
		this.targetAttribute = targetAttribute;
	}
	
}
