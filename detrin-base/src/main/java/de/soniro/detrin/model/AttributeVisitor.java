package de.soniro.detrin.model;

/**
 * The Attribute-Visitor allows to get the right handling for a concrete type.
 * When calling the attribute-accept method with this visitor the attribute will 
 * immediately call back the right visit-method.
 * 
 * @author Nina Rothenberg
 *
 */
public interface AttributeVisitor {

	public void visit(NominalAttribute nominalAttribute);
	
	public void visit(NumericAttribute numericAttribute);
}
