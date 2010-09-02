/**
 * 
 */
package de.stups.probkodkod;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.Arrays;

import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.prolog.IntegerPrologTerm;
import de.stups.probkodkod.prolog.PrologTerm;
import de.stups.probkodkod.prolog.StructuredPrologOutput;
import de.stups.probkodkod.test.KodkodUtil;
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
		final int value = decode(type, tupleSet);
		assertEquals(
				"Result must match input for permutation "
						+ Arrays.toString(perm), i, value);
	}

	private int decode(final Type type, final TupleSet tupleSet) {
		final StructuredPrologOutput pto = new StructuredPrologOutput();
		type.writeResult(pto, 0, null, tupleSet);
		pto.fullstop();
		assertEquals("Exactly one term expected", 1, pto.getSentences().size());
		PrologTerm term = pto.getSentences().iterator().next();
		assertTrue("IntegerPrologTerm expected",
				term instanceof IntegerPrologTerm);
		final int value = ((IntegerPrologTerm) term).getValue().intValue();
		return value;
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
