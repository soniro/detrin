package de.soniro.detrin.gui.visualisation;

import de.soniro.detrin.model.Branch;
import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.DecisionTreeVisitor;
import de.soniro.detrin.model.Leaf;
import de.soniro.detrin.model.Node;
import edu.uci.ics.jung.graph.DelegateTree;
import edu.uci.ics.jung.graph.Tree;

public class DecisionTreeToTree implements DecisionTreeVisitor {

	private Tree<DecisionTree, Branch> tree;
	
	public Tree<DecisionTree, Branch> mapDecisionTreeToDelegateTree(DecisionTree decisionTree) {
		tree = new DelegateTree<DecisionTree, Branch>();
		tree.addVertex(decisionTree);
		decisionTree.accept(this);
		return tree;
	}

	public void visit(Node node) {
		for (Branch branch : node.getBranches()) {
			tree.addEdge(branch, node, branch.getChild());
			branch.getChild().accept(this);
		}
	}

	public void visit(Leaf leaf) {
		// Do nothing
	}
	
}
