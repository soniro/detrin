package de.soniro.detrin;

import de.soniro.detrin.model.DecisionTree;

/**
 * A pruning method pruns a decision tree and provides information for the result.
 * 
 * @author Nina Rothenberg
 *
 */
public interface PruningMethod extends Extensionable, Explainable {

	/**
	 * Prunes the tree.
	 * 
	 * @param pruningInput - input parameter
	 * @return the pruned tree
	 */
	DecisionTree pruneTree(PruningInput pruningInput);
	
}
