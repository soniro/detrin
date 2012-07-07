package de.soniro.detrin.gui.tablemodel;

import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Instance;

public class InstanceTableModel implements TableModel {

	private final List<Attribute<?>> attributes;
	private final List<Instance> instances;

	public InstanceTableModel(Dataset dataset) {
		attributes = dataset.getAttributes();
		instances = dataset.getInstances();
	}

	public void addTableModelListener(TableModelListener l) {

	}

	public Class<?> getColumnClass(int columnIndex) {
		return Object.class;
	}

	public int getColumnCount() {
		return attributes.size();
	}

	public String getColumnName(int columnIndex) {
		return attributes.get(columnIndex).getName();
	}

	public int getRowCount() {
		return instances.size();
	}

	public Object getValueAt(int rowIndex, int columnIndex) {
		Instance instance = instances.get(rowIndex);
		return instance.getValueForAttribute(attributes.get(columnIndex));
	}

	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return false;
	}

	public void removeTableModelListener(TableModelListener l) {

	}

	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {

	}

}
