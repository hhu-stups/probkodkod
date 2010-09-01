/**
 * 
 */
package de.stups.probkodkod.types;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.prolog.IPrologTermOutput;

/**
 * @author plagge
 */
public class TupleType {
	private final Type[] types;
	private final Type[] setTypes;
	private final int arity;
	private final boolean isSingleton;
	private final boolean mustBeSingleton;
	private final boolean isTypeRelation;

	private final List<Integer> lower, upper;

	public static TupleType createTypeRelation(final Type type) {
		final boolean needsSingleton = !(type instanceof SetEnabledType);
		final Type[] types = new Type[] { type };
		return new TupleType(types, needsSingleton, true);
	}

	public TupleType(final Type[] types, final boolean isSingleton) {
		this(types, isSingleton, false);
	}

	private TupleType(final Type[] types, final boolean isSingleton,
			final boolean isTypeRelation) {
		this.types = types;
		this.arity = types.length;
		this.isSingleton = isSingleton;
		this.isTypeRelation = isTypeRelation;
		boolean oneTypeNeedsSingletons = false;
		for (final Type type : types) {
			if (!(type instanceof SetEnabledType)) {
				oneTypeNeedsSingletons = true;
			}
		}
		this.mustBeSingleton = oneTypeNeedsSingletons;
		if (mustBeSingleton && !isSingleton)
			throw new IllegalArgumentException("Must be singleton but is not");
		if (oneTypeNeedsSingletons) {
			setTypes = null;
		} else {
			setTypes = new SetEnabledType[arity];
			for (int i = 0; i < arity; i++) {
				setTypes[i] = types[i];
			}
		}

		final List<Integer> lower = new ArrayList<Integer>(arity);
		final List<Integer> upper = new ArrayList<Integer>(arity);
		int i = 0;
		for (final Type type : types) {
			final IntegerIntervall atomRange = type.getInterval();
			lower.add(atomRange.getLower());
			upper.add(atomRange.getUpper());
			i++;
		}
		this.lower = Collections.unmodifiableList(lower);
		this.upper = Collections.unmodifiableList(upper);
	}

	public int getArity() {
		return arity;
	}

	public boolean isSingleton() {
		return isSingleton;
	}

	public boolean mustBeSingleton() {
		return mustBeSingleton;
	}

	public void writeResult(final IPrologTermOutput pto, final Tuple tuple,
			final TupleSet tupleSet) {
		pto.openList();
		for (int i = 0; i < arity; i++) {
			types[i].writeResult(pto, i, tuple, tupleSet);
		}
		pto.closeList();
	}

	/**
	 * Create a {@link TupleSet} by specifying a range for each position of the
	 * tuple. See the documentation of this class.
	 * 
	 * Both parameters should have the same length, the arity of the relation.
	 * 
	 * @return a {@link TupleSet}, never <code>null</code>
	 */
	public TupleSet createTupleSet(final Universe universe) {
		final TupleFactory factory = universe.factory();
		final Tuple lowerTuple = factory.tuple(lower);
		final Tuple upperTuple = factory.tuple(upper);
		return factory.range(lowerTuple, upperTuple);
	}

	/**
	 * Create a {@link TupleSet} by specifying a collection of tuples
	 * 
	 * @param numTuples
	 *            a collection of type-specific tuples, never <code>null</code>.
	 *            Each tuple must not be <code>null</code> and is encoded as an
	 *            array. The length of the array must be the arity of the type
	 * @return a {@link TupleSet}, never <code>null</code>
	 */
	public TupleSet createTupleSet(final Universe universe,
			final Collection<int[]> numTuples) {
		if (isSingleton && numTuples.size() != 1)
			throw new IllegalArgumentException(
					"Expected singleton set for singleton");
		final Collection<Tuple> result;
		final TupleFactory factory = universe.factory();
		if (mustBeSingleton) {
			final int[] numbers = numTuples.iterator().next();
			result = createSingleton(factory, numbers);
		} else {
			result = createSet(factory, numTuples);
		}
		return factory.setOf(result);
	}

	private Collection<Tuple> createSet(final TupleFactory factory,
			final Collection<int[]> numTuples) {
		Collection<Tuple> tuples = new ArrayList<Tuple>(numTuples.size());
		for (int[] numbers : numTuples) {
			tuples.add(createSetElement(factory, numbers));
		}
		return tuples;
	}

	private Tuple createSetElement(final TupleFactory factory,
			final int[] numbers) {
		checkArity(numbers);
		final Object[] atoms = new Object[arity];
		for (int i = 0; i < arity; i++) {
			final int element = numbers[i];
			final SetEnabledType type = (SetEnabledType) types[i];
			atoms[i] = type.encodeElement(element);
		}
		return factory.tuple(atoms);
	}

	private Collection<Tuple> createSingleton(final TupleFactory factory,
			final int[] numbers) {
		checkArity(numbers);
		int[][] atomIndeces = createAllAtomIndices(numbers);
		Collection<Tuple> tuples = new ArrayList<Tuple>();
		final Object[] atoms = new Object[arity];
		createTuples(factory, 0, atomIndeces, atoms, tuples);
		return tuples;
	}

	private void createTuples(final TupleFactory factory, final int pos,
			final int[][] atomIndeces, final Object[] atoms,
			final Collection<Tuple> tuples) {
		if (pos < arity) {
			final int[] current = atomIndeces[pos];
			for (final int index : current) {
				atoms[pos] = index;
				createTuples(factory, pos + 1, atomIndeces, atoms, tuples);
			}
		} else {
			tuples.add(factory.tuple(atoms));
		}
	}

	private int[][] createAllAtomIndices(final int[] numbers) {
		int[][] all = new int[arity][];
		for (int i = 0; i < arity; i++) {
			final int element = numbers[i];
			all[i] = types[i].encode(element);
		}
		return all;
	}

	private void checkArity(final int[] numbers) {
		if (numbers.length != arity)
			throw new IllegalArgumentException("tuple has " + numbers.length
					+ " elements, but the relation's arity is " + arity);
	}

}
