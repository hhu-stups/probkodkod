/**
 * 
 */
package de.stups.probkodkod;

import kodkod.instance.TupleFactory;
import kodkod.instance.Universe;

/**
 * The TypedTupleFactory is a typed variant of Kodkod's TupleFactory. For each
 * call to create a tuple of tupleSet, the intervals of the underlying types
 * have to be given.
 * 
 * The factory then maps the type-specific inputs to atoms in the universe. E.g.
 * a specified problem has two types A and B with 3 resp. 5 atoms. The whole
 * universe therefore consists of 8 atoms:
 * 
 * A0 A1 A2 B0 B1 B2 B3 B4
 * 
 * The TypedTupleFactory maps now request like "create a typed tuple (A0,B2)" to
 * a tuple of atoms (0,5).
 * 
 * @see TupleFactory
 * @see Universe
 * @author plagge
 */
public class TypedTupleFactory {
	private final Universe universe;

	public TypedTupleFactory(final Universe universe) {
		this.universe = universe;
	}

	/**
	 * 
	 * @return the underlying Kodkod universe, never <code>null</code>.
	 */
	public Universe getUniverse() {
		return universe;
	}

}
