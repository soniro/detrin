package de.soniro.detrin.standard.splitcriterion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.SplitInput;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Group;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.Interval;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NumericAttribute;
import de.soniro.detrin.standard.Fraction;
import de.soniro.detrin.standard.Messages;

/**
 * The Information Gain is a popular splitting criterion.
 * This is an Implementation of this criterion which writes detailled Explanations. 
 * 
 * @author Nina Rothenberg
 */
public class InformationGain implements SplitCriterion {

	public static final String LABEL = Messages.getString("InformationGain.LABEL");

	private String explanation = "";
	
	private Attribute<?> bestAttributeForSplit;
	
	private Double bestSplit;
	
	private Double information;
	
	private String explanationForValueEntropy;
	
	private List<ExplanationInfo> explanations = new ArrayList<ExplanationInfo>();
	
	
	@Override
	public Attribute<?> getBestAttributeForSplit(SplitInput input) {
		explanation = "";
		bestAttributeForSplit = null;
		bestSplit = null;
		information = null;
		
		validate(input.getTargetAttribute(), input.getAttributes());
		for (Attribute<?> attribute : input.getAttributes()) {
			Double informationGain = getInformationGain(input.getTrainingsset(), attribute, input.getTargetAttribute());
			if (bestSplit == null || informationGain > bestSplit) {
				bestSplit = informationGain;
				bestAttributeForSplit = attribute;
			}
		}
		if (bestAttributeForSplit != null) {
			String bestSplitDetail = "";
			String rest = "";
			for (ExplanationInfo explanationInfo : explanations) {
				if(explanationInfo.name.equals(bestAttributeForSplit.getName())) {
					bestSplitDetail = explanationInfo.informationGainDetailedCalculation ;
				} else {
					rest += Messages.getString("explanation.othersInformationGain", explanationInfo.name, explanationInfo.informationGainResult);
				}
			}
			explanation = Messages.getString("explanation.summary", bestAttributeForSplit.getName(), bestSplit);
			explanation += bestSplitDetail;
			explanation += Messages.getString("explanation.othersInformationGainStart");
			explanation += rest;
			explanations.clear();
		}
		return bestAttributeForSplit;
	}
	
	public Double getInformationGain(Dataset trainingsset, Attribute<?> attribute, Attribute<?> targetAttribute) {
		ExplanationInfo explanationInfo = new ExplanationInfo();
		explanationInfo.name = attribute.getName();
		String entrophyForAttribute = "";
		
		Double informationGain = 0D;
		Double entropyForAttribute = 0D;
		if (information == null) information = calculateEntropy(targetAttribute, trainingsset);
		
		Map<String, Fraction> probabilitiesForAttributeValues = getProbabilitiesForAttributeValues(trainingsset, attribute);
		boolean first = true;
		String explanatioForAttributeEntropy = "";
		for (String attributeValue : ((NominalAttribute)attribute).getPossibleValues()) {
			if (first) {
				explanatioForAttributeEntropy += Messages.getString("explanation.entropyValue", attribute.getName());
				first = false;
			} else {
				explanatioForAttributeEntropy += " + ";
			} 
			explanationForValueEntropy = Messages.getString("explanation.entropyValue", attributeValue);
			Fraction probability = probabilitiesForAttributeValues.get(attributeValue) == null ? Fraction.ZERO : probabilitiesForAttributeValues.get(attributeValue);
			Double entropyForSubset = calculateEntropy(targetAttribute, trainingsset.getSubsetForAttributeValue(attribute, attributeValue));
			entropyForAttribute += probability.doubleValue() * entropyForSubset; 
			explanationForValueEntropy += Messages.getString("explanation.entropyResult", round(entropyForSubset));
			explanatioForAttributeEntropy += Messages.getString("explanation.entropyPart", probability, entropyForSubset);
			explanationInfo.informationGainDetailedCalculation += explanationForValueEntropy + "<br/>";
		}
		explanationInfo.informationGainDetailedCalculation += Messages.getString("explanation.entropyResultTest", attribute.getName());
		explanationInfo.informationGainDetailedCalculation += explanatioForAttributeEntropy;
		explanationInfo.informationGainDetailedCalculation += Messages.getString("explanation.entropyResult", entropyForAttribute) + "<br/>";
		explanationInfo.informationGainDetailedCalculation += entrophyForAttribute;
		informationGain = information - entropyForAttribute;
		explanationInfo.informationGainDetailedCalculation += Messages.getString("explanation.informationGainInfo");
		explanationInfo.informationGainDetailedCalculation += Messages.getString("explanation.informationGainStart", attribute.getName(), round(information), round(entropyForAttribute), round(informationGain));
		explanationInfo.informationGainResult = round(informationGain);
		explanations.add(explanationInfo);
		return informationGain;
	}
	
	public Double getInformationGain(Dataset trainingsset, NumericAttribute attribute, Attribute<?> targetAttribute) {
		Double informationGain = 0D;
		Double entropyForAttribute = 0D;
		if (information == null) information = calculateEntropy(targetAttribute, trainingsset);
		String calculation = "";
		Map<Interval, Fraction> probabilitiesForAttributeValues = getProbabilitiesForAttributeValues(trainingsset, attribute);
		for (Group<Double> group : attribute.getGroups()) {
			if (!calculation.isEmpty()) calculation += Messages.getString("InformationGain.ADD");
			Fraction probability = probabilitiesForAttributeValues.get(group) == null ? Fraction.ZERO : probabilitiesForAttributeValues.get(group);
			Double entropyForSubset = calculateEntropy(targetAttribute, trainingsset.getSubsetForAttributeGroup(attribute, group));
			entropyForAttribute += probability.doubleValue() * entropyForSubset;
			calculation += probability.toString() + Messages.getString("InformationGain.MULTIPLY") + entropyForSubset;
		}
		informationGain = information - entropyForAttribute;
		return informationGain;
	}
	
	public Double calculateEntropy(Attribute<?> targetAttribute, Dataset trainingsset) {
		Double entropy = 0D;
		Map<String, Fraction> probabilitiesForTargetValue = getProbabilitiesForAttributeValues(trainingsset, targetAttribute); 
		for (String attributeValue : ((NominalAttribute)targetAttribute).getPossibleValues()) {
			Fraction probability = probabilitiesForTargetValue.get(attributeValue) == null ? Fraction.ZERO : probabilitiesForTargetValue.get(attributeValue);
			if (probability == Fraction.ZERO) continue;
			entropy -= probability.doubleValue() * Math.log(probability.doubleValue()) / Math.log(2);
			explanationForValueEntropy += Messages.getString("explanation.entropySingle", probability);
		}
		return entropy;
	}

	public Double calculateEntropy(NumericAttribute targetAttribute, Dataset trainingsset) {
		Double entropy = 0D;
		Map<Interval, Fraction> probabilitiesForTargetValue = getProbabilitiesForAttributeValues(trainingsset, targetAttribute); 
		for (Group<Double> interval : targetAttribute.getGroups()) {
			Fraction probability = probabilitiesForTargetValue.get(interval) == null ? Fraction.ZERO : probabilitiesForTargetValue.get(interval);
			if (probability == Fraction.ZERO) continue;
			entropy -= probability.doubleValue() * Math.log(probability.doubleValue()) / Math.log(2); 
		}
		return entropy;
	}
	
	private void validate(Attribute<?> targetAttribute, List<Attribute<?>> attributes) {
		if (!(targetAttribute instanceof NominalAttribute)) {
			throw new RuntimeException(Messages.getString("InformationGain.NO_NOMINAL_TARGETATTRIBUTE"));
		}
		for (Attribute<?> attribute : attributes) {
			if (!(attribute instanceof NominalAttribute)) {
				attribute = ((NumericAttribute) attribute).toNominalAttribute();
			}	
		}
	}
	
	private Map<String, Fraction> getProbabilitiesForAttributeValues(Dataset trainingsset, Attribute<?> attribute) {
		Map<String, Fraction> probabilitiesForAttributeValues = new HashMap<String, Fraction>();
		Map<String, Long> targetValueCount = new HashMap<String, Long>();
		for (Instance instance : trainingsset.getInstances()) {
			Long currentCount = targetValueCount.get(instance.getValueForAttribute(attribute));
			if (currentCount == null) currentCount = 0L;
			if (!(attribute instanceof NominalAttribute)) throw new RuntimeException(Messages.getString("InformationGain.INFORMATION_GAIN_JUST_WITH_NOMINAL"));
			targetValueCount.put(instance.getValueForAttribute((NominalAttribute)attribute), currentCount + 1);
		}
		for (Entry<String, Long> entry : targetValueCount.entrySet()) {
			probabilitiesForAttributeValues.put(entry.getKey(), new Fraction(entry.getValue(), Long.valueOf(trainingsset.size())));
		}
		return probabilitiesForAttributeValues;
	}
	
	private Map<Interval, Fraction> getProbabilitiesForAttributeValues(Dataset trainingsset, NumericAttribute attribute) {
		Map<Interval, Fraction> probabilitiesForAttributeValues = new HashMap<Interval, Fraction>();
		Map<Interval, Long> targetValueCount = new HashMap<Interval, Long>();
		for (Instance instance : trainingsset.getInstances()) {
			Double value = Double.parseDouble(String.valueOf(instance.getValueForAttribute(attribute)));
			Group<Double> currentInterval = attribute.getGroupForValue(value);
			Long currentCount = targetValueCount.get(currentInterval);
			if (currentCount == null) currentCount = 0L;
			targetValueCount.put((Interval)currentInterval, currentCount + 1);
		}
		for (Entry<Interval, Long> entry : targetValueCount.entrySet()) {
			probabilitiesForAttributeValues.put(entry.getKey(), new Fraction(entry.getValue(), Long.valueOf(trainingsset.size())));
		}
		return probabilitiesForAttributeValues;
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
	
	private class ExplanationInfo {
		
		public String name;
		public String informationGainResult;
		public String informationGainDetailedCalculation = "";
		
	}

}
