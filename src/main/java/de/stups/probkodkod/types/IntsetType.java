/**
 * 
 */
package de.stups.probkodkod.types;

import java.util.HashMap;
import java.util.Map;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;

/**
 * The type represents integers that can be used in sets.
 * 
 * @author plagge
 */
public class IntsetType extends SetEnabledType {
	private final int[] values;
	private final Map<Integer, Integer> intToAtom = new HashMap<Integer, Integer>();

	public IntsetType(final String name, final IntegerIntervall interval,
			final int[] values) {
		super(name, interval);
		this.values = values;
		for (int i = 0; i < values.length; i++) {
			intToAtom.put(values[i], i);
		}
	}

	@Override
	public int decode(final int index, final Tuple tuple,
			final TupleSet tupleSet) {
		final int atomIndex = tuple.atomIndex(index);
		final int indexInType = atomIndex - interval.getLower();
		return values[indexInType];
	}

	@Override
	public int encodeElement(final int element) {
		Integer atom = intToAtom.get(element);
		if (atom == null)
			throw new IllegalArgumentException("integer out of bounds");
		return interval.getLower() + atom;

	}
}