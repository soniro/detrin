package de.soniro.detrin.gui.elements;

import java.util.Arrays;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * SortedComboBox.
 * 
 * This ComboBox sorts its items initially.
 * 
 * @author Nina Rothenberg
 *
 */
public class SortedComboBox extends JComboBox {

	private static final long serialVersionUID = 1461778148990186482L;

	public SortedComboBox(Object... items) {
		super();
		this.setModel(new SortedComboBoxModel(items));
	}
	
	/**
	 * SortedComboBoxModel.
	 * 
	 * It exends the {@link DefaultComboBoxModel} by sorting the items given by the constructor.
	 * To not destroy the sorting, no element should be added later.
	 * 
	 * @author Nina Rothenberg
	 */
	class SortedComboBoxModel extends DefaultComboBoxModel {
		
		private static final long serialVersionUID = -5939287407450726645L;

		private SortedComboBoxModel(Object... items) {
			super();
			Arrays.sort(items);
			for (int i = 0; i < items.length; i++){
				super.addElement(items[i]);
			}
		}
		
		@Override
		public void addElement(Object anObject) {
	        throw new UnsupportedOperationException("Kein Element darf hinzugefügt werden, denn es wird nicht sortiert.");
	    }

	    @Override
	    public void insertElementAt(Object anObject,int index) {
	        throw new UnsupportedOperationException("Kein Element darf an eine bestimmte Stelle hinzugefügt werden, denn es wird nicht sortiert.");	    	
	    }
		
	}
}
