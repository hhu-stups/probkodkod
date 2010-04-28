/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.prolog.IPrologTermOutput;

/**
 * This is the standard type where the type just consists of a couple of atoms.
 * 
 * @author plagge
 */
public class AtomsType extends Type {
	public AtomsType(final String name, final IntegerIntervall interval) {
		super(name, interval);
	}

	@Override
	public void writeResult(final IPrologTermOutput pto, final int index,
			final Tuple tuple, final TupleSet tupleSet) {
		final int atomIndex = tuple.atomIndex(index);
		pto.printNumber(atomIndex - interval.getLower());
	}

	@Override
	public boolean oneValueNeedsCompleteTupleSet() {
		return false;
	}
}