package de.soniro.detrin.gui.panel;

import java.awt.Color;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.ListSelectionModel;

public class GroupingList<T> extends JList {

	private static final long serialVersionUID = 8969177268233711980L;

	private final DefaultListModel listModel;

	public GroupingList() {
		this(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
	}

	public GroupingList(int selectionMode) {
		super(new DefaultListModel());
		listModel = (DefaultListModel) getModel();
		setFixedCellWidth(100);
		setBackground(Color.WHITE);
		setSelectionMode(selectionMode);
	}

	public void addItem(T item) {
		listModel.addElement(item);
	}

	public void addItems(Collection<T> items) {
		for (T item : items) {
			listModel.addElement(item);
		}
	}

	public void removeItem(T item) {
		listModel.removeElement(item);
	}

	public void removeItems(Collection<T> items) {
		for (T item : items) {
			listModel.removeElement(item);
		}
	}

	public void clear() {
		listModel.removeAllElements();
	}

	@SuppressWarnings("unchecked")
	public T getSelectedItem() {
		return (T) getSelectedValue();
	}

	@SuppressWarnings("unchecked")
	public Set<T> getSelectedItems() {
		Set<T> selectedValues = new HashSet<T>();
		selectedValues.addAll((List<T>) Arrays.asList(getSelectedValues()));
		return selectedValues;
	}

	@SuppressWarnings("unchecked")
	public Set<T> getAllItems() {
		Set<T> allItems = new HashSet<T>();
		for (int i = 0; i < listModel.size(); i++) {
			allItems.add((T) listModel.getElementAt(i));
		}
		return allItems;
	}

	public void moveItems(GroupingList<T> target, Collection<T> items) {
		target.addItems(items);
		removeItems(items);
	}
}
