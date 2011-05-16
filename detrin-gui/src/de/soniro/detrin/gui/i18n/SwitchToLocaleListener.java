package de.soniro.detrin.gui.i18n;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Locale;

import de.soniro.detrin.gui.DeTrInGui;

/**
 * Action listener for changing the gui-language.
 * 
 * @author Nina Rothenberg
 */
public class SwitchToLocaleListener implements ActionListener {

	final Locale locale;
	
	public SwitchToLocaleListener(Locale locale) {
		this.locale = locale;
	}
	
	public void actionPerformed(ActionEvent e) {
		DeTrInGui.getInstance().switchToLocale(locale);
	}

}
