package de.soniro.detrin;

import java.util.Locale;

/**
 * Interface for an extension.
 * 
 * @author Nina Rothenberg
 */
public interface Extensionable {

	/**
	 * Returns the label of the extension.
	 * 
	 * @param locale - The language for the label.
	 * @return {@link String}
	 */
	String getLabel(Locale locale);
		
}
