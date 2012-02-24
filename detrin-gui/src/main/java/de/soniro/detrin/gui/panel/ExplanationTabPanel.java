package de.soniro.detrin.gui.panel;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.plaf.basic.BasicButtonUI;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.StyleSheet;

import de.soniro.detrin.Explainable;
import de.soniro.detrin.gui.i18n.Messages;

public class ExplanationTabPanel  extends ExplanationPanel {

	private static final long serialVersionUID = -8693116325521134543L;

	final Map<Explainable, JComponent> treeComponentMap = new HashMap<Explainable, JComponent>();
	final DraggableTabbedPane tabPane = new DraggableTabbedPane();
	
	private static ExplanationTabPanel instance;
	
	public static ExplanationTabPanel getInstance() {
		if (instance == null) {
			instance = new ExplanationTabPanel();
		}
		return instance;
	}
	
	private ExplanationTabPanel() {
		super();
		tabPane.setPreferredSize(new Dimension(1,1));
		setViewportView(tabPane);
	}
	
	public void addComponent(String title, JComponent componente) {
		tabPane.addTab(title, componente);
		tabPane.setSelectedComponent(componente);
        tabPane.setTabComponentAt(tabPane.indexOfComponent(componente), createTabComponent(title));
	}
	
	public JComponent createTabComponent(String title) {
		JPanel panel = new JPanel();
        panel.setOpaque(false);
		panel.add(new JLabel(title));
		panel.add(new TabButton(panel));
		return panel;
	}
	
	public void showExplanation(Explainable explainable, String title) {
		String explanation = explainable.getExplanation(Messages.getInstance().getLocale());
		JEditorPane textPane = new JEditorPane();
		textPane.setEditable(false);
		textPane.setContentType("text/html");
		StyleSheet css = ((HTMLEditorKit)textPane.getEditorKit()).getStyleSheet();
		css.addRule("P { margin : 0; font-family : Arial, sans-serif; font-size : 10px; font-style : normal; }");
		if (explanation == null) {
			explanation = "Keine Erklärung vorhanden.";
		}
		textPane.setText("<p>" + explanation.replaceAll(System.getProperty("line.separator"), "<br/>") + "</p>");
		JScrollPane scrollPane = new JScrollPane(textPane);
		
		addComponent(title.replace("&lt;", "<").replace("&gt;", ">"), scrollPane);
		treeComponentMap.put(explainable, scrollPane);
	}
	
	public void hideExplanation(Explainable explainable) {
		JComponent component = treeComponentMap.remove(explainable);
		removeComponent(component);
	}

	@Override
	public void removeComponent(JComponent component) {
		int index = tabPane.indexOfComponent(component);
		if (index != -1) {
			tabPane.removeTabAt(index);
		}
	}
	
    private class TabButton extends JButton implements ActionListener {
    	
		private static final long serialVersionUID = 3787914355556746720L;
		
		final JPanel panel;
    	
        public TabButton(JPanel panel) {
        	super(new ImageIcon(TabButton.class.getResource("/images/close.png")));
        	this.panel = panel;
            int size = 17;
            setPreferredSize(new Dimension(size, size));
            setToolTipText("close this tab");
            //Make the button looks the same for all Laf's
            setUI(new BasicButtonUI());
            //Make it transparent
            setContentAreaFilled(false);
            //No need to be focusable
            setFocusable(false);
            setBorder(BorderFactory.createEtchedBorder());
            setBorderPainted(false);
            setRolloverEnabled(true);
            //Close the proper tab by clicking the button
            addActionListener(this);
        }

        public void actionPerformed(ActionEvent e) {
            int i = ExplanationTabPanel.this.tabPane.indexOfTabComponent(panel);
            if (i != -1) {
            	ExplanationTabPanel.this.tabPane.remove(i);
            }
        }
    }

}
