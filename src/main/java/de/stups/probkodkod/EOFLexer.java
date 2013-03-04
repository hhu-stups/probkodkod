package de.stups.probkodkod;

import java.io.IOException;
import java.io.PushbackReader;
import java.io.Reader;

import de.stups.probkodkod.parser.lexer.Lexer;
import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.node.EOF;
import de.stups.probkodkod.parser.node.TFullstop;
import de.stups.probkodkod.parser.node.Token;

/**
 * This class behaves likes the {@link Lexer}, only fullstop tokens are replaced
 * by EOF tokens. This way an input stream can be parsed several times (from
 * fullstop to fullstop) without the need to write the input into an extra
 * buffer and split it manually.
 * 
 * @author plagge
 */
public class EOFLexer extends Lexer {
	public EOFLexer(final PushbackReader in) {
		super(in);
	}

	public EOFLexer(final Reader in) {
		this(new PushbackReader(in, 10));
	}

	@Override
	public Token next() throws LexerException, IOException {
		return filter(super.next());
	}

	@Override
	public Token peek() throws LexerException, IOException {
		return filter(super.peek());
	}

	private Token filter(Token token) {
		if (token instanceof EOF)
			throw new AbortException();
		else if (token instanceof TFullstop) {
			token = new EOF();
		}
		return token;
	}

	public static class AbortException extends RuntimeException {
		private static final long serialVersionUID = 4042645224685292559L;
	}
}
