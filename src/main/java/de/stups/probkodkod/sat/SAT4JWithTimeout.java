package de.stups.probkodkod.sat;

import kodkod.engine.satlab.SATSolver;

import org.sat4j.core.VecInt;
import org.sat4j.minisat.SolverFactory;
import org.sat4j.specs.ContradictionException;
import org.sat4j.specs.ISolver;

final class SAT4JWithTimeout implements SATSolver {
	private ISolver solver;
	private boolean sat = true;
	private int vars, clauses;
	private long timeout;

	SAT4JWithTimeout(long ms) {
		this.solver = SolverFactory.instance().defaultSolver();
		this.solver.setTimeoutMs(ms);
		this.timeout = ms;
		this.vars = 0;
		this.clauses = 0;
	}

	public int numberOfVariables() {
		return vars;
	}

	public int numberOfClauses() {
		return clauses;
	}

	public void addVariables(int numVars) {
		if (numVars < 0)
			throw new IllegalArgumentException("numVars < 0: " + numVars);
		else if (numVars > 0) {
			vars += numVars;
			solver.newVar(vars);
		}
	}

	public boolean addClause(int[] lits) {
		try {
			if (sat) {
				clauses++;
				solver.addClause(new VecInt(lits));
				return true;
			}
		} catch (ContradictionException e) {
			sat = false;
		}
		return false;
	}

	public boolean solve() {
		try {
			if (sat)
				sat = solver.isSatisfiable();
			return sat;
		} catch (org.sat4j.specs.TimeoutException e) {
			throw new RuntimeException("timed out");
		}
	}

	public final boolean valueOf(int variable) {
		if (!sat)
			throw new IllegalStateException();
		if (variable < 1 || variable > vars)
			throw new IllegalArgumentException(variable + " !in [1.." + vars
					+ "]");
		return solver.model(variable);
	}

	public synchronized final void free() {
		solver = null;
	}

	public String toString() {
		return "SAT4JWithTimeout Solver using " + timeout + " ms timeout";
	}
}