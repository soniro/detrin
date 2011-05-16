package de.soniro.detrin.standard.splitcriterion;

import java.util.List;
import java.util.Locale;

import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.SplitInput;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Instance;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.standard.Fraction;
import de.soniro.detrin.standard.Messages;

/**
 * The Gini Gain is an popular splitting criterion.
 * This implementation offers explainations after calculating the best split.
 * 
 * @author Nina Rothenberg
 *
 */
public class GiniIndex implements SplitCriterion {

	public static final String LABEL = Messages.getString("GiniIndex.LABEL");
	
	private String explanation = "";
	
	@Override
	public Attribute<?> getBestAttributeForSplit(SplitInput input) {
		Attribute<?> bestAttributeForSplit = null;
		Double bestSplit = 0D;
		Double gini = getGini(input.getTrainingsset(), input.getTargetAttribute());
		for (Attribute<?> attribute : input.getAttributes()) {
			if (attribute.getPossibleValues().size() > 2) {
				if (!(attribute instanceof NominalAttribute)) {
					throw new RuntimeException("Aktuell sind nur nominale Attribute im Gini Index erlaubt.");
				}
				// TODO Not fully implemented yet!
				// Group possible Values
				for (List<String> group : ((NominalAttribute)attribute).getAllPossibleGroups(2)) {
					// Calculate Gini-Index per group.
					group.toString();
				}
				Double result = 0D;
				for (String attributeValue : ((NominalAttribute)attribute).getPossibleValues()) {
					result += probability(attribute, attributeValue, input.getTrainingsset()).doubleValue() * getGini(input.getTrainingsset().getSubsetForAttributeValue(attribute, attributeValue), input.getTargetAttribute());
				}
				Double giniGain = gini - result;
				giniGain.toString();
				
			}
			Double result = 0D;
			for (String attributeValue : ((NominalAttribute)attribute).getPossibleValues()) {
				result += probability(attribute, attributeValue, input.getTrainingsset()).doubleValue() * getGini(input.getTrainingsset().getSubsetForAttributeValue(attribute, attributeValue), input.getTargetAttribute());
			}
			explanation += Messages.getString("GiniIndex.INFO", attribute.getName(), round(gini));
			Double giniGain = gini - result;
			explanation += "Somit ist der Gini Gain = " + round(giniGain) + ".\n\n";
			if (giniGain > bestSplit) {
				bestSplit = giniGain;
				bestAttributeForSplit = attribute;
			}
		}
		explanation = "Bestes Attribut f&uuml;r den Split ist das Attribut '" + bestAttributeForSplit.getName() + "' mit einem Gini Gain von " + round(bestSplit) + ".<br/><br/>" + explanation;
		return bestAttributeForSplit;
	}
	
	private Fraction probability(Attribute<?> attribute, String value, Dataset trainingsset) {
		Long valueInTrainingset = 0L;
		for (Instance instance : trainingsset.getInstances()) {
			if (instance.getValueForAttribute(attribute).equals(value)) {
				valueInTrainingset++;
			}
		}
		return new Fraction(valueInTrainingset, Long.valueOf(trainingsset.size()));
	}
	
	private Double getGini(Dataset trainingsset, Attribute<?> attribute) {
		Double result = 0D;
		for (String attributeValue : ((NominalAttribute)attribute).getPossibleValues()) {
			Fraction probability = probability(attribute, attributeValue, trainingsset);
			result += Math.pow(probability.doubleValue(), 2);
		}
		return 1 - result;
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
