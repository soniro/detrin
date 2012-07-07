package de.soniro.detrin.gui.panel;


public class OptionPaneInput<T> {

	private final String label;

	private final T value;

	public OptionPaneInput(String label, T value) {
		this.label = label;
		this.value = value;
	}

	public T getValue() {
		return value;
	}

	@Override
	public String toString() {
		return label;
	}

}
