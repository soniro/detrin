package de.soniro.detrin.core;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.PropertyResourceBundle;

import de.soniro.detrin.PruningInput;
import de.soniro.detrin.SplitInput;
import de.soniro.detrin.StopInput;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.Group;
import de.soniro.detrin.model.Leaf;
import de.soniro.detrin.model.Node;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NumericAttribute;
import de.soniro.detrin.model.Properties;

/**
 * The core component by generating a {@link DecisionTree}. Loads extensions and handles 
 * the logic to call the different steps in the decisiontreegeneration-process.
 * 
 * @author Nina Rothenberg
 *
 */
public class DecisionTreeInducer {
	
	private final PluginClassLoader pluginClassLoader;
	
	private static final String ERROR_FILE_NOT_FOUND = "Properties-Datei für Plugin-Ordner konnte nicht gefunden werden";
	
	private static final String ERROR_IO_EXCEPTION = "Properties-Datei für Plugin-Ordner konnte nicht gelesen werden";
	
	public DecisionTreeInducer() {
		try {
			String pluginFolder = new PropertyResourceBundle(new BufferedReader(new FileReader("./config.properties"))).getString("PLUGIN_FOLDER");
			pluginClassLoader = new PluginClassLoader(pluginFolder.split(","));
		} catch (FileNotFoundException e) {
			throw new RuntimeException(ERROR_FILE_NOT_FOUND, e);
		} catch (IOException e) {
			throw new RuntimeException(ERROR_IO_EXCEPTION, e);
		}
	}

	public DecisionTree induceDecisionTree(Properties properties) {
		DecisionTree decisionTree = induceDecisionTree(properties, getActiveAttributes(properties));
		if (properties.getPruningMethod() != null) {
			decisionTree = pruneDecisionTree(decisionTree, properties);
		} 
		return decisionTree;
	}
	
	private List<Attribute<?>> getActiveAttributes(Properties properties) {
		List<Attribute<?>> attributes = new ArrayList<Attribute<?>>();
		for (Attribute<?> attribute : properties.getDataset().getAttributes()) {
			if (attribute.isActive()) {
				attributes.add(attribute);
			}
		}
		attributes.remove(properties.getTargetAttribute());
		return attributes;
	}
	
	private DecisionTree pruneDecisionTree(DecisionTree decisionTree, Properties properties) {
		PruningInput pruningInput = new PruningInput();
		pruningInput.setDecisionTree(decisionTree);
		pruningInput.setTrainingsset(properties.getDataset());
		pruningInput.setTestset(properties.getDataset());
		pruningInput.setTargetAttribute(properties.getTargetAttribute());
		return properties.getPruningMethod().pruneTree(pruningInput);
	}
	
	private DecisionTree induceDecisionTree(Properties properties, List<Attribute<?>> attributes) {
		Dataset trainingsset = properties.getDataset();
		Attribute<?> targetAttribute = properties.getTargetAttribute();
		StopInput stopInput = new StopInput(trainingsset, targetAttribute, null);
		
		if (properties.getStopCriterion().fulfillStopCriterion(stopInput)) {
			Leaf leaf = properties.getStopCriterion().getLeafForStopCriterion(stopInput);
			leaf.setExplanation(properties.getStopCriterion().getExplanation(null));
			return leaf;
		} else if (trainingsset.getInstances().size() < properties.getMinimalInstanceCount()) {
			String mostProperValue = trainingsset.getMostProperValueForAttribute((NominalAttribute)targetAttribute);
			Leaf leaf = new Leaf(targetAttribute, mostProperValue);
			leaf.setExplanation(String.format(Messages.TOO_FEW_INSTANCES, properties.getMinimalInstanceCount(), mostProperValue, targetAttribute.getName()));
			return leaf;
		} else if (attributes == null || attributes.isEmpty()) {
			String mostProperValue = trainingsset.getMostProperValueForAttribute((NominalAttribute)targetAttribute);
			Leaf leaf = new Leaf(targetAttribute, mostProperValue);
			leaf.setExplanation(String.format(Messages.TOO_FEW_INSTANCES, properties.getMinimalInstanceCount(), mostProperValue, targetAttribute.getName()));
			return leaf;
		}
		
		SplitInput splitInput = new SplitInput(trainingsset, attributes, targetAttribute);
		Attribute<?> attribute = properties.getSplitCriterion().getBestAttributeForSplit(splitInput);
		Node node = new Node(attribute);
		node.setExplanation(properties.getSplitCriterion().getExplanation(null));
		attributes.remove(attribute);
		if (attribute instanceof NominalAttribute) {
			NominalAttribute nominalAttribute = (NominalAttribute) attribute;
			for (String value : nominalAttribute.getPossibleValues()) {	
				Dataset newTrainingsset = trainingsset.getSubsetForAttributeValue(nominalAttribute, value);
				if (newTrainingsset.size() == 0) continue;
				properties.setDataset(newTrainingsset);
			
				DecisionTree decisionTree = induceDecisionTree(properties, new ArrayList<Attribute<?>>(attributes));
				if (decisionTree != null) {
					String explanation = Messages.getString("explanation.branch.dataset", value, newTrainingsset.size());
					if (decisionTree instanceof Leaf) {
						explanation += Messages.getString("explanation.branch.childLeaf", decisionTree.getLabel(), newTrainingsset.size());
					} else {
						explanation += Messages.getString("explanation.branch.child", decisionTree.getLabel(), newTrainingsset.size());
					}
					Branch branch = new Branch(value, decisionTree, node, newTrainingsset.size());
					branch.setExplanation(explanation);
					node.addBranch(branch);
				}
			}
		} else if (attribute instanceof NumericAttribute) {
			NumericAttribute numericAttribute = (NumericAttribute) attribute;
			for (Group<Double> interval : numericAttribute.getGroups()) {	
				Dataset newTrainingsset = trainingsset.getSubsetForAttributeGroup(numericAttribute, interval);
				if (newTrainingsset.size() == 0) continue;
				properties.setDataset(newTrainingsset);
			
				DecisionTree decisionTree = induceDecisionTree(properties, new ArrayList<Attribute<?>>(attributes));
				if (decisionTree != null) {
					String explanation = Messages.getString("explanation.branch.dataset", interval, newTrainingsset.size());
					if (decisionTree instanceof Leaf) {
						explanation += Messages.getString("explanation.branch.childLeaf", decisionTree.getLabel(), newTrainingsset.size());
					} else {
						explanation += Messages.getString("explanation.branch.child", decisionTree.getLabel(), newTrainingsset.size());
					}
					Branch branch = new Branch(interval.toString(), decisionTree, node, newTrainingsset.size());
					branch.setExplanation(explanation);
					node.addBranch(branch);
				}
			}
		}
		return node;
	}
	
	public <T> List<T> getAllPossibleInstances(Class<T> clazz) {
		List<T> possibilities = new ArrayList<T>();
		for (Class<T> currentClass : pluginClassLoader.loadClassesForInterface(clazz)) {
			try {
				possibilities.add(currentClass.newInstance());
			} catch (Exception e) {
				throw new RuntimeException(e.getMessage(), e);
			}
		}
		return possibilities;
	}
	
}