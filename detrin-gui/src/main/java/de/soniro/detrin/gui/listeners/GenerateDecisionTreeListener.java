package de.soniro.detrin.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;

import de.soniro.detrin.core.DecisionTreeInducer;
import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.exceptions.UncaughtExceptionHandler;
import de.soniro.detrin.gui.panel.DecisionTreePanel;
import de.soniro.detrin.gui.panel.PropertiesPanel;
import de.soniro.detrin.model.DecisionTree;
import de.soniro.detrin.model.NominalAttribute;
import de.soniro.detrin.model.Properties;

/**
 * Listener that generates a {@link DecisionTree} on action.
 * 
 * @author Nina Rothenberg
 */
public class GenerateDecisionTreeListener implements ActionListener {
	
	public void actionPerformed(ActionEvent arg0) {
		DecisionTreePanel.getInstance().showLoading();
		Thread thread = new Thread() {
			@Override
			public void run() {
				Properties properties = PropertiesPanel.getInstance().createProperties();
				if (!(properties.getTargetAttribute() instanceof NominalAttribute)) {
					JOptionPane.showMessageDialog(DeTrInGui.getInstance(), "Das gewählte Zielattribut muss ein nominales Attribut sein.", "Es ist ein Fehler aufgetreten", JOptionPane.ERROR_MESSAGE);
					DecisionTreePanel.getInstance().hideLoading();
				} else {
					DecisionTree decisionTree = new DecisionTreeInducer().induceDecisionTree(properties);
					DeTrInGui.getInstance().updateTree(decisionTree);
				}
			}
		};
		thread.setUncaughtExceptionHandler(new UncaughtExceptionHandler(true));
		thread.start();
	}

}
