package de.soniro.detrin.gui.elements;

import java.util.Collection;

import javax.swing.JOptionPane;

import de.soniro.detrin.gui.DeTrInGui;
import de.soniro.detrin.gui.i18n.Message;
import de.soniro.detrin.gui.i18n.Messages;

/**
 * HelperClass for {@link JOptionPane}.
 *
 * @author Nina Rothenberg
 */
public final class OptionPane extends JOptionPane {

	private static final long serialVersionUID = -2164950975786144714L;

	@SuppressWarnings("unchecked")
	public static <T>T showComboBoxDialogAndGetChoice(Message title, Message message, Collection<T> options, T defaultValue) {
		return (T) JOptionPane.showInputDialog(DeTrInGui.getInstance(),
				Messages.getInstance().getLabel(message), Messages.getInstance().getLabel(title),
				JOptionPane.QUESTION_MESSAGE, null, options.toArray(), defaultValue);
	}

	public static void showErrorMessage(Message message) {
		JOptionPane.showMessageDialog(DeTrInGui.getInstance(), Messages.getInstance().getLabel(message), Messages.getInstance().getLabel(Message.ERROR_MESSAGE_TITLE), JOptionPane.ERROR_MESSAGE);
	}

	public static void showErrorMessage(Message message, Throwable e) {
		JOptionPane.showMessageDialog(DeTrInGui.getInstance(), Messages.getInstance().getLabel(message) + "\nFehler: " + e.getMessage(),
				Messages.getInstance().getLabel(Message.ERROR_MESSAGE_TITLE), JOptionPane.ERROR_MESSAGE);
	}

}
