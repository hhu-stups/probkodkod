package de.stups.probkodkod;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;

import org.junit.Test;

import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.parser.ParserException;
import de.stups.probkodkod.test.Result;
import de.stups.probkodkod.test.ResultSetBuilder;

public class KodkodTest extends InteractionTestBase {
	@Test
	public void testQuantificationOnRelations() throws ParserException,
			LexerException, IOException {
		final String problem = load("relquant.kodkod");
		sendMessage(problem + ".");

		sendMessage("request relquant 10 pos ().");
		List<SortedMap<String, Result>> sol = new LinkedList<SortedMap<String, Result>>();
		getSolutions(false, sol);

		ResultSetBuilder b = new ResultSetBuilder();
		b.set("f", t(0, 0), t(0, 1), t(1, 0), t(1, 1)).store();
		checkSolutions(b.toCollection(), sol);
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
		b.single("i", t(5)).store();
		testAll("intvar", b.toCollection());
	}

	@Test
	public void testIntegerRange() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("r", t(1), t(2), t(3), t(4), t(5), t(6), t(7), t(8)).store();
		testAll("intrange", b.toCollection());
	}

	@Test
	public void testNegativeInteger() throws IOException, ParserException,
			LexerException, InterruptedException {
		ResultSetBuilder b = new ResultSetBuilder();
		// b.set("i", t(-3), t(1));
		b.single("x", t(-9)).store();
		testAll("negative", b.toCollection());
	}

	@Test
	public void testEmptySetInRequest() throws IOException, ParserException,
			LexerException {
		final String problem = load("simpletwovars.kodkod");
		sendMessage(problem + ".");

		// request a solution (max 10) when x is empty (was a bug)
		sendMessage("request simpletwovars 10 pos ((x )).");

		// get the solutions
		List<SortedMap<String, Result>> sol = new LinkedList<SortedMap<String, Result>>();
		getSolutions(false, sol);

		// and check if y is empty
		ResultSetBuilder b = new ResultSetBuilder();
		b.set("y").store();

		checkSolutions(b.toCollection(), sol);
	}

	@Test
	public void testBug1() throws ParserException, LexerException, IOException {
		final String problem = load("returnzero.kodkod");
		sendMessage(problem + ".");

		sendMessage("request returnzero 10 pos ().");
		List<SortedMap<String, Result>> sol = new LinkedList<SortedMap<String, Result>>();
		getSolutions(false, sol);

		ResultSetBuilder b = new ResultSetBuilder();
		b.single("x", t(0)).store();
		checkSolutions(b.toCollection(), sol);
	}

	@Test
	public void testSendMoreMoney() throws ParserException, LexerException,
			IOException {
		final String problem = load("sendmoremoney.kodkod");
		sendMessage(problem + ".");
		sendMessage("request sendmoremoney 100 pos ().");
		List<SortedMap<String, Result>> sol = new LinkedList<SortedMap<String, Result>>();
		getSolutions(false, sol);

		ResultSetBuilder b = new ResultSetBuilder();
		b.single("s", t(9));
		b.single("e", t(5));
		b.single("n", t(6));
		b.single("d", t(7));
		b.single("m", t(1));
		b.single("o", t(0));
		b.single("r", t(8));
		b.single("y", t(2));
		b.store();
		checkSolutions(b.toCollection(), sol);

		for (final Map<String, Result> entry : sol) {

		}
	}
}
