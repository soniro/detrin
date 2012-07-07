package de.soniro.detrin.core;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * Messages to return the correct string out of the messages.properties.
 *
 * @author Nina Rothenberg
 *
 */
public final class Messages {

	private static final String BUNDLE_NAME = "de.soniro.detrin.core.messages.explanations";

	public static final String TOO_FEW_INSTANCES = Messages.getString("DecisionTreeInducer.TOO_FEW_INSTANCES");

	public static final String NUMBER_INSTANCES_BRANCH = Messages.getString("DecisionTreeInducer.NUMBER_INSTANCES_BRANCH");

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return ResourceBundle.getBundle(BUNDLE_NAME).getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Object... objects) {
		try {
			return new MessageFormat(ResourceBundle.getBundle(BUNDLE_NAME).getString(key)).format(objects);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
