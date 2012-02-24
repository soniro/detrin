package de.soniro.detrin.gui.tablemodel;

import javax.swing.table.DefaultTableModel;

import de.soniro.detrin.gui.DeTrInGui;

public class AttributeTableModel extends DefaultTableModel {
	
	private static final long serialVersionUID = 8153117796927119922L;

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		if (columnIndex == 0) {
			return Boolean.class;
		} else {
			return super.getColumnClass(columnIndex);
		}
	}


	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			DeTrInGui.getInstance().updateActivateAttribute((Boolean)aValue, rowIndex);
		} else if (columnIndex == 3) {
			// TODO
		}
		super.setValueAt(aValue, rowIndex, columnIndex);
	}
	
	
	
}
