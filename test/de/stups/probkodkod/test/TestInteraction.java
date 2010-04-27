/**
 * 
 */
package de.stups.probkodkod.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.Collection;

import de.stups.probkodkod.EOFLexer;
import de.stups.probkodkod.KodkodAnalysis;
import de.stups.probkodkod.KodkodInteraction;
import de.stups.probkodkod.KodkodSession;
import de.stups.probkodkod.parser.lexer.Lexer;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.node.Start;
import de.stups.probkodkod.parser.parser.Parser;
import de.stups.probkodkod.parser.parser.ParserException;
import de.stups.probkodkod.prolog.PrologTerm;
import de.stups.probkodkod.prolog.StructuredPrologOutput;

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
		this.session = new KodkodSession(pto);
		this.analysis = new KodkodAnalysis(session);
	}

	public PrologTerm sendMessage(final String msg) throws ParserException,
			LexerException, IOException {
		final StringReader sreader = new StringReader(msg);
		final Lexer lexer = new EOFLexer(sreader);
		final Parser parser = new Parser(lexer);
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
}
