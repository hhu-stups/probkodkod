package de.stups.probkodkod;

import static junit.framework.Assert.assertEquals;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.test.KodkodUtil;
import de.stups.probkodkod.test.Permutations;
import de.stups.probkodkod.types.IntsetType;
import de.stups.probkodkod.types.Type;

public class IntsetTypeTest {
	private static final int TESTSIZE = 3;

	@Test
	public void testAllCombinations() {
		testAllCombinations(0);
	}

	@Test
	public void testAllCombinationsWithOffset() {
		testAllCombinations(2);
	}

	private void testAllCombinations(final int offset) {
		final int[] ints = createInts(TESTSIZE);
		final Universe universe = KodkodUtil.createUniverse(offset, TESTSIZE);
		for (final int[] perm : new Permutations(ints)) {
			testAllEncodeDecodes(offset, universe, perm);
		}
	}

	private void testAllEncodeDecodes(final int offset,
			final Universe universe, final int[] perm) {
		final IntegerIntervall interval = new IntegerIntervall(offset, offset
				+ TESTSIZE - 1);
		final Type type = new IntsetType("test", interval, perm);
		for (final int input : perm) {
			final int[] atoms = type.encode(input);
			final TupleSet tupleSet = KodkodUtil.fetchTupleSet(universe, atoms);
			assertEquals("exactly one atom expected", 1, tupleSet.size());
			final Tuple tuple = tupleSet.iterator().next();
			final int output = type.decode(0, tuple, tupleSet);
			assertEquals("output must match input", input, output);
		}
	}

	private static int[] createInts(final int length) {
		int[] result = new int[length];
		for (int i = 0; i < length; i++) {
			result[i] = i;
		}
		return result;
	}
}
