/**
 * 
 */
package de.stups.probkodkod;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import kodkod.ast.Formula;
import kodkod.ast.Relation;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import de.stups.probkodkod.bounds.AbstractBound;
import de.stups.probkodkod.bounds.ExactBound;
import de.stups.probkodkod.bounds.SubsetBound;
import de.stups.probkodkod.bounds.TypeBound;
import de.stups.probkodkod.tools.IntTools;
import de.stups.probkodkod.types.AtomsType;
import de.stups.probkodkod.types.IntsetType;
import de.stups.probkodkod.types.Pow2Type;
import de.stups.probkodkod.types.TupleType;
import de.stups.probkodkod.types.Type;

/**
 * A Problem defines the components of a Kodkod problem description. Objects of
 * this class are used by {@link KodkodAnalysis} to create a new problem
 * description. As soon the definition has finished, an immutable Version of the
 * problem is created by {@link #createImmutable()} and inserted into the
 * session (see {@link KodkodSession}).
 * 
 * @author plagge
 * 
 * @see ImmutableProblem
 */
public class Problem {
	private int currentSize = 0;
	private final String id;
	private Formula formula;
	private final Map<String, Type> types = new HashMap<String, Type>();
	private final Map<String, RelationInfo> relations = new HashMap<String, RelationInfo>();
	private int numberOffset;
	private int[] numbers;
	private Integer bitwidth;

	private Universe universe = null;
	private final Collection<TypeBound> typeBounds = new ArrayList<TypeBound>();

	public Problem(final String id) {
		this.id = id;
	}

	public void setFormula(final Formula formula) {
		this.formula = formula;
	}

	private void registerRelation(final String id, final TupleType tupleType,
			final AbstractBound bound) {
		if (relations.containsKey(id))
			throw new IllegalStateException("relation '" + id
					+ "' declared twice.");
		final Relation relation = Relation.nary(id, tupleType.getArity());
		final RelationInfo info = new RelationInfo(id, relation, bound,
				tupleType);
		relations.put(id, info);
	}

	/**
	 * @param pow2id
	 * @param intsetId
	 * @param intset
	 * @param maxPow2
	 */
	public void registerIntegerTypes(final String pow2id,
			final String intsetId, final IntegerIntervall intset,
			final IntegerIntervall pow2range) {
		checkIntsetArgs(intset);

		final int bitsForPows = calculateBitsForPows(pow2range);
		final int bitsForAtoms = calculateBitsForAtoms(intset, bitsForPows);
		final int totalBits = bitsForAtoms + bitsForPows;
		final int pow2start = currentSize + bitsForAtoms;

		numbers = createIntegerArray(intset, bitsForPows, bitsForAtoms);
		numberOffset = currentSize;

		registerIntset(intsetId, intset, bitsForAtoms);
		final IntegerIntervall pow2Interval = registerPow2(pow2id, bitsForPows,
				totalBits, pow2start);

		bitwidth = pow2Interval != null ? pow2Interval.getSize() : null;

		currentSize += totalBits;
	}

	private int calculateBitsForPows(final IntegerIntervall pow2range) {
		final int lower = pow2range.getLower();
		final int upper = pow2range.getUpper();
		// for negative integers, we need one bit less
		final int a = lower < 0 ? -lower - 1 : lower;
		final int b = upper < 0 ? -upper - 1 : upper;
		final int maxint = Math.max(a, b);
		return IntTools.bitwidth(maxint) + 1;
	}

	private IntegerIntervall registerPow2(final String pow2id,
			final int bitsForPows, final int totalBits, final int pow2start) {
		final IntegerIntervall interval;
		if (bitsForPows == 0) {
			interval = null;
		} else {
			interval = new IntegerIntervall(pow2start, currentSize + totalBits
					- 1);
			final int[] powers = new int[bitsForPows];
			System.arraycopy(numbers, totalBits - bitsForPows, powers, 0,
					bitsForPows);
			final Type type = new Pow2Type(pow2id, interval, powers);
			registerType(pow2id, type, interval);
		}
		return interval;
	}

	private void registerIntset(final String intsetId,
			final IntegerIntervall intset, final int bitsForAtoms) {
		if (intset != null) {
			final int neededAtoms = intset.getUpper() - intset.getLower() + 1;
			final IntegerIntervall interval = new IntegerIntervall(currentSize,
					currentSize + neededAtoms - 1);
			Type type = new IntsetType(intsetId, interval, numbers);
			registerType(intsetId, type, interval);
		}
	}

	private int calculateBitsForAtoms(final IntegerIntervall intset,
			final int bitsForPows) {
		final int bitsForAtoms;
		if (intset == null) {
			bitsForAtoms = 0;
		} else {
			final int minAtom = intset.getLower();
			final int maxAtom = intset.getUpper();
			final int bitwidth = IntTools.bitwidth(maxAtom);
			final int minPow = IntTools
					.smallestRepresentableInteger(bitsForPows);
			final boolean sharedNeg = intset.contains(minPow);
			bitsForAtoms = (maxAtom - minAtom + 1) - bitwidth
					+ (sharedNeg ? -1 : 0);

			if (bitwidth > bitsForPows)
				throw new IllegalArgumentException("too many atoms (" + maxAtom
						+ " <= 2^" + bitwidth + ") for too few powers of 2 ("
						+ bitsForPows + "< " + bitwidth + ")");
		}
		return bitsForAtoms;
	}

	private int[] createIntegerArray(final IntegerIntervall intset,
			final int bitsForPows, final int bitsForAtoms) {
		final int[] numbers = new int[bitsForAtoms + bitsForPows];
		final int minPow = IntTools.smallestRepresentableInteger(bitsForPows);
		final boolean negPowInAtoms = intset != null && intset.contains(minPow);
		if (intset != null) {
			int pos = 0;
			for (int currentInt = intset.getLower(); currentInt <= intset
					.getUpper(); currentInt++) {
				if (currentInt != minPow
						&& (currentInt < 0 || !IntTools.isPow2(currentInt))) {
					numbers[pos] = currentInt;
					pos++;
				}
			}
		}
		final int start;
		if (negPowInAtoms) {
			start = 1;
			numbers[bitsForAtoms] = minPow;
		} else {
			start = 0;
		}
		int pow = 0;
		for (int i = 0; i < bitsForPows - 1; i++) {
			pow = pow == 0 ? 1 : pow * 2;
			numbers[start + bitsForAtoms + i] = pow;
		}
		if (!negPowInAtoms) {
			numbers[numbers.length - 1] = minPow;
		}
		return numbers;
	}

	private void checkIntsetArgs(final IntegerIntervall atomIntegers) {
		if (atomIntegers != null) {
			final int minAtom = atomIntegers.getLower();
			final int maxAtom = atomIntegers.getUpper();
			if (minAtom > maxAtom)
				throw new IllegalArgumentException("minimum atom (" + minAtom
						+ ") must be smaller or equal than maximum atom ("
						+ maxAtom + ")");
			if (minAtom > 0)
				throw new IllegalArgumentException("minimum atom (" + minAtom
						+ ") must be zero or negative");
			if (maxAtom < 0)
				throw new IllegalArgumentException("maximum atom (" + maxAtom
						+ ") must be zero or positive");
		}
	}

	public Relation lookupRelation(final String id) {
		RelationInfo relation = relations.get(id);
		if (relation == null)
			throw new IllegalArgumentException("Unknown relation " + id);
		return relation.getRelation();
	}

	public ImmutableProblem createImmutable() {
		return new ImmutableProblem(id, formula, bitwidth, universe,
				relations.values(), numberOffset, numbers);
	}

	public void registerType(final String id, final int size) {
		final IntegerIntervall interval = new IntegerIntervall(currentSize,
				currentSize + size - 1);
		currentSize += size;
		registerType(id, new AtomsType(id, interval), interval);
	}

	private void registerType(final String id, final Type type,
			final IntegerIntervall interval) {
		final TypeBound bound = new TypeBound(interval);
		typeBounds.add(bound);
		types.put(id, type);
		final TupleType tupleType = TupleType.createTypeRelation(type);
		registerRelation(id, tupleType, bound);
	}

	public Type lookupType(final String typeId) {
		return types.get(typeId);
	}

	public void addRelation(final String id, final boolean isExact,
			final TupleType tupleType, final TupleSet ptset) {
		AbstractBound bound;
		if (isExact) {
			bound = new ExactBound(ptset);
		} else {
			bound = new SubsetBound(ptset);
		}
		registerRelation(id, tupleType, bound);
	}

	/**
	 * This method should be called after registering all types and before
	 * adding any other relations. It creates an universe consisting of enough
	 * atoms to enclose all types.
	 * 
	 * This method should only be called once.
	 */
	public void createUniverse() {
		if (universe != null)
			throw new IllegalStateException("createUniverse called twice");
		Collection<Integer> atoms = new ArrayList<Integer>(currentSize);
		for (int i = 0; i < currentSize; i++) {
			atoms.add(new Integer(i));
		}
		universe = new Universe(atoms);

		for (final TypeBound typeBound : typeBounds) {
			typeBound.createTupleSets(universe);
		}
	}

	public Universe getUniverse() {
		return universe;
	}

	public CalculatedIntegerAtoms getCalculatedIntegerAtoms() {
		return new CalculatedIntegerAtoms(numberOffset, numbers, bitwidth);
	}

	public static class CalculatedIntegerAtoms {
		private final int numberOffset;
		private final int[] numbers;
		private final Integer bitwidth;

		public CalculatedIntegerAtoms(final int numberOffset,
				final int[] numbers, final Integer bitwidth) {
			this.numberOffset = numberOffset;
			this.numbers = numbers == null ? null : new int[numbers.length];
			if (numbers != null) {
				System.arraycopy(numbers, 0, this.numbers, 0, numbers.length);
			}
			this.bitwidth = bitwidth;
		}

		public int getNumberOffset() {
			return numberOffset;
		}

		public int[] getNumbers() {
			return numbers;
		}

		public Integer getBitwidth() {
			return bitwidth;
		}
	}
}
