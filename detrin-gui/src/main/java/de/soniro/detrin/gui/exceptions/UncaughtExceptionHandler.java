package de.soniro.detrin.gui.exceptions;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.panel.DecisionTreePanel;

/**
 * This is the standard exception handler for uncaught exceptions.
 * He is set in the {@link de.soniro.detrin.gui.Main} class.
 *
 * He logs the error message and shows an error dialog to inform the user.
 *
 * @author Nina Rothenberg
 */
public class UncaughtExceptionHandler implements java.lang.Thread.UncaughtExceptionHandler {

	public static final Log LOGGER = LogFactory.getLog(UncaughtExceptionHandler.class);

	private final boolean hideLoading;

	public UncaughtExceptionHandler() {
		this(false);
	}

	public UncaughtExceptionHandler(boolean hideLoading) {
		this.hideLoading = hideLoading;
	}

	public void handle(Throwable e) {
		e.printStackTrace();
		LOGGER.error(e.getMessage(), e);
		JOptionPane.showMessageDialog(DeTrInGui.getInstance(), "Fehler: " + e.getMessage(), "Es ist ein Fehler aufgetreten", JOptionPane.ERROR_MESSAGE);
		if (hideLoading) {
			DecisionTreePanel.getInstance().hideLoading();
		}
	}

	public void uncaughtException(Thread t, Throwable e) {
		handle(e);
	}

}
