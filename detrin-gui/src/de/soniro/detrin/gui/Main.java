package de.soniro.detrin.gui;

import de.soniro.detrin.gui.exceptions.UncaughtExceptionHandler;

/**
 * The main class of the application.
 * 
 * @author Nina Rothenberg
 */
public final class Main {

	public static void main(String[] args) {
		/*
		 * Standard ExceptionHandler for all unhandled Exceptions in the application.
		 */
		System.setProperty("sun.awt.exception.handler", UncaughtExceptionHandler.class.getName());
		javax.swing.SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				DeTrInGui.getInstance();
			}
		});
	}

}