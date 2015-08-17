package de.stups.probkodkod;

import java.io.IOException;

import org.junit.Test;

import de.stups.probkodkod.parser.lexer.LexerException;
import de.stups.probkodkod.parser.parser.ParserException;

public class TimeoutTest extends InteractionTestBase {

	@Test
	public void testTimeout() throws ParserException, LexerException,
			IOException {
		String problem = load("sendmoremoney.kodkod");
		problem = problem.replaceAll("3500", "10");
		sendMessage(problem + ".");
		sendMessage("request sendmoremoney 100 pos ().");

		checkSATTimeout();

	}
}
