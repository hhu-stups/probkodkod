/**
 * 
 */
package de.stups.probkodkod.tools;

/**
 * This class contains some operations on integers
 * 
 * @author plagge
 */
public final class IntTools {

	/**
	 * @param maxint
	 * @return the number of bits needed to store maxint
	 */
	public static int bitwidth(int maxint) {
		int bits = 0;
		while (maxint != 0) {
			bits++;
			maxint >>= 1;
		}
		return bits;
	}

	public static int pow2(final int exp) {
		if (exp < 0)
			throw new IllegalArgumentException(
					"pow2 needs non-negative argument");
		int pow = 1;
		for (int i = 0; i < exp; i++) {
			pow <<= 1;
		}
		return pow;
	}

	public static int smallestRepresentableInteger(final int bitwidth) {
		return -pow2(bitwidth - 1);
	}

	/**
	 * @param value
	 * @return <code>true</code> if there is an i such that value == exp(2,i)
	 */
	public static boolean isPow2(int value) {
		if (value == 0)
			return false;
		for (; value != 0; value >>= 1) {
			if ((value & 1) == 1)
				return (value == 1);
		}
		throw new IllegalStateException(
				"End of isPow2 should never be reached.");
	}

	public static int countOneBits(int integer) {
		int pow2 = 1;
		int count = 0;
		while (integer != 0) {
			if ((integer & pow2) != 0) {
				count++;
				integer &= ~pow2;
			}
			pow2 <<= 1;
		}
		return count;
	}
}
