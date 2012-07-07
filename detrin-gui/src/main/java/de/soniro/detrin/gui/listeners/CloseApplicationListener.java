package de.soniro.detrin.gui.listeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import de.soniro.detrin.gui.DeTrInGui;

/**
 * Closes the application.
 * 
 * @author Nina Rothenberg
 */
public class CloseApplicationListener implements ActionListener {

	public void actionPerformed(ActionEvent e) {
		DeTrInGui.getInstance().setVisible(false);
		DeTrInGui.getInstance().dispose();
		System.exit(0);
	}

}
