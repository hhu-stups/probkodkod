/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.tools.IntTools;

/**
 * This type represents those integers that are represented by a set of powers
 * of 2.
 * 
 * @author plagge
 */
public class Pow2Type extends Type {
	private static final int[] EMPTY_INT_ARRAY = new int[0];
	private final int[] powers;
	private final int maxint;

	public Pow2Type(final String name, final IntegerIntervall interval,
			final int[] powers) {
		super(name, interval);
		this.powers = powers;
		int m = 0;
		for (final int p : powers) {
			m += p;
		}
		maxint = m;
	}

	@Override
	public int decode(final int index, final Tuple firsttuple,
			final TupleSet tupleSet) {
		// To print a value, we need the complete solution
		int result = 0;
		final int offset = interval.getLower();
		for (final Tuple tuple : tupleSet) {
			final int atom = tuple.atomIndex(index);
			result += powers[atom - offset];
		}
		return result;
	}

	@Override
	public boolean oneValueNeedsCompleteTupleSet() {
		return true;
	}

	@Override
	public int[] encode(int element) {
		if (element < 0 || element > maxint)
			throw new IllegalArgumentException("integer out of bounds ("
					+ element + " /: [" + 0 + "," + maxint + "]");
		final int[] result;
		if (element == 0) {
			result = EMPTY_INT_ARRAY;
		} else {
			final int length = IntTools.countOneBits(element);
			result = new int[length];
			final int offset = interval.getLower();
			int pos = 0;
			for (int i = 0; element != 0; i++) {
				final int pow2 = powers[i];
				if ((element & pow2) != 0) {
					result[pos] = i + offset;
					element -= pow2;
					pos++;
				}
			}
			assert pos == length;
		}
		return result;
	}
}