/**
 * 
 */
package de.stups.probkodkod.bounds;

import kodkod.ast.Relation;
import kodkod.instance.Bounds;
import kodkod.instance.TupleSet;

/**
 * This bound is used for variables. The values of the relation can take any
 * value that is a subset of the given TupleSet.
 * 
 * @author plagge
 */
public class SubsetBound implements AbstractBound {
	private final TupleSet tupleSet;

	public SubsetBound(final TupleSet tupleSet) {
		this.tupleSet = tupleSet;
	}

	@Override
	public void setBound(final Relation relation, final Bounds bounds) {
		bounds.bound(relation, tupleSet);
	}

	/**
	 * The value of the relation is any subset of the given tupleSet, so it is
	 * not yet determined. It is a variable.
	 */
	@Override
	public boolean isVariable() {
		return true;
	}
}