package de.soniro.detrin;

import de.soniro.detrin.model.Leaf;

/**
 * Interface for a stop-criterion.
 * This interface needs to be implemented if you want to add a new Stop-Criterion.
 * 
 * @author Nina Rothenberg
 */
public interface StopCriterion extends Extensionable, Explainable {

	/**
	 * Checks if the stop-criterion is reached.
	 * @param input
	 * @return
	 */
	boolean fulfillStopCriterion(StopInput input);
	
	/**
	 * Returns the best possible Leaf for a subtree.
	 * 
	 * @param input
	 * @return
	 */
	Leaf getLeafForStopCriterion(StopInput input);
	
}
