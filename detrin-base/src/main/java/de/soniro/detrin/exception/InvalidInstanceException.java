package de.soniro.detrin.exception;

/**
 * Exception for a invalid {@link de.soniro.detrin.model.Instance}.
 * 
 * @author Nina Rothenberg
 */
public class InvalidInstanceException extends Exception {

	private static final long serialVersionUID = 802147315179577567L;

	public InvalidInstanceException(String message) {
		super(message);
	}
	
}
