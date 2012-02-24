package de.soniro.detrin.gui.panel;

import javax.swing.JDialog;

import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.AttributeVisitor;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NumericAttribute;

/**
 * Opens the right grouping popup for the attribute.
 * 
 * @author Nina Rothenberg
 */
public class GroupingPopup extends JDialog {

	private static final long serialVersionUID = 453354302276905176L;

	public GroupingPopup(Attribute<?> attribute) {
		super(DeTrInGui.getInstance(), "Gruppieren des Attributes " + attribute.getName());
		init(attribute);
	}
	
	private void init(Attribute<?> attribute) {
		setBounds((getOwner().getWidth() / 2) - 250, (getOwner().getHeight() /2) - 200, 500, 400);
		AttributeVisitor attributeVisitor = new AttributeVisitor() {
			
			@Override
			public void visit(NumericAttribute numericAttribute) {
				throw new RuntimeException("Not implemented yet!");
			}
			
			@Override
			public void visit(NominalAttribute nominalAttribute) {
				add(new GroupingPanel(GroupingPopup.this, nominalAttribute));
				setVisible(true);
			}
		};
		attribute.accept(attributeVisitor);
	}
	
}
