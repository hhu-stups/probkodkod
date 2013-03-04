/**
 * 
 */
package de.stups.probkodkod.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import de.prob.prolog.output.StructuredPrologOutput;
import de.prob.prolog.term.PrologTerm;
import de.stups.probkodkod.EOFLexer;
import de.stups.probkodkod.KodkodAnalysis;
import de.stups.probkodkod.KodkodInteraction;
import de.stups.probkodkod.KodkodSession;
import de.stups.probkodkod.parser.lexer.Lexer;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.node.Start;
import de.stups.probkodkod.parser.parser.Parser;
import de.stups.probkodkod.parser.parser.ParserException;

/**
 * Like {@link KodkodInteraction}, but with a one-shot parser to allow
 * single-thread executions of the tests.
 * 
 * @author plagge
 */
public class TestInteraction {
	private final StructuredPrologOutput pto;
	private final KodkodSession session;
	private final KodkodAnalysis analysis;

	public TestInteraction() {
		this.pto = new StructuredPrologOutput();
		this.session = new KodkodSession();
		this.analysis = new KodkodAnalysis(session, pto);
	}

	@SuppressWarnings("unused")
	public PrologTerm sendMessage(final String msg) throws ParserException,
			LexerException, IOException {
		final StringReader sreader = new StringReader(msg);
		final Lexer lexer = new EOFLexer(sreader);
		final Parser parser = new Parser(lexer);
		if (1 == 0) {
			printPositions(msg);
		}
		final Start tree = parser.parse();
		tree.apply(analysis);
		pto.flush();

		if (pto.isSentenceStarted())
			throw new IllegalStateException(
					"there is currently a not finished sentence in the term output");
		Collection<PrologTerm> sentences = pto.getSentences();
		pto.clearSentences();
		if (sentences.size() > 1)
			throw new IllegalStateException(
					"expected one sentence, but there are " + sentences.size());
		return sentences.isEmpty() ? null : sentences.iterator().next();
	}

	private void printPositions(final String msg) {
		printPositions(msg.length(), 100);
		printPositions(msg.length(), 10);
		printPositions(msg.length(), 1);
		System.out.println("\nString: " + msg);
	}

	private void printPositions(final int length, final int e) {
		System.out.print("\n        ");
		for (int i = 1; i <= length; i++) {
			System.out.print(((i % (e * 10)) / e));
		}
	}
}
