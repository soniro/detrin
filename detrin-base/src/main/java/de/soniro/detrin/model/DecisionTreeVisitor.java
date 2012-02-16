package de.soniro.detrin.model;

/**
 * The {@link DecisionTreeVisitor} allows the user to visit a DecisionTree and 
 * handle it in the right way.
 * 
 * @author Nina Rothenberg
 *
 */
public interface DecisionTreeVisitor {
	
	public void visit(Node node);
	
	public void visit(Leaf leaf);

}
