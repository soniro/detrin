package de.soniro.detrin.standard.algorithms;

import java.util.Locale;
import java.util.Set;

import de.soniro.detrin.Algorithm;
import de.soniro.detrin.PruningMethod;
import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.StopCriterion;
import de.soniro.detrin.standard.Messages;
import de.soniro.detrin.standard.pruningmethods.ErrorBasedPruning;
import de.soniro.detrin.standard.splitcriterion.GainRatio;
import de.soniro.detrin.standard.stopcriterion.AllInstancesFromSameClass;

/**
 * The C4.5 algorithm.
 * Gives back a label and all properties.
 * 
 * @author Nina Rothenberg
 */
public class C45 implements Algorithm {

	@Override
	public String getLabel(Locale locale) {
		return Messages.getString("C45.label");
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
		return ErrorBasedPruning.class;
	}

	@Override
	public Class<? extends SplitCriterion> getPrimarySplitCriterion() {
		return GainRatio.class;
	}

	@Override
	public Class<? extends StopCriterion> getPrimaryStopCriterion() {
		return AllInstancesFromSameClass.class;
	}

}
