package de.soniro.detrin.gui.tablemodel;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Instance;

public class InstanceTableModel implements TableModel {

	final List<Attribute<?>> attributes;
	final List<Instance> instances;
	
	public InstanceTableModel(Dataset dataset) {
		attributes = dataset.getAttributes();
		instances = dataset.getInstances();
	}
	
	@Override
	public void addTableModelListener(TableModelListener l) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	@Override
	public int getColumnCount() {
		return attributes.size();
	}

	@Override
	public String getColumnName(int columnIndex) {
		return attributes.get(columnIndex).getName();
	}

	@Override
	public int getRowCount() {
		return instances.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		Instance instance = instances.get(rowIndex);
		return instance.getValueForAttribute(attributes.get(columnIndex));
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener l) {
		
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		
	}
	
}
