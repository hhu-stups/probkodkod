/**
 * 
 */
package de.stups.probkodkod.types;

import static junit.framework.Assert.assertEquals;

import java.util.Arrays;

import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.IntegerIntervall;
import de.stups.probkodkod.test.KodkodUtil;
import de.stups.probkodkod.test.Permutations;
import de.stups.probkodkod.types.Pow2Type;
import de.stups.probkodkod.types.Type;

/**
 * Test cases for the {@link Pow2Type}
 * 
 * @author plagge
 */
public class Pow2TypeTest {
	private static int BITWIDTH_TEST = 4;

	@Test
	public void testTupleSetNeeded() {
		final int[] pow2s = createPowersOfTwo(1);
		final Type type = new Pow2Type("test", new IntegerIntervall(0, 1),
				pow2s);
		assertEquals("tuple set is needed expected", true,
				type.oneValueNeedsCompleteTupleSet());
	}

	@Test
	public void testAllDecodeEncodeWithoutOffset() {
		testAllDecodeEncode(0);
	}

	@Test
	public void testAllDecodeEncodeWithOffset() {
		testAllDecodeEncode(4);

	}

	public void testAllDecodeEncode(final int offset) {
		int[] powersOf2 = createPowersOfTwo(BITWIDTH_TEST);
		final int maxint = (1 << BITWIDTH_TEST) - 1;
		final Universe universe = KodkodUtil.createUniverse(offset,
				BITWIDTH_TEST);
		final TupleFactory factory = universe.factory();
		for (final int[] perm : new Permutations(powersOf2)) {
			for (int i = 0; i < maxint; i++) {
				testDecodeEncode(offset, factory, perm, i);
			}
		}
	}

	private void testDecodeEncode(final int offset, final TupleFactory factory,
			final int[] perm, final int i) {
		final Type type = new Pow2Type("test", new IntegerIntervall(offset,
				offset + BITWIDTH_TEST - 1), perm);
		final int[] atoms = type.encode(i);
		final TupleSet tupleSet = KodkodUtil.fetchTupleSet(factory.universe(),
				atoms);
		final int value = type.decode(0, null, tupleSet);
		assertEquals(
				"Result must match input for permutation "
						+ Arrays.toString(perm), i, value);
	}

	private int[] createPowersOfTwo(final int width) {
		int[] result = new int[width];
		int p2 = 1;
		for (int i = 0; i < width; i++) {
			result[i] = p2;
			p2 <<= 1;
		}
		return result;
	}

}
