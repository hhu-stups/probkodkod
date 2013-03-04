/**
 * 
 */
package de.stups.probkodkod.bounds;

import kodkod.ast.Relation;
import kodkod.instance.Bounds;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;
import de.stups.probkodkod.IntegerIntervall;

/**
 * This bound is used to define which values a type relation can have. It is
 * basically the same as the {@link ExactBound}, but it takes an
 * {@link IntegerIntervall} in the constructor instead of a {@link TupleSet}.
 * This is so because when the bounds for the type are created, the universe is
 * not yet created because its final size is unknown.
 * 
 * As soon as the universe has been created, {@link #createTupleSets(Universe)}
 * must be called before using this class.
 * 
 * @author plagge
 */
public class TypeBound implements AbstractBound {
	private final IntegerIntervall interval;
	private TupleSet tupleSet;

	public TypeBound(final IntegerIntervall interval) {
		this.interval = interval;
	}

	/**
	 * It's a type, as such constant.
	 */
	@Override
	public boolean isVariable() {
		return false;
	}

	@Override
	public void setBound(final Relation relation, final Bounds bounds) {
		bounds.boundExactly(relation, tupleSet);
	}

	/**
	 * Usually, when the type is created, the universe is not yet created
	 * because it is not known how large it will be after registering all types.
	 * This method has to be called after the universe has been created to
	 * instantiate the {@link TupleSet} that includes all values of the type.
	 * 
	 * @param universe
	 */
	public void createTupleSets(final Universe universe) {
		final TupleFactory factory = universe.factory();
		final Tuple from = factory.tuple(universe.atom(interval.getLower()));
		final Tuple to = factory.tuple(universe.atom(interval.getUpper()));
		tupleSet = factory.range(from, to);
	}

}
