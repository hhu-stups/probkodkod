/**
 * 
 */
package de.stups.probkodkod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.stups.probkodkod.types.Type;

import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

/**
 * The TypedTupleFactory is a typed variant of Kodkod's TupleFactory. For each
 * call to create a tuple of tupleSet, the intervals of the underlying types
 * have to be given.
 * 
 * The factory then maps the type-specific inputs to atoms in the universe. E.g.
 * a specified problem has two types A and B with 3 resp. 5 atoms. The whole
 * universe therefor consists of 8 atoms:
 * 
 * A0 A1 A2 B0 B1 B2 B3 B4
 * 
 * The TypedTupleFactory maps now request like "create a typed tuple (A0,B2)" to
 * a tuple of atoms (0,5).
 * 
 * @see TupleFactory
 * @see Universe
 * @author plagge
 */
public class TypedTupleFactory {
	private final Universe universe;

	public TypedTupleFactory(final Universe universe) {
		this.universe = universe;
	}

	/**
	 * 
	 * @return the underlying Kodkod universe, never <code>null</code>.
	 */
	public Universe getUniverse() {
		return universe;
	}

	/**
	 * Create a {@link TupleSet} by specifying a range for each position of the
	 * tuple. See the documentation of this class.
	 * 
	 * Both parameters should have the same length, the arity of the relation.
	 * 
	 * @param typeIntervals
	 *            the intervals of the relation's type, never <code>null</code>.
	 *            typeIntervals must not contain <code>null</code> elements.
	 * 
	 * @return a {@link TupleSet}, never <code>null</code>
	 */
	public TupleSet createTupleSet(final Type[] types) {
		// the ranges store the information of the intervals in the form
		// [ [aL..aU, bL..bU, cL..cU] = ranges
		// (an example for ranges for a 3-ary type)
		// whereas Kodkod's TupleFactory needs it in the form
		// [aL,bL,cL]..[aU,bU,cU] = lower..upper
		final int numberOfRanges = types.length;
		final List<Integer> lower = new ArrayList<Integer>(numberOfRanges);
		final List<Integer> upper = new ArrayList<Integer>(numberOfRanges);
		int i = 0;
		for (final Type type : types) {
			final IntegerIntervall atomRange = type.getInterval();
			lower.add(atomRange.getLower());
			upper.add(atomRange.getUpper());
			i++;
		}
		final TupleFactory factory = universe.factory();
		final Tuple lowerTuple = factory.tuple(lower);
		final Tuple upperTuple = factory.tuple(upper);
		return factory.range(lowerTuple, upperTuple);

	}

	/**
	 * Create a {@link TupleSet} by specifying a collection of tuples
	 * 
	 * @param typeIntervals
	 *            the intervals of the relation's type, never <code>null</code>.
	 *            typeIntervals must not contain <code>null</code> elements. The
	 *            number of typeIntervals is the arity of the underlying type.
	 * @param numTuples
	 *            a collection of type-specific tuples, never <code>null</code>.
	 *            Each tuple must not be <code>null</code> and is encoded as an
	 *            array. The length of the array must be the arity of the type
	 * @return a {@link TupleSet}, never <code>null</code>
	 */
	public TupleSet createTupleSet(final Type[] types,
			final Collection<int[]> numTuples) {
		Collection<Tuple> tuples = new ArrayList<Tuple>(numTuples.size());
		for (int[] numbers : numTuples) {
			tuples.add(createTuple(types, numbers));
		}
		return universe.factory().setOf(tuples);
	}

	private Tuple createTuple(final Type[] types, final int[] numbers) {
		final int arity = types.length;
		if (numbers.length != arity)
			throw new IllegalArgumentException("tuple has " + numbers.length
					+ " elements, but the relation's arity is " + arity);
		List<Integer> atoms = new ArrayList<Integer>(arity);
		for (int i = 0; i < arity; i++) {
			final int element = numbers[i];
			final IntegerIntervall interval = types[i].getInterval();
			if (element < 0 || element >= interval.getSize())
				throw new IllegalArgumentException("element out of bounds");
			atoms.add(element + interval.getLower());
		}
		return universe.factory().tuple(atoms);
	}

}
