package de.soniro.detrin.standard.algorithms;

import java.util.Locale;
import java.util.Set;

import de.soniro.detrin.Algorithm;
import de.soniro.detrin.PruningMethod;
import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.StopCriterion;
import de.soniro.detrin.standard.Messages;
import de.soniro.detrin.standard.splitcriterion.InformationGain;
import de.soniro.detrin.standard.stopcriterion.AllInstancesFromSameClass;

/**
 * The ID3 algorithm.
 * Gives back a label and all properties.
 * 
 * @author Nina Rothenberg
 */
public class ID3 implements Algorithm {

	@Override
	public String getLabel(Locale locale) {
		return Messages.getString("ID3.label");
	}

	@Override
	public Set<Class<? extends PruningMethod>> getAllPossiblePruningMethods() {
		throw new RuntimeException("Not implemented yet!");
	}

	@Override
	public Set<Class<? extends SplitCriterion>> getAllPossibleSplitCriteria() {
		throw new RuntimeException("Not implemented yet!");
	}

	@Override
	public Set<Class<? extends StopCriterion>> getAllPossibleStopCriteria() {
		throw new RuntimeException("Not implemented yet!");
	}

	@Override
	public Class<? extends PruningMethod> getPrimaryPruningMethod() {
		/*
		 * It's a difference if there no pruning method should be used or 
		 * if it doesn't matter which pruning method is chosen. 
		 * What is null and how can you handle the other?
		 */
		return null;
	}

	@Override
	public Class<? extends SplitCriterion> getPrimarySplitCriterion() {
		return InformationGain.class;
	}

	@Override
	public Class<? extends StopCriterion> getPrimaryStopCriterion() {
		return AllInstancesFromSameClass.class;
	}

}
