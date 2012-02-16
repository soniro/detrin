package de.soniro.detrin.exception;

import de.soniro.detrin.model.Instance;

/**
 * Exception for a invalid {@link Instance}.
 * 
 * @author Nina Rothenberg
 */
public class InvalidInstanceException extends Exception {

	private static final long serialVersionUID = 802147315179577567L;

	public InvalidInstanceException(String message) {
		super(message);
	}
	
}
