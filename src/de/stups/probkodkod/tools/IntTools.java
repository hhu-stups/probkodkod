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
	 * @param power
	 * @return exp(2,power)
	 */
	public static long pow2(final int power) {
		return power == 0 ? 0 : 1 << (power - 1);
	}

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
