package de.soniro.detrin.model;

import de.soniro.detrin.PruningMethod;
import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.StopCriterion;

/**
 * The {@link Properties} class represents the objects which were sent to generate a 
 * decision tree. They contains all properties. 
 * 
 * @author Nina Rothenberg
 */
public class Properties implements Cloneable {

	private Dataset dataset;

	private Attribute<?> targetAttribute;

	private SplitCriterion splitCriterion;

	private StopCriterion stopCriterion;

	private PruningMethod pruningMethod;

	private Long minimalInstanceCount;
	
	public Dataset getDataset() {
		return dataset;
	}
	
	public void setDataset(Dataset dataset) {
		this.dataset = dataset;
	}
	
	public Attribute<?> getTargetAttribute() {
		return targetAttribute;
	}
	
	public void setTargetAttribute(Attribute<?> targetAttribute) {
		this.targetAttribute = targetAttribute;
	}
	
	public SplitCriterion getSplitCriterion() {
		return splitCriterion;
	}
	
	public void setSplitCriterion(SplitCriterion splitCriterion) {
		this.splitCriterion = splitCriterion;
	}
	
	public StopCriterion getStopCriterion() {
		return stopCriterion;
	}
	
	public void setStopCriterion(StopCriterion stopCriterion) {
		this.stopCriterion = stopCriterion;
	}
	
	public PruningMethod getPruningMethod() {
		return pruningMethod;
	}
	
	public void setPruningMethod(PruningMethod pruningMethod) {
		this.pruningMethod = pruningMethod;
	}
	
	public Long getMinimalInstanceCount() {
		return minimalInstanceCount;
	}
	
	public void setMinimalInstanceCount(Long minimalInstanceCount) {
		this.minimalInstanceCount = minimalInstanceCount;
	}
	
	@Override
	public Object clone() {
		Properties clone = new Properties();
		clone.setDataset((Dataset) dataset.clone());
		clone.setMinimalInstanceCount(minimalInstanceCount);
		clone.setPruningMethod(pruningMethod);
		clone.setSplitCriterion(splitCriterion);
		clone.setStopCriterion(stopCriterion);
		clone.setTargetAttribute(clone.getDataset().getAttributeByName(targetAttribute.getName()));
		return clone;
	}
	
}
