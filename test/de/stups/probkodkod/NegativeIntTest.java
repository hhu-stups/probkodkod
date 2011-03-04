/**
 * 
 */
package de.stups.probkodkod;

import java.util.Iterator;

import kodkod.ast.Formula;
import kodkod.ast.IntConstant;
import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author plagge
 * 
 */
public class NegativeIntTest {

	@Test
	public void testNegativeIntegers() {
		for (int value = -8; value <= 7; value++) {
			testNegativeInteger(value);
		}
	}

	private void testNegativeInteger(final int value) {
		final Universe universe = new Universe(1, -8, 2, 4);
		final TupleFactory factory = universe.factory();
		final Bounds bounds = new Bounds(universe);
		bounds.boundExactly(1, factory.setOf(1));
		bounds.boundExactly(2, factory.setOf(2));
		bounds.boundExactly(4, factory.setOf(4));
		bounds.boundExactly(-8, factory.setOf(-8));
		final Relation x = Relation.unary("x");
		bounds.bound(x, factory.allOf(1));
		final Formula formula = x.sum().eq(IntConstant.constant(value));
		final Solver solver = new Solver();
		solver.options().setSolver(SATFactory.MiniSat);
		solver.options().setBitwidth(5);
		final Iterator<Solution> iterator = solver.solveAll(formula, bounds);
		Assert.assertTrue("solution expected", iterator.hasNext());
		final Solution solution = iterator.next();
		final Instance instance = solution.instance();
		Assert.assertNotNull("Instance should not be null", instance);
		final TupleSet tuples = instance.tuples(x);
		int sum = 0;
		for (final Tuple tuple : tuples) {
			final Object atom = tuple.atom(0);
			sum += ((Integer) atom).intValue();
		}
		Assert.assertEquals("constant in formula and solution should be same",
				value, sum);
		Assert.assertTrue("no other solution expected", !iterator.hasNext()
				|| iterator.next().instance() == null);
	}
}
