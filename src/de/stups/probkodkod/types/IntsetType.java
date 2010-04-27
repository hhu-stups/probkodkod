/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.prolog.IPrologTermOutput;

/**
 * The type represents integers that can be used in sets.
 * 
 * @author plagge
 */
public class IntsetType extends Type {
	private final int[] values;

	public IntsetType(final String name, final IntegerIntervall interval,
			final int[] values) {
		super(name, interval);
		this.values = values;
	}

	@Override
	public void writeResult(final IPrologTermOutput pto, final int index,
			final Tuple tuple, final TupleSet tupleSet) {
		final int atomIndex = tuple.atomIndex(index);
		final int indexInType = atomIndex - interval.getLower();
		pto.printNumber(values[indexInType]);
	}
}