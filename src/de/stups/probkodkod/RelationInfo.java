/**
 * 
 */
package de.stups.probkodkod;

import kodkod.ast.Relation;
import kodkod.instance.Bounds;
import de.stups.probkodkod.bounds.AbstractBound;
import de.stups.probkodkod.types.TupleType;

/**
 * This class contains various information about a relation that is declared for
 * a problem.
 * 
 * @author plagge
 */
public class RelationInfo {
	private final String id;
	private final Relation relation;
	private final AbstractBound bound;
	private final TupleType type;

	public RelationInfo(final String id, final Relation relation,
			final AbstractBound bound, final TupleType type) {
		this.id = id;
		this.relation = relation;
		this.bound = bound;
		this.type = type;

		if (bound.isVariable()) {
			if (id == null || relation == null)
				throw new IllegalArgumentException(
						"For variables, id and relation must not be null");
		}
	}

	/**
	 * @return the ID of the relation, never <code>null</code>
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the relation itself, never <code>null</code>
	 */
	public Relation getRelation() {
		return relation;
	}

	/**
	 * @return the bound that belongs to the relation to constrain its values,
	 *         never <code>null</code>
	 * @see AbstractBound
	 */
	public AbstractBound getBound() {
		return bound;
	}

	/**
	 * 
	 * @return <code>true</code> iff the relation is a variable, this is exactly
	 *         the case if its bound is exact
	 */
	public boolean isVariable() {
		return bound.isVariable();
	}

	/**
	 * @return <code>true</code> iff the relation is declared as a singleton
	 *         relation
	 */
	public boolean isSingleton() {
		return type.isSingleton();
	}

	/**
	 * @return the n types of this n-ary relation, never <code>null</code>, all
	 *         elements are not <code>null</code>.
	 */
	public TupleType getTupleType() {
		return type;
	}

	/**
	 * Use the abstract bound of the relation to add a bound to the bounds of a
	 * concrete Kodkod solver call. The bound constrains the possible values of
	 * the relation for that call. In case of a constant, it exactly sets its
	 * values, in case of a variable it types the relations by allowing only the
	 * atoms
	 * 
	 * @param bounds
	 */
	public void setBound(final Bounds bounds) {
		bound.setBound(relation, bounds);
	}

}
