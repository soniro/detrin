package de.soniro.detrin.standard.splitcriterion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.SplitInput;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Interval;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NumericAttribute;
import de.soniro.detrin.model.Interval.BorderType;
import de.soniro.detrin.standard.Messages;

public class GainRatio implements SplitCriterion {

	public static final String LABEL = Messages.getString("GainRatio.LABEL");
	
	private String explanation;
	
	private Attribute<?> bestAttributeForSplit;
	
	private Double bestSplit;
	
	@Override
	public Attribute<?> getBestAttributeForSplit(SplitInput input) {
		explanation = "";
		bestAttributeForSplit = null;
		bestSplit = null;
		
		InformationGain informationGainSplitCriterion = new InformationGain();
		for (Attribute<?> attribute : input.getAttributes()) {
			if (!(attribute instanceof NominalAttribute)) {
				createNominalGroups((NumericAttribute)attribute, input.getTrainingsset(), input.getTargetAttribute(), informationGainSplitCriterion);
			}
			Double informationGain;
			if (attribute instanceof NumericAttribute) {
				informationGain = informationGainSplitCriterion.getInformationGain(input.getTrainingsset(), (NumericAttribute)attribute, input.getTargetAttribute());
			} else {
				informationGain = informationGainSplitCriterion.getInformationGain(input.getTrainingsset(), attribute, input.getTargetAttribute());
			}
			Double splitEntropy;
			if (attribute instanceof NumericAttribute) {
				splitEntropy = informationGainSplitCriterion.calculateEntropy((NumericAttribute)attribute, input.getTrainingsset());
			} else {
				splitEntropy = informationGainSplitCriterion.calculateEntropy(attribute, input.getTrainingsset());
			}
			Double gainRatio = informationGain / splitEntropy;
			explanation += Messages.getString("GainRatio.CALCULATION", round(informationGain), round(splitEntropy), round(gainRatio));
			if (bestSplit == null || gainRatio > bestSplit) {
				explanation += Messages.getString("GainRatio.RATIO_IS_BETTER", attribute.getName());
				bestSplit = gainRatio;
				bestAttributeForSplit = attribute;
			} else {
				explanation += Messages.getString("GainRatio.RATIO_NOT_BETTER", attribute.getName());
			}
		}
		explanation = Messages.getString("GainRatio.BEST_SPLIT_ATTRIBUTE", bestAttributeForSplit.getName(), round(bestSplit)) + explanation;
		return bestAttributeForSplit;
	}

	private void createNominalGroups(NumericAttribute attribute, Dataset trainingsset, Attribute<?> targetAttribute, InformationGain gain) {
		List<Double> currentValues = new ArrayList<Double>();
		currentValues.addAll(trainingsset.getCurrentValuesForAttribute(attribute));
		Collections.sort(currentValues);
		Double bestThreashold = null;
		Double bestGainRatio = null;
		List<String> splitExplanations = new ArrayList<String>();
		for (int i = 0; i < currentValues.size() - 1; i++) {
			String currentSplitExplanation = "";
			attribute.getGroups().clear();
			Double first = Double.parseDouble(String.valueOf(currentValues.get(i)));
			Double second = Double.parseDouble(String.valueOf(currentValues.get(i+1)));
			Double createThreshold = (first + second) / 2;
			Interval upperBound = new Interval(createThreshold, BorderType.UPPER_BORDER);
			Interval lowerBound = new Interval(createThreshold, BorderType.LOWER_BORDER);
			attribute.addGroup(upperBound);
			attribute.addGroup(lowerBound);
			Double informationGain = gain.getInformationGain(trainingsset, attribute, targetAttribute);
			Double splitEntropy = gain.calculateEntropy(attribute, trainingsset);
			Double gainRatio = informationGain / splitEntropy;
			currentSplitExplanation += "Gain Ratio f&uuml;r die Intervalle " + lowerBound.toString() + " und " + upperBound.toString() + " = " + round(gainRatio);
			if (bestGainRatio == null || bestGainRatio < informationGain) {
				bestGainRatio = gainRatio;
				bestThreashold = createThreshold;
			}
			splitExplanations.add(currentSplitExplanation);
		}
		if (currentValues.size() == 1) {
			bestThreashold = Double.parseDouble(String.valueOf(currentValues.get(0)));
			String currentSplitExplanation = "";
			attribute.getGroups().clear();
			Interval upperBound = new Interval(bestThreashold, BorderType.UPPER_BORDER);
			Interval lowerBound = new Interval(bestThreashold, BorderType.LOWER_BORDER);
			attribute.addGroup(upperBound);
			attribute.addGroup(lowerBound);
			Double informationGain = gain.getInformationGain(trainingsset, attribute, targetAttribute);
			Double splitEntropy = gain.calculateEntropy(attribute, trainingsset);
			bestGainRatio = informationGain / splitEntropy;
			currentSplitExplanation += "Gain Ratio f&uuml;r die Intervalle " + lowerBound.toString() + " und " + upperBound.toString() + " = " + round(bestGainRatio);
			splitExplanations.add(currentSplitExplanation);
		}
		attribute.getGroups().clear();
		Interval upperBound = new Interval(bestThreashold, BorderType.UPPER_BORDER);
		Interval lowerBound = new Interval(bestThreashold, BorderType.LOWER_BORDER);
		attribute.addGroup(upperBound);
		attribute.addGroup(lowerBound);
		explanation += "Der beste Split f&uuml;r das Attribut " + attribute.getName() + " sind die Intervalle "
			+ lowerBound.toString() + " und " + upperBound.toString() + ". Diese haben eine Gain Ratio von: " + round(bestGainRatio)
			+ "<br/><br/>Folgende Intervalle wurden getestet: ";	
		for (String currentSplitExplanation : splitExplanations) {
			explanation+="<br/>" + currentSplitExplanation;
		}
		explanation += "<br/><br/>";
	}

	@Override
	public String getLabel(Locale locale) {
		return LABEL;
	}

	@Override
	public String getExplanation(Locale locale) {
		return explanation;
	}
	
	private String round(Double value) {
		return String.valueOf(Math.round( value * 10000. ) / 10000.);
	}

}
