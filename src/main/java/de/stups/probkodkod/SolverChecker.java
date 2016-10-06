/**
 * 
 */
package de.stups.probkodkod;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.stups.probkodkod.sat.SAT4JWithTimeoutFactory;
import kodkod.ast.Formula;
import kodkod.ast.IntConstant;
import kodkod.ast.Relation;
import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.Bounds;
import kodkod.instance.Universe;

/**
 * Here we determine if we use Minisat or SAT4J as back-end. We do this by
 * trying to load minisat.
 * 
 * @author plagge
 */
public class SolverChecker {
	private static final Logger LOGGER = Logger.getLogger("de.stups.probkodkod");

	public static SATFactory determineSatFactory(SATSolver satSolver) {
		switch (satSolver) {
		case glucose:
			return determineSatFactory(SATFactory.Glucose, new SAT4JWithTimeoutFactory());
		case lingeling:
			return determineSatFactory(SATFactory.Lingeling, new SAT4JWithTimeoutFactory());
		case minisat:
			return determineSatFactory(SATFactory.MiniSat, new SAT4JWithTimeoutFactory());
		case sat4j:
			return determineSatFactory(new SAT4JWithTimeoutFactory());
		default:
			throw new Error("No valid SAT solver back-end for Kodkod selected.");
		}
	}

	public static SATFactory determineSatFactory(SATFactory... factories) {
		Map<String, Throwable> throwables = new HashMap<String, Throwable>();
		for (final SATFactory factory : factories) {
			try {
				check(factory);
				// the factory seems to work, we use it
				LOGGER.info("Using SAT solver back-end: " + factory);
				return factory;
			} catch (Throwable t) {
				// the factory does not work, we store the stack-trace for the
				// case that no solvers are found
				throwables.put(factory.toString(), t);
			}
		}
		LOGGER.severe("No SAT solver back-end found.");
		for (final Map.Entry<String, Throwable> entry : throwables.entrySet()) {
			final String msg = "Error when trying to use solver: " + entry.getKey();
			LOGGER.log(Level.SEVERE, msg, entry.getValue());
		}
		throw new Error("No SAT solver back-end for Kodkod found.");
	}

	private static void check(SATFactory factory) {
		final Solver solver = new Solver();
		solver.options().setSolver(factory);
		final Universe universe = new Universe("a", "b");
		final Relation relation = Relation.unary("relation");
		final Formula formula = relation.count().eq(IntConstant.constant(2));
		final Bounds bounds = new Bounds(universe);
		bounds.bound(relation, universe.factory().allOf(1));
		solver.solve(formula, bounds);
	}

}
