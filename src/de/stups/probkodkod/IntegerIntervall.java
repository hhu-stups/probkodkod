/**
 * 
 */
package de.stups.probkodkod;

/**
 * Stores an integer interval by its upper and lower bound.
 * 
 * @author plagge
 */
public class IntegerIntervall {
	private final int lower, upper;

	public IntegerIntervall(final int lower, final int upper) {
		if (lower > upper)
			throw new IllegalArgumentException(
					"lower bound should be <= upper bound");
		this.lower = lower;
		this.upper = upper;
	}

	public int getLower() {
		return lower;
	}

	public int getUpper() {
		return upper;
	}

	public int getSize() {
		return upper - lower + 1;
	}

	@Override
	public String toString() {
		return "[" + lower + ".." + upper + "]";
	}

	public boolean contains(final int value) {
		return lower <= value && value <= upper;
	}

}
