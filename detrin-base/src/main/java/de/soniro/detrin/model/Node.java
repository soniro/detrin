package de.soniro.detrin.model;

import java.util.ArrayList;
import java.util.List;

/**
 * A node is a specifig decisionTree.
 * It has several branches.
 * 
 * @author Nina Rothenberg
 *
 */
public class Node extends DecisionTree {

	private List<Branch> branches = new ArrayList<Branch>();

	public Node(Attribute<?> attribute) {
		super(attribute);
	}
	
	@Override
	public int getDepth() {
		int depth = 0;
		for (Branch branch : branches) {
			DecisionTree child = branch.getChild();
			if (child.getDepth() > depth) {
				depth = child.getDepth();
			}
		}
		return depth + 1;
	}
	
	@Override
	public String getLabel() {
		return attribute.getName();
	}

	public List<Branch> getBranches() {
		return branches;
	}

	@Override
	public void accept(DecisionTreeVisitor visitor) {
		visitor.visit(this);
	}
	
	public void addBranch(Branch branch) {
		branches.add(branch);
	}

	@Override
	public String classify(Instance instance) {
		String branchValue = instance.getValueForAttribute(attribute).toString();
		for (Branch branch : branches) {
			if (attribute instanceof NominalAttribute && branch.getLabel(null).equals(branchValue)) {
				return branch.getChild().classify(instance);
			} else if (attribute instanceof NumericAttribute && branch.getLabel(null).equals(((NumericAttribute) attribute).getGroupForValue(Double.valueOf(branchValue)).toString())) {
				return branch.getChild().classify(instance);
			}
		}
		throw new RuntimeException(String.format("No branch found for value %s in node %s", branchValue, getLabel()));
	}
	
}
