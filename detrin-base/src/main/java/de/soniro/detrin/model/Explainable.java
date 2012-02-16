package de.soniro.detrin.model;

import java.util.Locale;

/**
 * This Class implements the setter and getter of the explanation so you won't
 * need to implement it over and over again.
 * 
 * @author Nina Rothenberg
 *
 */
public abstract class Explainable implements de.soniro.detrin.Explainable {

	private String explanation;
	
	@Override
	public String getExplanation(Locale locale) {
		return explanation;
	}
	
	public void setExplanation(String explanation) {
		this.explanation = explanation;
	}
	
}
