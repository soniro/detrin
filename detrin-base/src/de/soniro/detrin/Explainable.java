package de.soniro.detrin;

import java.util.Locale;

/**
 * The interface for Objects with Explanations.
 * 
 * @author Nina Rothenberg
 *
 */
public interface Explainable {

	/**
	 * Returns the explanation in the chosen language.
	 * 
	 * @param locale - The language the explanation should be.
	 * @return The explanation in the selected language.
	 */
	String getExplanation(Locale locale);
	
}
