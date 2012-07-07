package de.soniro.detrin.gui.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Singleton class to load the messages.
 *
 * @author Nina Rothenberg
 */
public final class Messages {

	private static final Messages INSTANCE = new Messages();

	private Locale locale = Locale.GERMAN;

	private Messages() { }


	public static Messages getInstance() {
		return INSTANCE;
	}

	public String getLabel(Message message) {
		return ResourceBundle.getBundle("messages", locale).getString(message.getName());
	}

	public String getLabel(Message message, Object... objects) {
		return new MessageFormat(ResourceBundle.getBundle("messages", locale).getString(message.getName())).format(objects);
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public Locale getLocale() {
		return locale;
	}
}
