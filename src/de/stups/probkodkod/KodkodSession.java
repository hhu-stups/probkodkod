package de.stups.probkodkod;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import kodkod.engine.Solver;
import kodkod.engine.satlab.SATFactory;
import kodkod.instance.TupleSet;
import de.prob.prolog.output.IPrologTermOutput;

/**
 * The session contains all the information that is needed during an interaction
 * with a client.
 * 
 * Mainly this is a set of problems, identified by an unique string, and a set
 * of ongoing requests.
 * 
 * @author plagge
 * 
 */
public class KodkodSession {
	private final SATFactory SOLVER = SolverChecker.determineSatFactory();

	private final Logger logger = Logger.getLogger(KodkodSession.class
			.getName());
	private final Map<String, ImmutableProblem> problems = new HashMap<String, ImmutableProblem>();
	private final Map<ImmutableProblem, Solver> solvers = new HashMap<ImmutableProblem, Solver>();
	private final Map<ImmutableProblem, Request> currentRequests = new HashMap<ImmutableProblem, Request>();
	private boolean stopped = false;

	public void addProblem(final ImmutableProblem problem) {
		String id = problem.getId();
		problems.put(id, problem);

		final Solver solver = new Solver();
		solver.options().setSolver(SOLVER);
		solver.options().setSymmetryBreaking(0);
		final Integer bitwidth = problem.getBitwidth();
		if (bitwidth != null) {
			solver.options().setBitwidth(bitwidth);
		}
		solvers.put(problem, solver);

		info(problem, "added");
	}

	public void deleteProblem(final ImmutableProblem problem) {
		problems.remove(problem.getId());
		currentRequests.remove(problem);
		solvers.remove(problem);
		info(problem, "deleted");
	}

	public void request(final ImmutableProblem problem, final boolean signum,
			final Map<String, TupleSet> newBounds) {
		final Solver solver = solvers.get(problem);
		Request request = problem.createRequest(solver, signum, newBounds);
		currentRequests.put(problem, request);
		info(problem, "request added");
	}

	public ImmutableProblem getProblem(final String problemId) {
		return problems.get(problemId);
	}

	public boolean writeNextSolutions(final ImmutableProblem problem,
			final int size, final IPrologTermOutput pto) {
		Request request = currentRequests.get(problem);
		if (request == null)
			throw new IllegalArgumentException("No request for "
					+ problem.getId());
		info(problem, "list max " + size + " solutions");
		boolean hasSolutions = request.writeNextSolutions(pto, size);
		if (!hasSolutions) {
			currentRequests.remove(problem);
			info(problem, "no more solutions, request deleted");
		}

		return hasSolutions;
	}

	public boolean isStopped() {
		return stopped;
	}

	public void stop() {
		logger.info("session stopped");
		this.stopped = true;
	}

	public void reset() {
		long before = Runtime.getRuntime().freeMemory();
		this.problems.clear();
		this.solvers.clear();
		this.currentRequests.clear();
		System.gc();
		long after = Runtime.getRuntime().freeMemory();
		logger.info("session reseted (" + before
				+ " bytes of free memory before and " + after
				+ " bytes after reset (diff: " + (after - before) + " bytes)");
	}

	private void info(final ImmutableProblem problem, final String info) {
		if (logger.isLoggable(Level.INFO)) {
			logger.info("problem '" + problem.getId() + "': " + info);
		}
	}
}
