package de.soniro.detrin.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;

import de.soniro.detrin.Algorithm;
import de.soniro.detrin.Extensionable;
import de.soniro.detrin.PruningMethod;
import de.soniro.detrin.SplitCriterion;
import de.soniro.detrin.StopCriterion;
import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.elements.SortedComboBox;
import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.listeners.GenerateDecisionTreeListener;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.Properties;

public final class PropertiesPanel extends JPanel {

	private static final long serialVersionUID = -5130940127856740498L;

	public JPanel algorithmPanel = new JPanel();
	public JComboBox targetAttributeComboBox = new JComboBox();
	public JComboBox splitCriterionComboBox;
	public JComboBox stopCriterionComboBox;
	public JComboBox pruningMethodComboBox;
	public JTextField minimumInstanceCount;

	public JLabel algorithmLabel = new JLabel(Messages.getInstance().getLabel(Message.ALGORITHM));
	public JLabel targetAttributeLabel = new JLabel(Messages.getInstance().getLabel(Message.TARGET_ATTRIBUTE));
	public JLabel splitCriterionLabel = new JLabel(Messages.getInstance().getLabel(Message.SPLIT_CRITERION));
	public JLabel stopCriterionLabel = new JLabel(Messages.getInstance().getLabel(Message.STOP_CRITERION));
	public JLabel pruningMethodLabel = new JLabel(Messages.getInstance().getLabel(Message.PRUNING_METHOD));
	public JLabel minimumInstanceCountLabel = new JLabel(Messages.getInstance().getLabel(Message.MINIMAL_INSTANCE_COUNT));

	public JButton button = new JButton(Messages.getInstance().getLabel(Message.GENERATE_DECISION_TREE));

	private final Map<String, Algorithm> algorithmMap = new HashMap<String, Algorithm>();
	private final Map<String, SplitCriterion> splitCriterionMap = new HashMap<String, SplitCriterion>();
	private final Map<String, StopCriterion> stopCriterionMap = new HashMap<String, StopCriterion>();
	private final Map<String, PruningMethod> pruningMethodMap = new HashMap<String, PruningMethod>();

	private Properties propertiesOfCurrentTree;

	private static PropertiesPanel instance;

	public static PropertiesPanel getInstance() {
		if (instance == null) {
			instance = new PropertiesPanel();
		}
		return instance;
	}

	private PropertiesPanel() {
		super(new GridBagLayout());
		createPropertiesPanel();
	}

	private void createPropertiesPanel() {
		createMap(algorithmMap, Algorithm.class);
		createMap(splitCriterionMap, SplitCriterion.class);
		createMap(stopCriterionMap, StopCriterion.class);
		createMap(pruningMethodMap, PruningMethod.class);

		ButtonGroup radioGroup = new ButtonGroup();
		for (Entry<String, Algorithm> algorithm : algorithmMap.entrySet()) {
			final JRadioButton radioButton = new JRadioButton(algorithm.getKey());
			final Algorithm currentAlgorithm = algorithm.getValue();
			algorithmPanel.add(radioButton);
			radioGroup.add(radioButton);
			radioButton.addActionListener(new ActionListener() {

				public void actionPerformed(ActionEvent e) {
					setPropertiesForAlgorithm(currentAlgorithm);
				}
			});
		}
		pruningMethodMap.put("<Kein Pruning>", null);

		splitCriterionComboBox = new SortedComboBox(splitCriterionMap.keySet().toArray());
		stopCriterionComboBox = new SortedComboBox(stopCriterionMap.keySet().toArray());
		pruningMethodComboBox = new SortedComboBox(pruningMethodMap.keySet().toArray());
		minimumInstanceCount = new JTextField("3", 5);
		minimumInstanceCount.setHorizontalAlignment(JTextField.RIGHT);
		button.addActionListener(new GenerateDecisionTreeListener());
		add(algorithmLabel, createGridBagConstraint(0, 0));
		add(algorithmPanel, createGridBagConstraint(1, 0));
		add(targetAttributeLabel, createGridBagConstraint(0, 1));
		add(targetAttributeComboBox, createGridBagConstraint(1, 1));
		add(splitCriterionLabel, createGridBagConstraint(0, 2));
		add(splitCriterionComboBox, createGridBagConstraint(1, 2));
		add(stopCriterionLabel, createGridBagConstraint(0, 3));
		add(stopCriterionComboBox, createGridBagConstraint(1, 3));
		add(pruningMethodLabel, createGridBagConstraint(0, 4));
		add(pruningMethodComboBox, createGridBagConstraint(1, 4));
		add(minimumInstanceCountLabel, createGridBagConstraint(0, 5));
		add(minimumInstanceCount, createGridBagConstraint(1, 5));
		add(button, createGridBagConstraint(1, 9, GridBagConstraints.EAST));
	}

	private void setPropertiesForAlgorithm(Algorithm algorithm) {
		selectItemForClass(splitCriterionComboBox, splitCriterionMap, algorithm.getPrimarySplitCriterion());
		selectItemForClass(stopCriterionComboBox, stopCriterionMap, algorithm.getPrimaryStopCriterion());
		selectItemForClass(pruningMethodComboBox, pruningMethodMap, algorithm.getPrimaryPruningMethod());
	}

	private void selectItemForClass(JComboBox comboBox, Map<String, ?> objectMap, Class<?> classForSelection) {
		if (classForSelection == null) {
			comboBox.setSelectedIndex(0);
			return;
		}
		for (Entry<String, ?> entry : objectMap.entrySet()) {
			if (entry.getValue() != null && classForSelection.equals(entry.getValue().getClass())) {
				comboBox.setSelectedItem(entry.getKey());
				return;
			}
		}
		throw new RuntimeException(String.format("In der Auswahlliste %s wurde keine Auswahlmöglichkeit mit dem Typ '%s' gefunden.", comboBox.getName(), classForSelection.getName()));
	}

	public GridBagConstraints createGridBagConstraint(int gridx,
			int gridy) {
		return createGridBagConstraint(gridx, gridy, GridBagConstraints.WEST);
	}

	public GridBagConstraints createGridBagConstraint(int gridx,
			int gridy, int anchor) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.anchor = anchor;
		return gbc;
	}

	public <T extends Extensionable> void createMap(Map<String, T> map, Class<T> instanceClass) {
		List<T> instances = DeTrInGui.DETRIN.getAllPossibleInstances(instanceClass);
		for (T instance : instances) {
			map.put(instance.getLabel(Messages.getInstance().getLocale()), instance);
		}
	}

	public void updateTargetAttribute(Dataset dataset) {
		targetAttributeComboBox.removeAllItems();
		for (Attribute<?> attribute : dataset.getAttributes()) {
			targetAttributeComboBox.addItem(attribute);
		}
	}

	public void updateLabels() {
		algorithmLabel.setText(Messages.getInstance().getLabel(Message.ALGORITHM));
		targetAttributeLabel.setText(Messages.getInstance().getLabel(
				Message.TARGET_ATTRIBUTE));
		splitCriterionLabel.setText(Messages.getInstance().getLabel(
				Message.SPLIT_CRITERION));
		stopCriterionLabel.setText(Messages.getInstance().getLabel(
				Message.STOP_CRITERION));
		pruningMethodLabel.setText(Messages.getInstance().getLabel(
				Message.PRUNING_METHOD));
		minimumInstanceCountLabel.setText(Messages.getInstance().getLabel(
				Message.MINIMAL_INSTANCE_COUNT));

		button.setText(Messages.getInstance().getLabel(
				Message.GENERATE_DECISION_TREE));
	}

	public Attribute<?> getTargetAttribute() {
		return (Attribute<?>) targetAttributeComboBox.getSelectedItem();
	}

	public SplitCriterion getSplitCriterion() {
		return splitCriterionMap.get(splitCriterionComboBox.getSelectedItem());
	}

	public StopCriterion getStopCriterion() {
		return stopCriterionMap.get(stopCriterionComboBox.getSelectedItem());
	}

	public PruningMethod getPruningMethod() {
		return pruningMethodMap.get(pruningMethodComboBox.getSelectedItem());
	}

	public Long getMinimalInstanceCount() {
		try {
			return Long.parseLong(minimumInstanceCount.getText());
		} catch (NumberFormatException e) {
			JOptionPane.showMessageDialog(minimumInstanceCount, "Der eingegebene Wert ist kein nummerischer Wert. Verwende Standard-Wert 3.");
			minimumInstanceCount.setText("3");
			return 3L;
		}
	}

	public Properties createProperties() {
		Properties properties = new Properties();
		properties.setDataset(DeTrInGui.getInstance().getDataset());
		properties.setTargetAttribute(PropertiesPanel.getInstance().getTargetAttribute());
		properties.setSplitCriterion(PropertiesPanel.getInstance().getSplitCriterion());
		properties.setStopCriterion(PropertiesPanel.getInstance().getStopCriterion());
		properties.setPruningMethod(PropertiesPanel.getInstance().getPruningMethod());
		properties.setMinimalInstanceCount(PropertiesPanel.getInstance().getMinimalInstanceCount());
		propertiesOfCurrentTree = (Properties) properties.clone();
		return properties;
	}

	public void setPropertiesOfCurrentTree(Properties properties) {
		propertiesOfCurrentTree = properties;
	}

	public Properties getPropertiesOfCurrentTree() {
		return propertiesOfCurrentTree;
	}

}
