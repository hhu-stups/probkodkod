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
import de.prob.prolog.output.IPrologTermOutput;
import de.stups.probkodkod.types.TupleType;

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
					final TupleType tupleType = relinfo.getTupleType();
					pto.openTerm(tupleType.isSingleton() ? "b" : "s");
					pto.printAtom(relinfo.getId());
					if (tupleType.isSingleton()) {
						final Tuple tuple = tupleSet.isEmpty() ? null
								: tupleSet.iterator().next();
						writeTuple(pto, tupleType, tupleSet, tuple);
					} else {
						pto.openList();
						writeTupleSet(pto, tupleType, tupleSet);
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
			final TupleType tupleType, final TupleSet tupleSet) {
		for (Tuple tuple : tupleSet) {
			writeTuple(pto, tupleType, tupleSet, tuple);
		}
	}

	private void writeTuple(final IPrologTermOutput pto,
			final TupleType tupleType, final TupleSet tupleSet,
			final Tuple tuple) {
		final int[] intTuple = tupleType.decodeTuple(tuple, tupleSet);
		pto.openList();
		for (int elem : intTuple) {
			pto.printNumber(elem);
		}
		pto.closeList();
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
