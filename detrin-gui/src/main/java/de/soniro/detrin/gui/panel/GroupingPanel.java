package de.soniro.detrin.gui.panel;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import de.soniro.detrin.model.Group;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.NominalGroup;

public class GroupingPanel extends JPanel {
	
	private static final long serialVersionUID = 7917101252798123412L;
	
	private static final JButton addGroup = new JButton(new ImageIcon(GroupingPanel.class.getResource("/images/add.png")));
	private static final JButton deleteGroups = new JButton(new ImageIcon(GroupingPanel.class.getResource("/images/close.png")));
	private static final JButton allToGroup = new JButton("<<");
	private static final JButton selectedToGroup = new JButton("<");
	private static final JButton selectedToPool = new JButton(">");
	private static final JButton allToPool = new JButton(">>");
	private static final JButton speichernButton = new JButton("Gruppierung");

	private final GroupingList<Group<String>> groups = new GroupingList<Group<String>>(ListSelectionModel.SINGLE_SELECTION);

	private final GroupingList<String> valuesInGroup = new GroupingList<String>();

	private final GroupingList<String> valuesInPool = new GroupingList<String>();
	
	private final JDialog parent;

	private NominalAttribute attribute;
	
	public GroupingPanel(JDialog parent, NominalAttribute attribute) {
		super();
		this.attribute = attribute;
		this.parent = parent;
		groups.addItems(this.attribute.getGroups());
		groups.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				NominalGroup group = (NominalGroup)groups.getSelectedItem();
				valuesInGroup.clear();
				valuesInGroup.addItems(group.getValues());
			}
		});
		valuesInPool.addItems(getValuesWithoutGroup());	
		setSize(500, 400);
		setLayout(new GridBagLayout());
		initPanel();
	}
	
	private Set<String> getValuesWithoutGroup() {
		Set<String> valuesWithoutGroup = new HashSet<String>(attribute.getPossibleValues());
		for (Group<String> group : attribute.getGroups()) {
			valuesWithoutGroup.removeAll(((NominalGroup) group).getValues());
		}
		return valuesWithoutGroup;
	}
	
	private void initPanel() {
		addGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String groupName = (String)JOptionPane.showInputDialog(
	                    GroupingPanel.this,
	                    "Name der Gruppe:",
	                    "Gruppe hinzufügen",
	                    JOptionPane.QUESTION_MESSAGE);
				NominalGroup group = new NominalGroup(groupName);
				attribute.addGroup(group);
				groups.addItem(group);
				groups.setSelectedValue(group, true);
			}
		});
		deleteGroups.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				Set<Group<String>> selectedGroups = groups.getSelectedItems();
				for (Group<String> group : selectedGroups) {
					valuesInPool.addItems(((NominalGroup) group).getValues());
					attribute.getGroups().remove(group);
					groups.removeItem(group);
				}
			}
		});
		selectedToGroup.setPreferredSize(allToGroup.getPreferredSize());
		selectedToPool.setPreferredSize(allToPool.getPreferredSize());
		allToGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((NominalGroup)groups.getSelectedItem()).getValues().addAll(valuesInPool.getAllItems());
				valuesInPool.moveItems(valuesInGroup, valuesInPool.getAllItems());
			}
		});
		selectedToGroup.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((NominalGroup)groups.getSelectedItem()).getValues().addAll(valuesInPool.getSelectedItems());
				valuesInPool.moveItems(valuesInGroup, valuesInPool.getSelectedItems());
			}
		});
		selectedToPool.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((NominalGroup)groups.getSelectedItem()).getValues().removeAll(valuesInGroup.getSelectedItems());
				valuesInGroup.moveItems(valuesInPool, valuesInGroup.getSelectedItems());
			}
		});
		allToPool.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				((NominalGroup)groups.getSelectedItem()).getValues().removeAll(valuesInGroup.getAllItems());
				valuesInGroup.moveItems(valuesInPool, valuesInGroup.getAllItems());
			}
		});
		JScrollPane groupPanel = new JScrollPane(groups);
		groupPanel.setSize(100, 150);
		JScrollPane valuesInGroupPanel = new JScrollPane(valuesInGroup);
		valuesInGroupPanel.setSize(100, 150);
		JScrollPane valuesInPoolPanel = new JScrollPane(valuesInPool);
		valuesInPoolPanel.setSize(100, 150);
		add(new JLabel("Gruppen"), createGridBagConstraint(0, 0));
		add(groupPanel, createGridBagConstraint(0, 1, 1, 2));
		add(addGroup, createGridBagConstraint(1, 1));
		add(deleteGroups, createGridBagConstraint(1, 2));
		add(new JLabel("Werte der aktuellen Gruppe"), createGridBagConstraint(0, 3));
		add(valuesInGroupPanel, createGridBagConstraint(0, 4, 1, 4));
		add(allToGroup, createGridBagConstraint(1, 4));
		add(selectedToGroup, createGridBagConstraint(1, 5));
		add(selectedToPool, createGridBagConstraint(1, 6));
		add(allToPool, createGridBagConstraint(1, 7));
		add(new JLabel("Übrige Werte"), createGridBagConstraint(2, 3));
		add(valuesInPoolPanel, createGridBagConstraint(2, 4, 1, 4));
		add(speichernButton, createGridBagConstraint(2, 8));
		speichernButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				parent.setVisible(false);
				parent.removeAll();
			}
		});
	}
	
	public JList createList() {
		JList list = new JList(new String[]{"Test1", "Test2", "Test3"});
		list.setFixedCellWidth(100);
		list.setFixedCellHeight(20);
		list.setVisibleRowCount(10);
		return list;
	}
	
	public GridBagConstraints createGridBagConstraint(int gridx,
			int gridy) {
		return createGridBagConstraint(gridx, gridy, 1, 1);
	}
	
	public GridBagConstraints createGridBagConstraint(int gridx,
			int gridy, int gridwidth, int gridheight) {
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.gridx = gridx;
		gbc.gridy = gridy;
		gbc.gridwidth = gridwidth;
		gbc.gridheight = gridheight;
		gbc.anchor = GridBagConstraints.NORTHWEST;
		return gbc;
	}
	
}
