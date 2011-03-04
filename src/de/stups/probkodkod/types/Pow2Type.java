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
	private final int minint;
	private final int maxint;
	private final int negativeElementPosition;

	public Pow2Type(final String name, final IntegerIntervall interval,
			final int[] powers) {
		super(name, interval);
		this.powers = powers;
		int max = 0, min = 0;
		int negpos = -1;
		boolean negativeIncluded = false;
		int current = 0;
		for (final int p : powers) {
			if (p > 0) {
				max += p;
			}
			if (p < 0) {
				if (negativeIncluded)
					throw new IllegalArgumentException(
							"Only one negative atom allowed");
				negativeIncluded = true;
				negpos = current;
				min += p;
			}
			current++;
		}
		maxint = max;
		minint = min;
		negativeElementPosition = negpos;
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
		if (element < minint || element > maxint)
			throw new IllegalArgumentException("integer out of bounds ("
					+ element + " /: [" + 0 + "," + maxint + "]");
		final int[] result;
		if (element == 0) {
			result = EMPTY_INT_ARRAY;
		} else {
			final boolean wasNegative;
			final int startPos;
			if (element < 0) {
				element = element - minint;
				wasNegative = true;
				startPos = 1;
			} else {
				wasNegative = false;
				startPos = 0;
			}
			final int length = IntTools.countOneBits(element)
					+ (wasNegative ? 1 : 0);
			result = new int[length];
			if (wasNegative) {
				result[0] = negativeElementPosition + interval.getLower();
			}
			encodePositive(element, startPos, result, length);
		}
		return result;
	}

	private void encodePositive(int element, final int startpos,
			final int[] result, final int length) {
		final int offset = interval.getLower();
		int pos = startpos;
		for (int i = 0; element != 0; i++) {
			final int pow2 = powers[i];
			if (pow2 >= 0 && (element & pow2) != 0) {
				result[pos] = i + offset;
				element -= pow2;
				pos++;
			}
		}
		assert pos == length;
	}
}