package de.stups.probkodkod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import kodkod.ast.Relation;
import kodkod.engine.Solution;
import kodkod.instance.Instance;
import kodkod.instance.Tuple;
import kodkod.instance.TupleSet;
import de.stups.probkodkod.prolog.IPrologTermOutput;
import de.stups.probkodkod.types.Type;

/**
 * A request stores the information about a ongoing query in the current
 * session. Mainly objects of this class store which variables have to be send
 * to the client and an iterator over the solutions of the kodkod solver.
 * 
 * @author plagge
 */
public final class Request {
	private static Logger logger = Logger.getLogger(Request.class.getName());

	private final RelationInfo[] variables;
	private final Iterator<Solution> iterator;

	public Request(final RelationInfo[] variables,
			final Iterator<Solution> iterator) {
		this.variables = variables;
		this.iterator = iterator;
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("request created: return variables are: "
					+ Arrays.asList(variables));
		}
	}

	public Map<String, TupleSet> nextSolution() {
		Map<String, TupleSet> result = null;
		Instance instance = nextInstance();
		if (instance != null) {
			result = new HashMap<String, TupleSet>();
			for (int i = 0; i < variables.length; i++) {
				final RelationInfo relinfo = variables[i];
				final Relation relation = relinfo.getRelation();
				result.put(relinfo.getId(), instance.tuples(relation));
			}
		}
		return result;
	}

	/**
	 * Writes the next solutions to the output stream.
	 * 
	 * @param writer
	 *            the output stream
	 * @param max
	 *            maximum number of solutions to write to the stream
	 * @return true if there are possibly more solutions, false if all solutions
	 *         have been send to the stream
	 */
	public boolean writeNextSolutions(final IPrologTermOutput pto, final int max) {
		pto.openTerm("solutions");
		pto.openList();
		boolean solutionsPresent = true;
		int num;
		for (num = 0; solutionsPresent & num < max; num++) {
			Instance instance = nextInstance();
			solutionsPresent = instance != null;
			if (solutionsPresent) {
				pto.openList();
				for (int i = 0; i < variables.length; i++) {
					final RelationInfo relinfo = variables[i];
					final TupleSet tupleSet = instance.tuples(relinfo
							.getRelation());
					pto.openTerm("b");
					pto.printAtom(relinfo.getId());
					if (relinfo.isSingleton()) {
						final Tuple tuple = tupleSet.iterator().next();
						writeTuple(pto, tupleSet, relinfo.getTypes(), tuple);
					} else {
						pto.openList();
						writeTupleSet(pto, tupleSet, relinfo.getTypes());
						pto.closeList();
					}
					pto.closeTerm();
				}
				pto.closeList();
			}
		}
		pto.closeList();
		pto.printAtom(solutionsPresent ? "more" : "all");
		if (logger.isLoggable(Level.FINE)) {
			logger.fine("wrote " + num + " solutions");
		}
		pto.closeTerm();
		pto.fullstop();
		return solutionsPresent;
	}

	private void writeTupleSet(final IPrologTermOutput pto,
			final TupleSet tupleSet, final Type[] types) {
		for (Tuple tuple : tupleSet) {
			writeTuple(pto, tupleSet, types, tuple);
		}
	}

	private void writeTuple(final IPrologTermOutput pto,
			final TupleSet tupleSet, final Type[] types, final Tuple tuple) {
		pto.openTerm("t");
		pto.openList();
		final int arity = tupleSet.arity();
		for (int index = 0; index < arity; index++) {
			types[index].writeResult(pto, index, tuple, tupleSet);
		}
		pto.closeList();
		pto.closeTerm();
	}

	private Instance nextInstance() {
		Instance instance;
		if (iterator.hasNext()) {
			Solution solution = iterator.next();
			instance = solution.instance();
		} else {
			instance = null;
		}
		return instance;
	}
}
