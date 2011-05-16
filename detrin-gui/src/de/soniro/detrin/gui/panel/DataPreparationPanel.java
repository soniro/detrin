package de.soniro.detrin.gui.panel;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableCellRenderer;

import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.listeners.ImportDatasetListener;
import de.soniro.detrin.gui.tablemodel.AttributeTableModel;
import de.soniro.detrin.gui.tablemodel.InstanceTableModel;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Attribute.GroupingType;

public class DataPreparationPanel extends JTabbedPane {

	private static final long serialVersionUID = 5548752870754486785L;

	JScrollPane instances = new JScrollPane();
	JScrollPane attributes = new JScrollPane();

	JTable attributeTable;

	AttributeTableModel attributeTableModel;

	private static DataPreparationPanel instance;

	public static DataPreparationPanel getInstance() {
		if (instance == null) {
			instance = new DataPreparationPanel();
		}
		return instance;
	}

	private DataPreparationPanel() {
		add(Messages.getInstance().getLabel(Message.INSTANCES), instances);
		add(Messages.getInstance().getLabel(Message.ATTRIBUTES), attributes);
		instances.setViewportView(createDataImport());
		attributes.setViewportView(createDataImport());
	}

	private JPanel createDataImport() {
		JPanel importPanel = new JPanel();
		JButton importData = new JButton(Messages.getInstance().getLabel(
				Message.IMPORT));
		importData.addActionListener(new ImportDatasetListener());
		importPanel.add(importData);
		return importPanel;
	}

	public void updatePanel(Dataset dataset) {
		JTable instancesTable = new JTable(new InstanceTableModel(dataset));
		instances.setViewportView(instancesTable);
		attributeTableModel = new AttributeTableModel();
		Object[][] dataVector = new Object[dataset.getAttributes().size()][5];
		for (int i = 0; i < dataset.getAttributes().size(); i++) {
			Attribute<?> attribute = dataset.getAttributes().get(i);
			dataVector[i][0] = attribute.isActive();
			dataVector[i][1] = attribute.getName();
			dataVector[i][2] = attribute.getPossibleValues();
			dataVector[i][3] = attribute.getGroupingType();
			dataVector[i][4] = attribute;
		}
		attributeTableModel.setDataVector(dataVector, new Object[] { 0, 1, 2,
				"Gruppierung", "Gruppen" });
		attributeTable = new JTable(attributeTableModel) {
			private static final long serialVersionUID = -5113286652167065503L;

			public void tableChanged(TableModelEvent e) {
				super.tableChanged(e);
				repaint();
			}
		};
		this.updateLabels();
		attributeTable.setRowHeight(20);
		attributeTable.getColumnModel().getColumn(0).setMaxWidth(11);
		attributeTable.getColumnModel().getColumn(1).setPreferredWidth(20);
		attributeTable.getColumnModel().getColumn(2).setPreferredWidth(100);
		attributeTable.getColumnModel().getColumn(3).setPreferredWidth(50);
		attributeTable.getColumnModel().getColumn(4).setPreferredWidth(15);
		attributeTable.getColumnModel().getColumn(3).setCellRenderer(
				new AttributeTableRenderer());
		attributeTable.getColumnModel().getColumn(3).setCellEditor(
				new DefaultCellEditor(new JComboBox(GroupingType.values())));
		attributeTable.getColumnModel().getColumn(4).setCellRenderer(
				new AttributeTableRenderer());
		attributeTable.getColumnModel().getColumn(4).setCellEditor(
				new ButtonEditor(new JCheckBox()));
		attributes.setViewportView(attributeTable);
	}

	public void updateLabels() {
		if (attributeTable != null) {
			attributeTable.getColumnModel().getColumn(0).setHeaderValue(
					Messages.getInstance().getLabel(Message.ACTIVATED));
			attributeTable.getColumnModel().getColumn(1).setHeaderValue(
					Messages.getInstance().getLabel(Message.NAME));
			attributeTable.getColumnModel().getColumn(2).setHeaderValue(
					Messages.getInstance().getLabel(Message.VALUES));
		} else {
			updateButtonOnPanel((JPanel) instances.getViewport().getView());
			updateButtonOnPanel((JPanel) attributes.getViewport().getView());
		}
		setTitleAt(0, Messages.getInstance().getLabel(Message.INSTANCES));
		setTitleAt(1, Messages.getInstance().getLabel(Message.ATTRIBUTES));
	}

	private void updateButtonOnPanel(JPanel panel) {
		((JButton) panel.getComponent(0)).setText(Messages.getInstance()
				.getLabel(Message.IMPORT));
	}

	class AttributeTableRenderer implements TableCellRenderer {

		final Map<Integer, JComboBox> comboBoxes = new HashMap<Integer, JComboBox>();

		@Override
		public Component getTableCellRendererComponent(JTable table,
				Object value, boolean isSelected, boolean hasFocus, int row,
				int column) {
			if (value == null)
				return null;
			if (column == 3) {
				JComboBox comboBox = comboBoxes.get(row);
				if (comboBox == null) {
					comboBox = new JComboBox(GroupingType.values());
					comboBoxes.put(row, comboBox);
				}
				comboBox.setSelectedItem(value);
				return comboBox;
			} else if (column == 4) {
				JButton button = new JButton(new ImageIcon(getClass()
						.getResource("/images/edit.png")));
				button.setOpaque(false);
				return button;
			} else {
				return (Component) value;
			}
		}

	}

	class ButtonEditor extends DefaultCellEditor {

		private static final long serialVersionUID = 8810355142436101003L;

		protected JButton button;
		private boolean isPushed;

		private Attribute<?> attribute;

		public ButtonEditor(JCheckBox checkBox) {
			super(checkBox);
			button = new JButton();
			button.setOpaque(true);
			button.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					fireEditingStopped();
				}
			});
		}

		public Component getTableCellEditorComponent(JTable table,
				Object value, boolean isSelected, int row, int column) {
			if (isSelected) {
				button.setForeground(table.getSelectionForeground());
				button.setBackground(table.getSelectionBackground());
			} else {
				button.setForeground(table.getForeground());
				button.setBackground(table.getBackground());
			}
			isPushed = true;
			attribute = (Attribute<?>) value;
			return button;
		}

		public Object getCellEditorValue() {
			if (isPushed) {
				new GroupingPopup(attribute);
			}
			isPushed = false;
			return attribute;
		}

		public boolean stopCellEditing() {
			isPushed = false;
			return super.stopCellEditing();
		}

		protected void fireEditingStopped() {
			super.fireEditingStopped();
		}
	}

}
