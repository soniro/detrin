package de.soniro.detrin.gui.panel;

import javax.swing.JComponent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

public class ExplanationDesktopPanel extends ExplanationPanel {

	private static final long serialVersionUID = -4248287273955667406L;

	final JDesktopPane desktopPane = new JDesktopPane();
	
	public ExplanationDesktopPanel() {
		super();
		setViewportView(desktopPane);
	}
	
	public void addComponent(String title, JComponent componente) {
		JInternalFrame iFrame = new JInternalFrame(title, true, true, true, true);
        iFrame.setSize(100,100);
        iFrame.setVisible(true);
        iFrame.add(componente);
		desktopPane.add(iFrame);
	}

	@Override
	public void removeComponent(JComponent componente) {
		desktopPane.remove(componente);
	}
	
}
