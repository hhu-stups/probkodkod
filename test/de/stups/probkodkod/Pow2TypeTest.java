/**
 * 
 */
package de.stups.probkodkod;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Test;

import de.stups.probkodkod.prolog.IntegerPrologTerm;
import de.stups.probkodkod.prolog.PrologTerm;
import de.stups.probkodkod.prolog.StructuredPrologOutput;
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
	public void testAllDecodeEncode() {
		int[] powersOf2 = createPowersOfTwo(BITWIDTH_TEST);
		final int maxint = (1 << BITWIDTH_TEST) - 1;
		final Universe universe = new Universe(createAtoms(BITWIDTH_TEST));
		final TupleFactory factory = universe.factory();
		for (final int[] perm : new Permutations(powersOf2)) {
			for (int i = 0; i < maxint; i++) {
				testDecodeEncode(factory, perm, i);
			}
		}
	}

	private void testDecodeEncode(final TupleFactory factory, final int[] perm,
			final int i) {
		final Type type = new Pow2Type("test", new IntegerIntervall(0,
				BITWIDTH_TEST - 1), perm);
		final int[] atoms = type.encode(i);
		final TupleSet tupleSet = factory.setOf(createAtoms(atoms));
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

	private static Collection<Object> createAtoms(final int count) {
		Collection<Object> result = new ArrayList<Object>(count);
		for (int i = 0; i < count; i++) {
			result.add(createAtom(i));
		}
		return result;
	}

	private static Object[] createAtoms(final int[] ints) {
		Object[] result = new Object[ints.length];
		for (int i = 0; i < ints.length; i++) {
			result[i] = createAtom(ints[i]);
		}
		return result;
	}

	private static Object createAtom(final int num) {
		return "Atom" + num;
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
