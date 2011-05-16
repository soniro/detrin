package de.soniro.detrin.standard;

import java.text.MessageFormat;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

public class Messages {
	private static final String BUNDLE_NAME = "de.soniro.detrin.standard.messages";

	private static final ResourceBundle RESOURCE_BUNDLE = ResourceBundle
			.getBundle(BUNDLE_NAME);

	private Messages() {
	}

	public static String getString(String key) {
		try {
			return RESOURCE_BUNDLE.getString(key);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}

	public static String getString(String key, Object... objects) {
		try {
			return new MessageFormat(RESOURCE_BUNDLE.getString(key)).format(objects);
		} catch (MissingResourceException e) {
			return '!' + key + '!';
		}
	}
}
