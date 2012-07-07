package de.soniro.detrin.standard;


/**
 * Represent a fraction, to make it possible to visualize a fraction
 * readable for the user.
 *
 * @author Nina Rothenberg
 *
 */
public class Fraction {

	public static final Fraction ZERO = new Fraction(0L, 1L);

	private Long zaehler;

	private Long nenner;

	public Fraction(Long zaehler, Long nenner) {
		this.zaehler = zaehler;
		this.nenner = nenner;
	}

	public void add(Long number) {
		this.zaehler += this.nenner;
	}

	public Double doubleValue() {
		return zaehler.doubleValue() / nenner.doubleValue();
	}

	@Override
	public String toString() {
		if (nenner.equals(1L)) {
			return zaehler.toString();
		} else {
			return zaehler + "/" + nenner;
		}
	}

}
