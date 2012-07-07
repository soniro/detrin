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

	private static final JButton ADD_GROUP = new JButton(new ImageIcon(GroupingPanel.class.getResource("/images/add.png")));
	private static final JButton DELETE_GROUPS = new JButton(new ImageIcon(GroupingPanel.class.getResource("/images/close.png")));
	private static final JButton ALL_TO_GROUP = new JButton("<<");
	private static final JButton SELECTED_TO_GROUP = new JButton("<");
	private static final JButton SELECTED_TO_POOL = new JButton(">");
	private static final JButton ALL_TO_POOL = new JButton(">>");
	private static final JButton SAVE_BUTTON = new JButton("Gruppierung");

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

			public void valueChanged(ListSelectionEvent e) {
				NominalGroup group = (NominalGroup) groups.getSelectedItem();
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
		ADD_GROUP.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				String groupName = JOptionPane.showInputDialog(
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
		DELETE_GROUPS.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				Set<Group<String>> selectedGroups = groups.getSelectedItems();
				for (Group<String> group : selectedGroups) {
					valuesInPool.addItems(((NominalGroup) group).getValues());
					attribute.getGroups().remove(group);
					groups.removeItem(group);
				}
			}
		});
		SELECTED_TO_GROUP.setPreferredSize(ALL_TO_GROUP.getPreferredSize());
		SELECTED_TO_POOL.setPreferredSize(ALL_TO_POOL.getPreferredSize());
		ALL_TO_GROUP.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((NominalGroup) groups.getSelectedItem()).getValues().addAll(valuesInPool.getAllItems());
				valuesInPool.moveItems(valuesInGroup, valuesInPool.getAllItems());
			}
		});
		SELECTED_TO_GROUP.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((NominalGroup) groups.getSelectedItem()).getValues().addAll(valuesInPool.getSelectedItems());
				valuesInPool.moveItems(valuesInGroup, valuesInPool.getSelectedItems());
			}
		});
		SELECTED_TO_POOL.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((NominalGroup) groups.getSelectedItem()).getValues().removeAll(valuesInGroup.getSelectedItems());
				valuesInGroup.moveItems(valuesInPool, valuesInGroup.getSelectedItems());
			}
		});
		ALL_TO_POOL.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				((NominalGroup) groups.getSelectedItem()).getValues().removeAll(valuesInGroup.getAllItems());
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
		add(ADD_GROUP, createGridBagConstraint(1, 1));
		add(DELETE_GROUPS, createGridBagConstraint(1, 2));
		add(new JLabel("Werte der aktuellen Gruppe"), createGridBagConstraint(0, 3));
		add(valuesInGroupPanel, createGridBagConstraint(0, 4, 1, 4));
		add(ALL_TO_GROUP, createGridBagConstraint(1, 4));
		add(SELECTED_TO_GROUP, createGridBagConstraint(1, 5));
		add(SELECTED_TO_POOL, createGridBagConstraint(1, 6));
		add(ALL_TO_POOL, createGridBagConstraint(1, 7));
		add(new JLabel("&Uuml;brige Werte"), createGridBagConstraint(2, 3));
		add(valuesInPoolPanel, createGridBagConstraint(2, 4, 1, 4));
		add(SAVE_BUTTON, createGridBagConstraint(2, 8));
		SAVE_BUTTON.addActionListener(new ActionListener() {

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
