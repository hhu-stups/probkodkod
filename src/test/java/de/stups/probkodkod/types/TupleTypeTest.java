package de.stups.probkodkod.types;

import static junit.framework.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Before;
import org.junit.Test;

import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.test.KodkodUtil;

/**
 * 
 * Default universe:
 * 
 * atoms: A B C, integers: 0 1 2 3 4 5
 * 
 * 012345678
 * 
 * ABC035124
 */
public class TupleTypeTest {
	private Type atoms, intset, powset;

	@Before
	public void setUp() {
		atoms = new AtomsType("atoms", new IntegerIntervall(0, 2));
		intset = new IntsetType("intsets", new IntegerIntervall(3, 8),
				new int[] { 0, 3, 5, 1, 2, 4 });
		powset = new Pow2Type("powints", new IntegerIntervall(6, 8), new int[] {
				1, 2, 4 });
	}

	@Test
	public void testValidConstructor() {
		new TupleType(new Type[] { atoms, intset }, false);
		new TupleType(new Type[] { atoms }, false);
		new TupleType(new Type[] { atoms }, true);
		new TupleType(new Type[] { powset }, true);
		new TupleType(new Type[] { atoms, powset }, true);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor1() {
		new TupleType(new Type[] { powset }, false);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testInvalidConstructor2() {
		new TupleType(new Type[] { atoms, powset }, false);
	}

	@Test
	public void testPowIntSingleton() {
		final TupleType tt = new TupleType(new Type[] { powset }, true);
		final TupleSet t1 = createSingleton(tt, 5);
		final int[] result = tt.decodeTuple(t1.iterator().next(), t1);
		assertEquals("expected 1-tuple", 1, result.length);
		assertEquals("expected input-value", 5, result[0]);
	}

	@Test
	public void testPair() {
		final TupleType tt = new TupleType(new Type[] { powset, atoms }, true);
		final TupleSet t1 = createSingleton(tt, 5, 2);
		final int[] result = tt.decodeTuple(t1.iterator().next(), t1);
		assertEquals("expected pair", 2, result.length);
		assertEquals("expected input-value", 5, result[0]);
		assertEquals("expected input-value", 2, result[1]);
	}

	@Test
	public void testRelation() {
		final TupleType tt = new TupleType(new Type[] { intset, atoms }, false);
		final Universe universe = createUniverse();
		final Collection<int[]> inputSet = new ArrayList<int[]>();
		final int[] firstInput = new int[] { 5, 2 };
		final int[] secondInput = new int[] { 3, 1 };
		inputSet.add(firstInput);
		inputSet.add(secondInput);
		final TupleSet tupleSet = tt.createTupleSet(universe, inputSet);
		assertEquals("expected tupleSet size is 2", 2, tupleSet.size());
		boolean foundFirst = false, foundSecond = false;
		for (Tuple tuple : tupleSet) {
			final int[] result = tt.decodeTuple(tuple, tupleSet);
			foundFirst |= Arrays.equals(firstInput, result);
			foundSecond |= Arrays.equals(secondInput, result);
		}
		assertEquals("expected first input", true, foundFirst);
		assertEquals("expected second input", true, foundSecond);
	}

	private TupleSet createSingleton(final TupleType tupleType,
			final int... ints) {
		final Universe universe = createUniverse();
		final Collection<int[]> coll = Collections.singleton(ints);
		return tupleType.createTupleSet(universe, coll);
	}

	private Universe createUniverse() {
		return KodkodUtil.createUniverse(0, 9);
	}
}
