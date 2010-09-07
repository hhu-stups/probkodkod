package de.stups.probkodkod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;
import org.junit.Test;

import de.prob.prolog.term.CompoundPrologTerm;
import de.prob.prolog.term.IntegerPrologTerm;
import de.prob.prolog.term.ListPrologTerm;
import de.prob.prolog.term.PrologTerm;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.parser.ParserException;
import de.stups.probkodkod.test.Result;
import de.stups.probkodkod.test.ResultSetBuilder;
import de.stups.probkodkod.test.TestInteraction;

public class KodkodTest {
	private TestInteraction kodkod;
	private PrologTerm answer;

	@Before
	public void setUp() throws Exception {
		kodkod = new TestInteraction();
	}

	@Test
	public void testLoop() throws ParserException, LexerException, IOException,
			InterruptedException {
		String problem = load("loop.kodkod");

		List<SortedMap<String, Result>> loop1 = new LinkedList<SortedMap<String, Result>>();
		List<SortedMap<String, Result>> loop2 = new LinkedList<SortedMap<String, Result>>();

		// start first request for loop
		sendMessage("request loop 22 pos ().");
		checkUnknown("loop");

		// submit problem loop
		sendMessage(problem + ".");

		// start first request for loop
		sendMessage("request loop 0 pos ().");
		getSolutions(true, loop1);

		// get all answers (7)
		sendMessage("list loop 10.");
		getSolutions(false, loop1);
		checkSolutions(createExpResLoop(), loop1);

		loop1.clear();

		// submit second problem loop2
		sendMessage(problem.replaceAll("loop", "loop2") + ".");

		// start second request for loop (parametrized with lentry=3)
		sendMessage("request loop 0 pos ((lentry <3>)).");
		getSolutions(true, loop1);
		assertTrue(loop1.isEmpty());

		// start first request for loop2 and get first 2 answers (2 of 7)
		sendMessage("request loop2 2 pos ().");
		getSolutions(true, loop2);

		// get all answers for loop (2)
		sendMessage("list loop 10.");
		getSolutions(false, loop1);
		checkSolutions(createExpResLoopP(), loop1);

		sendMessage("request nonexistant 5 pos ().");
		checkUnknown("nonexistant");

		// get rest of answers for loop2 (5 of 7)
		sendMessage("list loop2 10.");
		getSolutions(false, loop2);

		checkSolutions(createExpResLoop(), loop2);
	}

	private static Collection<SortedMap<String, Result>> createExpResLoop() {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("L", t(3)).single("lentry", t(3)).store();
		b.set("L", t(6)).single("lentry", t(6)).store();
		b.set("L", t(6), t(7)).single("lentry", t(6)).store();
		b.set("L", t(2), t(3), t(4)).single("lentry", t(4)).store();
		b.set("L", t(2), t(3), t(4), t(5), t(6), t(7)).single("lentry", t(3))
				.store();
		b.set("L", t(2), t(3), t(4), t(5), t(6), t(7)).single("lentry", t(4))
				.store();
		b.set("L", t(2), t(3), t(4), t(5), t(6), t(7)).single("lentry", t(2))
				.store();
		return b.toCollection();
	}

	private static Collection<SortedMap<String, Result>> createExpResLoopP() {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("L", t(3)).store();
		b.set("L", t(2), t(3), t(4), t(5), t(6), t(7)).store();
		return b.toCollection();
	}

	@Test
	public void testIntegers() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("a", t(0)).set("b", t(1)).set("c", t(0), t(1)).store();
		testAll("integers", b.toCollection());
	}

	@Test
	public void testProjection() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.single("s", t(0, 1)).set("t", t(1, 0, 0, 1)).store();
		testAll("projection", b.toCollection());
	}

	@Test
	public void testFunctions() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("tf", t(0, 0), t(1, 1)).set("pf", t(0, 0), t(1, 1)).store();
		b.set("tf", t(0, 0), t(1, 1)).set("pf", t(0, 0)).store();
		testAll("functions", b.toCollection());
	}

	@Test
	public void testIntegerCasts() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("x", t(5)).set("y", t(7)).store();
		testAll("integercast", b.toCollection());
	}

	@Test
	public void testIntegerVariable() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("i", t(5)).store();
		testAll("intvar", b.toCollection());
	}

	private void testAll(final String name,
			final Collection<SortedMap<String, Result>> expected)
			throws IOException, ParserException, LexerException,
			InterruptedException {
		List<SortedMap<String, Result>> solutions = new LinkedList<SortedMap<String, Result>>();

		String problem = load(name + ".kodkod");
		// debugOutput(problem);
		sendMessage(problem + ".");
		sendMessage("request " + name + " 100 pos ().");
		getSolutions(false, solutions);
		checkSolutions(expected, solutions);
	}

	private static String load(final String filename) throws IOException {
		InputStream input = KodkodTest.class.getResourceAsStream(filename);
		int c = input.read();
		StringBuffer buf = new StringBuffer();
		while (c >= 0) {
			buf.append((char) c);
			c = input.read();
		}
		return buf.toString().replace('\n', ' ');
	}

	private static PrologTerm t(final int... elems) {
		return createIntTuple("t", elems);
	}

	private static PrologTerm createIntTuple(final String functor,
			final int... elems) {
		PrologTerm[] pelems = new PrologTerm[elems.length];
		for (int i = 0; i < elems.length; i++) {
			pelems[i] = new IntegerPrologTerm(elems[i]);
		}
		PrologTerm tuple = new ListPrologTerm(pelems);
		return new CompoundPrologTerm(functor, tuple);
	}

	private void checkSolutions(
			final Collection<SortedMap<String, Result>> expected,
			final Collection<SortedMap<String, Result>> result)
			throws IOException, ParserException, LexerException {
		assertEquals(expected.size(), result.size());
		Set<SortedMap<String, Result>> a = new HashSet<SortedMap<String, Result>>();
		Set<SortedMap<String, Result>> b = new HashSet<SortedMap<String, Result>>();
		a.addAll(expected);
		b.addAll(result);
		assertEquals(a, b);
	}

	private void getSolutions(final boolean moreExpected,
			final Collection<SortedMap<String, Result>> collection)
			throws IOException, ParserException, LexerException {
		CompoundPrologTerm solsterm = (CompoundPrologTerm) answer;
		assertEquals("solutions", solsterm.getFunctor());
		assertEquals(2, solsterm.getArity());
		ListPrologTerm listterm = (ListPrologTerm) solsterm.getArgument(1);
		for (PrologTerm bindings : listterm) {
			ListPrologTerm solterm = (ListPrologTerm) bindings;
			final SortedMap<String, Result> valuemap = extractSolution(solterm);
			collection.add(valuemap);
		}
		final PrologTerm moreTerm = solsterm.getArgument(2);
		if (moreExpected) {
			assertEquals(new CompoundPrologTerm("more"), moreTerm);
		} else {
			assertEquals(new CompoundPrologTerm("all"), moreTerm);
		}
	}

	private SortedMap<String, Result> extractSolution(
			final ListPrologTerm bindings) {
		SortedMap<String, Result> solution = new TreeMap<String, Result>();
		for (PrologTerm elemterm : bindings) {
			CompoundPrologTerm bindterm = (CompoundPrologTerm) elemterm;
			assertEquals("b", bindterm.getFunctor());
			assertEquals(2, bindterm.getArity());
			CompoundPrologTerm nameterm = (CompoundPrologTerm) bindterm
					.getArgument(1);
			assertTrue(nameterm.isAtom());
			final PrologTerm resultTerm = bindterm.getArgument(2);
			final Result result;
			if (resultTerm.isList()) {
				result = new Result.SetResult((ListPrologTerm) resultTerm);
			} else {
				result = new Result.SingletonResult(resultTerm);
			}
			final String name = nameterm.getFunctor();
			solution.put(name, result);
		}
		return solution;
	}

	private void checkUnknown(final String problem) throws IOException {
		final CompoundPrologTerm expected = new CompoundPrologTerm("unknown",
				new CompoundPrologTerm(problem));
		assertEquals(expected, answer);
	}

	private void sendMessage(final String msg)
			throws de.stups.probkodkod.parser.parser.ParserException,
			de.stups.probkodkod.parser.lexer.LexerException, IOException {
		answer = kodkod.sendMessage(msg);
	}
}
