package de.stups.probkodkod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.stups.probkodkod.types.TupleType;
import kodkod.ast.Formula;
import kodkod.engine.Solution;
import kodkod.engine.Solver;
import kodkod.instance.Bounds;
import kodkod.instance.TupleFactory;
import kodkod.instance.TupleSet;
import kodkod.instance.Universe;

/**
 * This is the immutable Version of a Kodkod problem description. Objects of
 * this class are usually stored in the {@link KodkodSession}.
 * 
 * Aside the universe, the relations and the formula, this class contains the
 * default bounds for the relations. It distinguishes between exact bounds
 * (constants) and upper bounds (variables). In a request (see
 * {@link #createRequest(Solver, Map)}), values for variables can be given and
 * the computed values of the remaining variables are returned. This way, no
 * redundant constants are passed between client and server.
 * 
 * @see Problem
 * @author plagge
 */
public final class ImmutableProblem {
	private static final Logger logger = Logger.getLogger(ImmutableProblem.class.getName());

	// The id of the problem
	private final String id;
	// The Kodkod formula to solve
	private final Formula formula;
	// The bounds of those relations that are already known, e.g. for constants
	// and types. This bounds are later cloned to add bounds for values sent by
	// the client
	private final Bounds preBounds;
	// The relation information of all variables (= relations without exact
	// bounds)
	// The relation information of constants is not needed because that
	// information is already stored in the preBounds above
	private final RelationInfo[] variableInfos;
	// The same but stored in a map for faster look-up
	private final Map<String, RelationInfo> relations = new HashMap<String, RelationInfo>();;
	// The bitwidth that the solver should use for this problem
	private final Integer solverBitwidth;
	// The factory is used to create tuples and tupleSets
	private final Universe universe;

	public ImmutableProblem(final String id, final Formula formula, final Integer bitwidth, final Universe universe,
			final Collection<RelationInfo> infos, final int numberOffset, final int[] numbers) {
		this.id = id;
		this.solverBitwidth = bitwidth;
		this.universe = universe;

		variableInfos = new RelationInfo[countVariables(infos)];
		preBounds = setUpBounds(universe, variableInfos, relations, infos);
		registerNumbers(preBounds, numberOffset, numbers);

		this.formula = addOnePredicates(formula, variableInfos);

		if (logger.isLoggable(Level.FINE)) {
			List<String> varnames = new ArrayList<String>();
			for (final RelationInfo relinfo : variableInfos) {
				varnames.add(relinfo.getId());
			}
			logger.fine("'" + id + "' created: vars=" + varnames);
		}
	}

	private static Bounds setUpBounds(final Universe universe, final RelationInfo[] variableInfos,
			final Map<String, RelationInfo> relations, final Collection<RelationInfo> infos) {
		Bounds preBounds = new Bounds(universe);
		int indexVar = 0;
		for (final RelationInfo relinfo : infos) {
			final String relid = relinfo.getId();
			// the information of variables is stored for later use,
			// those of constants is stored into the bounds
			if (relinfo.isVariable()) {
				relations.put(relid, relinfo);
				variableInfos[indexVar] = relinfo;
				indexVar++;
			} else {
				relinfo.setBound(preBounds);
			}
		}
		return preBounds;
	}

	private static void registerNumbers(final Bounds bounds, final int numberOffset, final int[] numbers) {
		if (numbers != null) {
			final Universe universe = bounds.universe();
			final TupleFactory factory = universe.factory();
			for (int i = 0; i < numbers.length; i++) {
				final Object currentAtom = universe.atom(numberOffset + i);
				bounds.boundExactly(numbers[i], factory.setOf(currentAtom));
			}
		}
	}

	private Formula addOnePredicates(Formula formula, final RelationInfo[] variables) {
		for (RelationInfo relinfo : variables) {
			final TupleType tupleType = relinfo.getTupleType();
			if (tupleType.formulaOneShouldBeAdded()) {
				final Formula isOne = relinfo.getRelation().one();
				formula = isOne.and(formula);
			}
		}
		return formula;
	}

	private static int countVariables(final Collection<RelationInfo> infos) {
		int numberOfVariables = 0;
		for (final RelationInfo relinfo : infos) {
			if (relinfo.isVariable()) {
				numberOfVariables++;
			}
		}
		return numberOfVariables;
	}

	public String getId() {
		return id;
	}

	public Formula getFormula() {
		return formula;
	}

	public Integer getBitwidth() {
		return solverBitwidth;
	}

	public RelationInfo lookupRelationInfo(final String id) {
		final RelationInfo info = relations.get(id);
		if (id == null)
			throw new IllegalArgumentException("unknown relation " + id);
		return info;
	}

	/**
	 * Create a request.
	 * 
	 * @param solver
	 *            A working solver instance, never <code>null</code>.
	 * @param signum
	 *            The signum of the formula (positive: formula unchanged,
	 *            negative: formula negated)
	 * @param values
	 *            a mapping from some variables to their values, never
	 *            <code>null</code>.
	 * @return A request object
	 * @see Request
	 */
	public Request createRequest(final Solver solver, final boolean signum, final Map<String, TupleSet> values) {
		final Bounds bounds = preBounds.clone();

		final int resultSize = variableInfos.length - values.size();
		RelationInfo variables[] = new RelationInfo[resultSize];
		int solIndex = 0;
		for (int i = 0; i < variableInfos.length; i++) {
			final RelationInfo relinfo = variableInfos[i];
			final TupleSet tupleSet = values.get(relinfo.getId());
			if (tupleSet == null) {
				relinfo.setBound(bounds);
				variables[solIndex] = relinfo;
				solIndex++;
			} else {
				bounds.boundExactly(relinfo.getRelation(), tupleSet);
			}
		}
		final Formula effFormula = signum ? formula : formula.not();

		try {
			Iterator<Solution> iterator = solver.solveAll(effFormula, bounds);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("request for '" + id + "': args=" + values.keySet() + ", remaining vars="
						+ Arrays.asList(variables));
				logger.info("formula: " + effFormula);
				logger.info(bounds.toString());
			}
			return new Request(variables, iterator);
		} catch (IllegalArgumentException e) {
			Solution sol = solver.solve(effFormula, bounds);
			if (logger.isLoggable(Level.FINE)) {
				logger.fine("request for '" + id + "': args=" + values.keySet() + ", remaining vars="
						+ Arrays.asList(variables));
				logger.info("formula: " + effFormula);
				logger.info(bounds.toString());
			}

			return new Request(variables, Arrays.asList(sol).iterator());
		}
	}

	public Universe getUniverse() {
		return universe;
	}

}
