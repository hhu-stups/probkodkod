/**
 * 
 */
package de.stups.probkodkod.test;

import java.util.ArrayList;
import java.util.Collection;

import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

/**
 * @author plagge
 * 
 */
public class KodkodUtil {
	public static Universe createUniverse(final int offset, final int atomNumber) {
		Collection<Object> atoms = new ArrayList<Object>();
		for (int i = 0; i < offset; i++) {
			atoms.add("Dummy Atom " + i);
		}
		for (int i = 0; i < atomNumber; i++) {
			atoms.add("Atom" + i);
		}
		return new Universe(atoms);
	}

	public static TupleSet fetchTupleSet(final Universe universe,
			final int[] ints) {
		final TupleFactory factory = universe.factory();
		return factory.setOf(fetchAtoms(universe, ints));

	}

	private static Object[] fetchAtoms(final Universe universe, final int[] ints) {
		Object[] result = new Object[ints.length];
		for (int i = 0; i < ints.length; i++) {
			result[i] = universe.atom(ints[i]);
		}
		return result;
	}

	public static int[] createInts(final int width) {
		int[] result = new int[width];
		for (int i = 0; i < width; i++) {
			result[i] = i;
		}
		return result;
	}

}
