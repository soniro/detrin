package de.soniro.detrin.standard.pruningmethods;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.soniro.detrin.PruningInput;
import de.soniro.detrin.PruningMethod;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.Leaf;
import de.soniro.detrin.model.Node;

public class ErrorBasedPruning implements PruningMethod {

	@Override
	public DecisionTree pruneTree(PruningInput pruningInput) {
		DecisionTree decisionTree = pruningInput.getDecisionTree();
		Dataset trainingsset = pruningInput.getTrainingsset();
		Dataset testset = pruningInput.getTestset();
		Attribute<?> targetAttribute = pruningInput.getTargetAttribute();
		if (decisionTree instanceof Leaf || trainingsset.size() < 1) return decisionTree;
		DecisionTree prunedDecisionTree = new Leaf(targetAttribute, getMostLikelyLeafValue(decisionTree, trainingsset, targetAttribute));
		Branch maxChild = getMaxChild((Node) decisionTree);
		Double upperBoundForSubtree = getUpperBoundOfConfidenceInterval(decisionTree, testset, targetAttribute);
		Double upperBoundForPrunedSubtree = getUpperBoundOfConfidenceInterval(decisionTree, testset, targetAttribute);
		Double upperBoundForMaxChild = getUpperBoundOfConfidenceInterval(maxChild.getChild(), testset.getSubsetForAttributeValue(decisionTree.getAttribute(),
				maxChild.getLabel(null)), targetAttribute);
		/*
		 * Pruned tree has lowest error rate.
		 */
		if (upperBoundForPrunedSubtree <= upperBoundForSubtree && upperBoundForPrunedSubtree <= upperBoundForMaxChild) {
			decisionTree = prunedDecisionTree;
			String newExplanation;
			if (upperBoundForMaxChild == upperBoundForSubtree) {
				newExplanation = "Der vorherige Baum wurde an dieser Stelle auf das Blatt " + decisionTree.getLabel()
						+ " gekürzt, weil mit diesem Blatt die Fehlerrate gleich ist.\nFehlerrate vorheriger Baum: " + upperBoundForSubtree
						+ "\nFehlerrate des Blattes: " + upperBoundForPrunedSubtree;
			} else {
				newExplanation = "Der vorherige Baum wurde an dieser Stelle auf das Blatt " + decisionTree.getLabel()
						+ " gekürzt, weil mit diesem Blatt die Fehlerrate gleich ist und somit der Baum ohne Qualitätsverlust vereinfacht werden kann.\nFehlerrate: "
						+ upperBoundForSubtree;
			}
			decisionTree.setExplanation(newExplanation);
		/*
		 * MaxChild has lowest error rate.
		 */
		} else if (upperBoundForMaxChild <= upperBoundForSubtree && upperBoundForMaxChild <= upperBoundForPrunedSubtree) {
			decisionTree = maxChild.getChild();
			String currentExplanation = maxChild.getExplanation(null);
			String newExplanation;
			if (upperBoundForMaxChild == upperBoundForSubtree) {
				newExplanation = "Der vorherige Baum wurde an dieser Stelle durch den wahrscheinlichsten Kindknoten " + decisionTree.getLabel()
						+ " ersetzt, weil bei diesem die Fehlerrate gleich ist und somit der Baum ohne Qualitätsverlust vereinfacht werden kann.\nFehlerrate: "
						+ upperBoundForSubtree;
			} else {
				newExplanation = "Der vorherige Baum wurde an dieser Stelle durch den wahrscheinlichsten Kindknoten " + decisionTree.getLabel()
						+ " ersetzt, weil bei diesem die Fehlerrate kleiner ist.\nFehlerrate vorheriger Baum: " + upperBoundForSubtree
						+ "\nFehlerrate des Kindes: " + upperBoundForMaxChild;
			}
			decisionTree.setExplanation(newExplanation + "\n\n" + currentExplanation);
		}
		if (decisionTree instanceof Node) {
			for (Branch branch : ((Node) decisionTree).getBranches()) {
				PruningInput newPruningInput = new PruningInput();
				newPruningInput.setDecisionTree(branch.getChild());
				newPruningInput.setTrainingsset(trainingsset.getSubsetForAttributeValue(decisionTree.getAttribute(), branch.getLabel(null)));
				newPruningInput.setTestset(testset.getSubsetForAttributeValue(decisionTree.getAttribute(), branch.getLabel(null)));
				newPruningInput.setTargetAttribute(targetAttribute);
				branch.setChild(pruneTree(newPruningInput));
			}
		}
		return decisionTree;
	}

	private Branch getMaxChild(Node decisionTree) {
		Branch maxChild = null;
		int maxChildSize = 0;
		for (Branch branch : decisionTree.getBranches()) {
			if (branch.getNumberOfInstances() > maxChildSize) {
				maxChildSize = branch.getNumberOfInstances();
				maxChild = branch;
			}
		}
		return maxChild;
	}

	public String getMostLikelyLeafValue(DecisionTree decisionTree, Dataset dataset, Attribute<?> targetAttribute) {
		Map<String, Integer> instanceCount = new HashMap<String, Integer>();
		for (Instance instance : dataset.getInstances()) {
			String currentValue = instance.getValueForAttribute(targetAttribute).toString();
			Integer currentCount = instanceCount.get(currentValue);
			if (currentCount == null) currentCount = 0;
			instanceCount.put(currentValue, ++currentCount);
		}
		Integer maxCount = 0;
		String mostLikelyLeaf = null;
		for (Entry<String, Integer> entry : instanceCount.entrySet()) {
			if (entry.getValue() > maxCount) {
				maxCount = entry.getValue();
				mostLikelyLeaf = entry.getKey();
			}
		}
		return mostLikelyLeaf;
	}

	private Double getUpperBoundOfConfidenceInterval(DecisionTree decisionTree, Dataset testset, Attribute<?> targetAttribute) {
		Double misclassificationRate = getMisclassificationRate(decisionTree, testset, targetAttribute);
		Double sqrt = Math.sqrt((misclassificationRate * (1 - misclassificationRate)) / testset.size());
		Double z = 1D;
		return misclassificationRate + (z * sqrt);
	}

	private Double getMisclassificationRate(DecisionTree decisionTree, Dataset testset, Attribute<?> targetAttribute) {
		int numberOfFalseClassifications = 0;
		for (Instance instance : testset.getInstances()) {
			String valueTargetAttribute = decisionTree.classify(instance);
			if (!valueTargetAttribute.equals(instance.getValueForAttribute(targetAttribute))) {
				numberOfFalseClassifications++;
			}
		}
		return new Double(numberOfFalseClassifications) / testset.size();
	}

	@Override
	public String getLabel(Locale locale) {
		return "Error-Based Pruning";
	}

	@Override
	public String getExplanation(Locale locale) {
		return "";
	}

}
