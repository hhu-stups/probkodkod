package de.stups.probkodkod.types;

import de.stups.probkodkod.IntegerIntervall;

public abstract class SetEnabledType extends Type {

	public SetEnabledType(final String name, final IntegerIntervall interval) {
		super(name, interval);
	}

	@Override
	public final boolean oneValueNeedsCompleteTupleSet() {
		return false;
	}

	public abstract int encodeElement(int element);

	@Override
	public final int[] encode(final int element) {
		return new int[] { encodeElement(element) };
	}

}