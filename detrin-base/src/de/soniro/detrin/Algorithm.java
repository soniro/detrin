package de.soniro.detrin;

import java.util.Set;

/**
 * The Algorithm-Interface represents Algorithm.
 * All classes in the ClassPath implementing this interface are options in the GUI.
 *  
 * @author Nina Rothenberg
 *
 */
public interface Algorithm extends Extensionable { 
	
	/**
	 * Returns the most recommended Split-Criterion of the algorithm.
	 * 
	 * @return Type of the primary Split-Criterion.
	 */
	Class<? extends SplitCriterion> getPrimarySplitCriterion();

	/**
	 * Returns the most recommended Stop-Criterion of the algorithm.
	 * 
	 * @return Type of the primary Stop-Criterion.
	 */
	Class<? extends StopCriterion> getPrimaryStopCriterion();

	/**
	 * Returns the most recommended Pruning-Method of the algorithm.
	 * 
	 * @return Type of the primary Pruning-Method.
	 */
	Class<? extends PruningMethod> getPrimaryPruningMethod();
	
	/**
	 * Returns all possible Split-Criteria. if the result is <code>null</code> it doesn't
	 * matter which criterion is chosen. If the List is empty no split-criterion have to
	 * be selected. 
	 * 
	 * @return all possible Split-Criteria
	 */
	Set<Class<? extends SplitCriterion>> getAllPossibleSplitCriteria();

	/**
	 * Returns all possible Stop-Criteria. if the result is <code>null</code> it doesn't
	 * matter which criterion is chosen. If the List is empty no split-criterion have to
	 * be selected. 
	 * 
	 * @return all possible Stop-Criteria
	 */
	Set<Class<? extends StopCriterion>> getAllPossibleStopCriteria();

	/**
	 * Returns all possible Pruning-Methods. if the result is <code>null</code> it doesn't
	 * matter which method is chosen. If the List is empty no pruning method have to
	 * be selected. 
	 * 
	 * @return all possible Stop-Criteria
	 */
	Set<Class<? extends PruningMethod>> getAllPossiblePruningMethods();
	
}
