package de.stups.probkodkod.sat;

import kodkod.engine.satlab.SATFactory;
import kodkod.engine.satlab.SATSolver;

public class SAT4JWithTimeoutFactory extends SATFactory {
	private long timeout;

	public SAT4JWithTimeoutFactory() {
		this(1500);
	}

	public SAT4JWithTimeoutFactory(long ms) {
		this.timeout = ms;
	}

	@Override
	public SATSolver instance() {
		return new SAT4JWithTimeout(timeout);
	}

	public String toString() {
		return "SAT4J with timeout of " + timeout + " ms";
	}
}
