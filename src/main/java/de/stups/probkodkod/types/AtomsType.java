/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;

/**
 * This is the standard type where the type just consists of a couple of atoms.
 * 
 * @author plagge
 */
public class AtomsType extends SetEnabledType {
	public AtomsType(final String name, final IntegerIntervall interval) {
		super(name, interval);
	}

	@Override
	public int decode(final int index, final Tuple tuple,
			final TupleSet tupleSet) {
		final int atomIndex = tuple.atomIndex(index);
		return atomIndex - interval.getLower();
	}

	@Override
	public int encodeElement(final int element) {
		if (element < 0 || element >= interval.getSize())
			throw new IllegalArgumentException("element out of bounds");
		return interval.getLower() + element;
	}
}