package de.stups.probkodkod;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

import org.junit.Before;

import de.prob.prolog.term.CompoundPrologTerm;
import de.prob.prolog.term.IntegerPrologTerm;
import de.prob.prolog.term.ListPrologTerm;
import de.prob.prolog.term.PrologTerm;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.parser.ParserException;
import de.stups.probkodkod.test.Result;
import de.stups.probkodkod.test.ResultSetBuilder;
import de.stups.probkodkod.test.TestInteraction;

public class InteractionTestBase {

	private TestInteraction kodkod;
	private PrologTerm answer;

	protected static Collection<SortedMap<String, Result>> createExpResLoop() {
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

	@Before
	public void setUp() throws Exception {
		kodkod = new TestInteraction();
	}

	protected static Collection<SortedMap<String, Result>> createExpResLoopP() {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("L", t(3)).store();
		b.set("L", t(2), t(3), t(4), t(5), t(6), t(7)).store();
		return b.toCollection();
	}

	protected static String load(final String filename) throws IOException {
		InputStream input = KodkodTest.class.getClassLoader().getResourceAsStream("problems/"
				+ filename);
		int c = input.read();
		StringBuffer buf = new StringBuffer();
		while (c >= 0) {
			buf.append((char) c);
			c = input.read();
		}
		return buf.toString();
	}

	protected void testAll(final String name,
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

	protected static PrologTerm t(final int... elems) {
		return createIntTuple(elems);
	}

	private static PrologTerm createIntTuple(final int... elems) {
		PrologTerm[] pelems = new PrologTerm[elems.length];
		for (int i = 0; i < elems.length; i++) {
			pelems[i] = new IntegerPrologTerm(elems[i]);
		}
		return new ListPrologTerm(pelems);
	}

	public InteractionTestBase() {
		super();
	}

	protected void checkSolutions(
			final Collection<SortedMap<String, Result>> expected,
			final Collection<SortedMap<String, Result>> result)
			throws IOException, ParserException, LexerException {
		assertEquals(expected.size(), result.size());
		for (final SortedMap<String, Result> expMember : expected) {
			if (!result.contains(expMember)) {
				fail("expected solution '" + expMember
						+ "' not found in result '" + result + "'");
			}
		}
	}

	protected void getSolutions(final boolean moreExpected,
			final Collection<SortedMap<String, Result>> collection)
			throws IOException, ParserException, LexerException {
		CompoundPrologTerm solsterm = (CompoundPrologTerm) answer;
		assertEquals("solutions", solsterm.getFunctor());
		assertEquals(3, solsterm.getArity());
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
		ListPrologTerm durterm = (ListPrologTerm) solsterm.getArgument(3);
		assertEquals("number of durations", listterm.size(), durterm.size());
	}

	private SortedMap<String, Result> extractSolution(
			final ListPrologTerm bindings) {
		SortedMap<String, Result> solution = new TreeMap<String, Result>();
		for (PrologTerm elemterm : bindings) {
			CompoundPrologTerm bindterm = (CompoundPrologTerm) elemterm;
			CompoundPrologTerm nameterm = (CompoundPrologTerm) bindterm
					.getArgument(1);
			assertTrue(nameterm.isAtom());
			final String name = nameterm.getFunctor();
			assertEquals(2, bindterm.getArity());
			final PrologTerm resultTerm = bindterm.getArgument(2);
			final String functor = bindterm.getFunctor();
			Result result = null;
			if ("b".equals(functor)) {
				result = new Result.SingletonResult(resultTerm);
			} else if ("s".equals(functor)) {
				result = new Result.SetResult((ListPrologTerm) resultTerm);
			} else {
				fail("unexpected functor " + functor);
			}
			solution.put(name, result);
		}
		return solution;
	}

	protected void checkUnknown(final String problem) throws IOException {
		final CompoundPrologTerm expected = new CompoundPrologTerm("unknown",
				new CompoundPrologTerm(problem));
		assertEquals(expected, answer);
	}

	protected void sendMessage(final String msg)
			throws de.stups.probkodkod.parser.parser.ParserException,
			de.stups.probkodkod.parser.lexer.LexerException, IOException {
		answer = kodkod.sendMessage(msg);
	}

}