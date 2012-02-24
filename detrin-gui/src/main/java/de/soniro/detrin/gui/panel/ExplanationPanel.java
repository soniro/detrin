package de.soniro.detrin.gui.panel;

import javax.swing.JComponent;
import javax.swing.JScrollPane;

public abstract class ExplanationPanel extends JScrollPane {

	private static final long serialVersionUID = 9048723678858198673L;

	public abstract void addComponent(String title, JComponent componente);

	public abstract void removeComponent(JComponent componente);
	
}
