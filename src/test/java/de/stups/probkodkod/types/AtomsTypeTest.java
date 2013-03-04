package de.stups.probkodkod.types;

import static junit.framework.Assert.assertEquals;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.test.KodkodUtil;

public class AtomsTypeTest {
	private static final int TESTSIZE = 3;

	@Test
	public void testIsSetenabled() {
		final Type type = new AtomsType("test", new IntegerIntervall(0, 1));
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
		final IntegerIntervall interval = new IntegerIntervall(offset, offset
				+ TESTSIZE - 1);
		final SetEnabledType type = new AtomsType("test", interval);
		for (final int input : ints) {
			final int[] atoms = type.encode(input);
			assertEquals("exactly one atom expected", 1, atoms.length);
			final int singleAtom = type.encodeElement(input);
			assertEquals("encodeElement must match encode", atoms[0],
					singleAtom);
			final TupleSet tupleSet = KodkodUtil.fetchTupleSet(universe, atoms);
			final Tuple tuple = tupleSet.iterator().next();
			final int output = type.decode(0, tuple, tupleSet);
			assertEquals("output must match input", input, output);
		}
	}

}
