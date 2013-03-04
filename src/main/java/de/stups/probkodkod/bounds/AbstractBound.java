/**
 * 
 */
package de.stups.probkodkod.bounds;

import kodkod.ast.Relation;
import kodkod.instance.Bounds;

/**
 * A bound constrains the possible values of a relation for a
 * 
 * @author plagge
 */
public interface AbstractBound {

	/**
	 * Constraints the possible values of a relation.
	 * 
	 * @param relation
	 *            the Kodkod relation, never <code>null</code>
	 * @param bounds
	 *            the Kodkod bounds, specific for a single request, never
	 *            <code>null</code>.
	 */
	void setBound(final Relation relation, final Bounds bounds);

	/**
	 * @return if the bound is such that the value of the relation is not yet
	 *         completely determined. The tools sends only values of variables.
	 */
	boolean isVariable();
}