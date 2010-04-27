/**
 * 
 */
package de.stups.probkodkod.bounds;

import kodkod.ast.Relation;
import kodkod.instance.Bounds;
import kodkod.instance.TupleSet;

/**
 * An exact bound defines a relation with a constant value by a {@link TupleSet}
 * 
 * @author plagge
 */
public class ExactBound implements AbstractBound {
	private final TupleSet tupleSet;

	public ExactBound(final TupleSet tupleSet) {
		this.tupleSet = tupleSet;
	}

	@Override
	public void setBound(final Relation relation, final Bounds bounds) {
		bounds.boundExactly(relation, tupleSet);
	}

	/**
	 * An {@link ExactBound} is never a variable as its values are completely
	 * determined by the {@link TupleSet}.
	 */
	@Override
	public boolean isVariable() {
		return false;
	}
}