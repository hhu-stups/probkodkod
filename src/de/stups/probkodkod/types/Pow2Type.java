/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.prolog.IPrologTermOutput;

/**
 * This type represents those integers that are represented by a set of powers
 * of 2.
 * 
 * @author plagge
 */
public class Pow2Type extends Type {
	private final int[] powers;

	public Pow2Type(final String name, final IntegerIntervall interval,
			final int[] powers) {
		super(name, interval);
		this.powers = powers;
	}

	@Override
	public void writeResult(final IPrologTermOutput pto, final int index,
			final Tuple firsttuple, final TupleSet tupleSet) {
		// To print a value, we need the complete solution
		int result = 0;
		final int offset = interval.getLower();
		for (final Tuple tuple : tupleSet) {
			final int atom = tuple.atomIndex(index);
			result += powers[atom - offset];
		}
		pto.printNumber(result);
	}
}