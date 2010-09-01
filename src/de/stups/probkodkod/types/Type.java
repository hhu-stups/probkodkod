/**
 * 
 */
package de.stups.probkodkod.types;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.prolog.IPrologTermOutput;

/**
 * For each registered type, an instance of this class is generated. A type
 * stores the information which atoms represents the members of the type and how
 * a value is printed in the output of solutions.
 * 
 * @author plagge
 */
public abstract class Type {
	private final String name;
	protected final IntegerIntervall interval;

	public Type(final String name, final IntegerIntervall interval) {
		this.name = name;
		this.interval = interval;
	}

	/**
	 * @return the interval of atoms in Kodkod's universe that represent the
	 *         members of this type
	 */
	public IntegerIntervall getInterval() {
		return interval;
	}

	/**
	 * Writes a part of the solution to the PrologOutput
	 * 
	 * @param pto
	 *            the Prolog output printer to write the result into
	 * @param index
	 *            the index of the value to print in the tuple. E.g. if a tuple
	 *            of a binary relation A<->B should be printed, this method is
	 *            called for type A with index 1 and then for type B with index
	 *            2
	 * @param tuple
	 *            one element of the solution that should be printed
	 * @param tupleSet
	 *            the complete solution
	 */
	public abstract void writeResult(IPrologTermOutput pto, int index,
			Tuple tuple, TupleSet tupleSet);

	public abstract int[] encode(int element);

	public abstract boolean oneValueNeedsCompleteTupleSet();

	@Override
	public String toString() {
		return (name == null ? "<unnamed>" : name) + interval.toString();
	}

}
