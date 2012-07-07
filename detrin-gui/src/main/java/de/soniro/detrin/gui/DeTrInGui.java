package de.soniro.detrin.gui;

import java.awt.Toolkit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.swing.JFrame;
import javax.swing.JSplitPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.soniro.detrin.core.DecisionTreeInducer;
import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;
import de.soniro.detrin.gui.menu.DeTrInMenuBar;
import de.soniro.detrin.gui.panel.DataPreparationPanel;
import de.soniro.detrin.gui.panel.DecisionTreePanel;
import de.soniro.detrin.gui.panel.ExplanationTabPanel;
import de.soniro.detrin.gui.panel.PropertiesPanel;
import de.soniro.detrin.gui.visualisation.DecisionTreeToTree;
import de.soniro.detrin.model.Attribute;
import de.soniro.detrin.model.Dataset;
import de.soniro.detrin.model.DecisionTree;

/**
 * The MainPanel of DeTrIn.
 *
 * @author Nina Rothenberg
 */
public final class DeTrInGui extends JFrame {

	private static final long serialVersionUID = 8066978368739318601L;

	private static final Log LOGGER = LogFactory.getLog(DeTrInGui.class);

	public static final DecisionTreeInducer DETRIN = new DecisionTreeInducer();

	private final List<DecisionTree> tree = new ArrayList<DecisionTree>();
	private int currentTree = 0;
	private Dataset dataset;

	private DeTrInMenuBar menu;

	private JSplitPane mainSplit;
	private JSplitPane attributesAndProperties;
	private JSplitPane decisionTreeAndExplanation;

	private static DeTrInGui instance;

	public static DeTrInGui getInstance() {
		if (instance == null) {
			instance = new DeTrInGui();
		}
		return instance;
	}

	private DeTrInGui() {
		super();
		updateTitle();
		createGui();
	}

	private void createGui() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setIconImage(Toolkit.getDefaultToolkit().getImage(Main.class.getResource("/images/icon.png")));

		attributesAndProperties = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, DataPreparationPanel.getInstance(), PropertiesPanel.getInstance());
		attributesAndProperties.setOneTouchExpandable(true);
		decisionTreeAndExplanation = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, DecisionTreePanel.getInstance(), ExplanationTabPanel.getInstance());
		decisionTreeAndExplanation.setOneTouchExpandable(true);
		mainSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, attributesAndProperties, decisionTreeAndExplanation);
		mainSplit.setOneTouchExpandable(true);
		mainSplit.setPreferredSize(Toolkit.getDefaultToolkit().getScreenSize());

		add(mainSplit);
		menu = new DeTrInMenuBar();
		setMenuBar(menu);
		pack();
		setVisible(true);
	}

	public void switchToLocale(Locale locale) {
		Messages.getInstance().setLocale(locale);
		updateTitle();
		menu.updateLabels();
		DataPreparationPanel.getInstance().updateLabels();
		PropertiesPanel.getInstance().updateLabels();
	}

	private void updateTitle() {
		setTitle(Messages.getInstance().getLabel(Message.TITLE));
	}

	public void updateDataset(Dataset dataset) {
		this.dataset = dataset;
		DataPreparationPanel.getInstance().updatePanel(dataset);
		PropertiesPanel.getInstance().updateTargetAttribute(dataset);
	}

	public void updateTree(DecisionTree decisionTree) {
		tree.add(decisionTree);
		currentTree = tree.indexOf(decisionTree);
		DecisionTreePanel.getInstance().updateTree(new DecisionTreeToTree()
				.mapDecisionTreeToDelegateTree(decisionTree));
	}

	public Dataset getDataset() {
		return dataset;
	}

	public DecisionTree getCurrentTree() {
		return tree.get(currentTree);
	}

	public void updateActivateAttribute(boolean activate, int attributeIndex) {
		Attribute<?> attribute = dataset.getAttributes().get(attributeIndex);
		LOGGER.info(Messages.getInstance().getLabel(Message.ATTRIBUTE_IS_ACTIVE, attribute, activate));
		attribute.setActive(activate);
	}

}
