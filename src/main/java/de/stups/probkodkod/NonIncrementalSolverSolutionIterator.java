package de.stups.probkodkod;

import java.util.Iterator;

import kodkod.engine.Solution;

public class NonIncrementalSolverSolutionIterator implements Iterator<Solution> {

	private Solution sol;
	private boolean returned = false;

	public NonIncrementalSolverSolutionIterator(Solution sol) {
		this.sol = sol;
	}

	@Override
	public boolean hasNext() {
		return true;
	}

	@Override
	public Solution next() {
		if (!returned) {
			returned = true;
			return sol;
		}
		throw new RuntimeException("second solution of non-incremental solver requested");
	}

}
