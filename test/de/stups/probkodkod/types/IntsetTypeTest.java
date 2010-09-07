package de.stups.probkodkod.types;

import static junit.framework.Assert.assertEquals;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.test.KodkodUtil;
import de.stups.probkodkod.test.Permutations;

public class IntsetTypeTest {
	private static final int TESTSIZE = 3;

	@Test
	public void testIsSetenabled() {
		final int[] ints = KodkodUtil.createInts(1);
		final Type type = new IntsetType("test", new IntegerIntervall(0, 1),
				ints);
		assertEquals("one tuple should be enough", false,
				type.oneValueNeedsCompleteTupleSet());
	}

	@Test
	public void testAllCombinations() {
		testAllCombinations(0);
	}

	@Test
	public void testAllCombinationsWithOffset() {
		testAllCombinations(2);
	}

	private void testAllCombinations(final int offset) {
		final int[] ints = KodkodUtil.createInts(TESTSIZE);
		final Universe universe = KodkodUtil.createUniverse(offset, TESTSIZE);
		for (final int[] perm : new Permutations(ints)) {
			testAllEncodeDecodes(offset, universe, perm);
		}
	}

	private void testAllEncodeDecodes(final int offset,
			final Universe universe, final int[] perm) {
		final IntegerIntervall interval = new IntegerIntervall(offset, offset
				+ TESTSIZE - 1);
		final SetEnabledType type = new IntsetType("test", interval, perm);
		for (final int input : perm) {
			final int[] atoms = type.encode(input);
			assertEquals("exactly one atom expected", 1, atoms.length);
			final int singleAtom = type.encodeElement(input);
			assertEquals("encodeElement should match encode", atoms[0],
					singleAtom);
			final TupleSet tupleSet = KodkodUtil.fetchTupleSet(universe, atoms);
			final Tuple tuple = tupleSet.iterator().next();
			final int output = type.decode(0, tuple, tupleSet);
			assertEquals("output must match input", input, output);
		}
	}

}
